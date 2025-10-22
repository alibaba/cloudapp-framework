/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.alibaba.cloudapp.observabilities.opentelemetry.metric;

import com.alibaba.cloudapp.api.observabilities.InvokeCount;
import com.alibaba.cloudapp.api.observabilities.Metric;
import com.alibaba.cloudapp.api.observabilities.MetricCollection;
import com.alibaba.cloudapp.observabilities.opentelemetry.util.FieldMethodArgsWrapper;
import com.alibaba.cloudapp.observabilities.opentelemetry.util.MetricConverter;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.metrics.*;
import io.opentelemetry.semconv.ServiceAttributes;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Aspect
@Slf4j
public class MetricCollectionAspect {

    /**
     * Store all the MetricCollection by serviceName.
     */
    public final static Map<String, Meter> Meters = new HashMap<>();
    /**
     * Store all the metrics by metric name.
     */
    public final static Map<String, Object> Metrics = new HashMap<>();
    /**
     * store the method attributes expression by metric name.
     */
    Map<String, Map<String, Expression>> attrExprs = new HashMap<>();
    /**
     * store the method attributes key-value pairs by metric name.
     */
    Map<String, Map<String, String>> attrPairs = new HashMap<>();
    /**
     * Store classes which have been initialized, to avoid repeat initialization
     */
    Map<String, Boolean> initializedClassesCache = new HashMap<>();
    
    /**
     * SpEL parser to parse methods attributes
     */
    private final ExpressionParser parser = new SpelExpressionParser();
    
    /**
     * Defined the inner counter name of the method.
     */
    public static String INNER_COUNTER_TAG = "inner.counter";

    public static String DEFAULT_SCOPE_VERSION = "1.0.0";

    /**
     * Prefix key of method attributes expression, if value not start with this
     * prefix, it will be considered as simple constant.
     */
    public static String PRESERVE_EXPR_TAG = "args";

    public Method[] getMethods(ProceedingJoinPoint pjp) {
        return getPointClass(pjp).getMethods();
    }
    
    private Field[] getFields(ProceedingJoinPoint pjp) {
        return getPointClass(pjp).getDeclaredFields();
    }
    
    private MetricCollection getAnnotations(ProceedingJoinPoint pjp) {
        return getPointClass(pjp).getAnnotation(MetricCollection.class);
    }
    
    private void initialMeter(ProceedingJoinPoint pjp) {
        Class<?> pjpClass = getPointClass(pjp);
        MetricCollection collection = getAnnotations(pjp);
        Field[] fields = getFields(pjp);
        Method[] methods = getMethods(pjp);
        
        // fields processor
        initialFields(fields, pjp, collection);

        // method processor
        initialMethods(methods, pjp, collection);
        
        // cache the initialized class, to avoid repeat initialization
        initializedClassesCache.put(pjpClass.getName(), true);
    }
    
    /**
     * Process the fields annotated by Metric{@link Metric} of the class.
     * @param fields the class all fields
     * @param pjp the joinPoint to process
     * @param collection the MetricCollection{@link MetricCollection} annotation of the
     *                   class
     */
    private void initialFields(Field[] fields, ProceedingJoinPoint pjp,
                               MetricCollection collection) {
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Metric.class)) {
                    Metric metric = field.getAnnotation(Metric.class);
                    prepareAttributes(collection.serviceName(), metric.name(),
                                      metric.attributes());
                    if (Metrics.containsKey(metric.name())) {
                        return;
                    }
                    switch (metric.type()) {
                        case COUNTER:
                            storeCounter(collection, metric, pjp, field);
                            break;
                        case HISTOGRAM:
                            storeHistogram(collection, metric, pjp, field);
                            break;
                        case GAUGE:
                            storeGauge(collection, metric, pjp, field);
                            break;
                        default:
                            log.warn("invalid meter type {}", metric.type());
                    }
                }
            } catch (Exception e) {
                log.error("error when initial meter", e);
            }
        }
    }
    
    /**
     * Initialize the annotated methods, such as method with annotation.
     * InvokeCount{@link InvokeCount}.
     * @param methods the class all methods
     * @param pjp the joinPoint to process
     * @param collection the MetricCollection{@link MetricCollection} annotation of the
     *                   class
     */
    private void initialMethods(Method[] methods, ProceedingJoinPoint pjp,
                                MetricCollection collection) {
        for (Method method : methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(InvokeCount.class)) {
                InvokeCount invokeCount = method.getAnnotation(InvokeCount.class);
                Metric metric = MetricConverter.convert(invokeCount);
                if (Metrics.containsKey(invokeCount.name())) {
                    continue;
                }
                prepareAttributes(collection.serviceName(), metric.name(),
                                  metric.attributes());
                LongCounterBuilder counterBuilder = getCounterBuilder(
                        pjp, collection, metric);
                Metrics.put(metric.name(), counterBuilder.build());
            }
        }
    }
    
    /**
     * Build a counter builder for counter metric.
     * @param collection the MetricCollection{@link MetricCollection}
     *                  annotation of the annotated class
     * @param metric the Metric{@link Metric} annotation of the annotated method
     * @return a LongCounterBuilder of OpenTelemetry-SDK
     */
    private LongCounterBuilder getCounterBuilder(
            ProceedingJoinPoint pjp, MetricCollection collection, Metric metric) {
        return getMeter(pjp, collection.scopeName(), collection.scopeVersion())
                .counterBuilder(metric.name())
                .setDescription(metric.description())
                .setUnit(metric.unit());
    }
    
    /**
     * Store the counter metric meter while initialization.
     */
    private void storeCounter(MetricCollection collection, Metric metric,
                              ProceedingJoinPoint pjp, Field field) {
        LongCounterBuilder counterBuilder = getCounterBuilder(
                pjp, collection, metric);
        if (metric.async()) {
            // if the metric is defined as async collection, build a callback
            // additionally.
            Metrics.put(metric.name(),
                        counterBuilder.buildWithCallback(
                                counterCallback(pjp, field, metric.name())));
        } else {
            Metrics.put(metric.name(), counterBuilder.build());
        }
    }
    
    /**
     * Store the histogram metric meter while initialization.
     * Histogram is not support the async collection.
     */
    private void storeHistogram(MetricCollection provider, Metric metric,
                                ProceedingJoinPoint pjp, Field field) {
        DoubleHistogramBuilder doubleHistogram =
                getMeter(pjp, provider.scopeName(), provider.scopeVersion())
                        .histogramBuilder(metric.name())
                        .setDescription(metric.description())
                        .setUnit(metric.unit());
        // set bucket boundaries if set
        if (metric.buckets() != null && metric.buckets().length > 0) {
            doubleHistogram.setExplicitBucketBoundariesAdvice(
                    Arrays.stream(metric.buckets()).boxed().collect(Collectors.toList()));
        }
        Metrics.put(metric.name(), doubleHistogram.build());
    }
    
    /**
     * Store the gauge metric meter while initialization.
     */
    private void storeGauge(MetricCollection provider, Metric metric,
                             ProceedingJoinPoint pjp, Field field) {
        DoubleGaugeBuilder gaugeBuilder =
                getMeter(pjp, provider.scopeName(), provider.scopeVersion())
                        .gaugeBuilder(metric.name())
                        .setDescription(metric.description())
                        .setUnit(metric.unit());
        if (metric.async()) {
            Metrics.put(metric.name(),
                        gaugeBuilder.buildWithCallback(
                                gaugeCallback(pjp, field, metric.name())));
        } else {
            Metrics.put(metric.name(), gaugeBuilder.build());
        }
    }
    
    /**
     * Parses the method attributes string into a map of attribute names to Expression objects using SpEL.
     * The attribute string format is "key1=value1;key2=value2", where value can be an SpEL expression.
     * For example: "myAttr1=args[0].name;myAttr2=args[1]"
     *
     * @param attrStr The attribute string, containing multiple key-value pairs separated by ";"
     */
    private void prepareAttributes(String serviceName, String metricName,
                                   String attrStr) {
        Map<String, Expression> attrExpressPair = new HashMap<>();
        Map<String, String> attrPair = new HashMap<>();
        attrPair.put(ServiceAttributes.SERVICE_NAME.getKey(), serviceName);
        String[] attrs = attrStr.split(";");
        for (String attr : attrs) {
            // if not contains "=", skip this pair
            if (Strings.isBlank(attr) || !attr.contains("=")) {
                continue;
            }
            try {
                String exprStr = attr.split("=")[1];
                if (!exprStr.startsWith(PRESERVE_EXPR_TAG)) {
                    // if expression value not starts with "args" key,
                    // considered as simple value
                    attrPair.put(attr.split("=")[0], exprStr);
                } else {
                    Expression attrExpr = parser.parseExpression(
                            exprStr + ".toString()");
                    attrExpressPair.put(attr.split("=")[0], attrExpr);
                }
            } catch (ParseException e) {
                log.warn("invalid parser string {}", attrStr);
            }
        }
        if (!attrPair.isEmpty()) {
            attrPairs.put(metricName, attrPair);
        }
        if (!attrExpressPair.isEmpty()) {
            attrExprs.put(metricName, attrExpressPair);
        }
    }
    
    /**
     * Build the attributes for the metric when the metric is recorded.
     * @param metricName the name of the metrics
     * @param args the arguments of the method
     * @return Metric attributes object
     */
    private Attributes buildAttributes(String metricName,
                                       FieldMethodArgsWrapper args) {
        if (!Metrics.containsKey(metricName) ||
                (!attrExprs.containsKey(metricName)) &&
                        !attrPairs.containsKey(metricName)) {
            return null;
        }
        Map<String, Expression> exprs = attrExprs.get(metricName);
        Map<String, String> attrs = attrPairs.get(metricName);
        AttributesBuilder builder = Attributes.builder();
        if (exprs != null && args.getArgs() != null) {
            for (Map.Entry<String, Expression> entry : exprs.entrySet()) {
                if (entry == null || entry.getValue() == null
                        || entry.getValue().getValue(args) == null) {
                    continue;
                }
                // add the expression result to the metric attributes
                builder.put(entry.getKey(),
                            String.valueOf(entry.getValue().getValue(args)));
            }
        }
        if (attrs != null && args != null) {
            for (Map.Entry<String, String> entry : attrs.entrySet()) {
                builder.put(entry.getKey(), entry.getValue());
            }
        }
        // add the method name to the metric attributes
        builder.put(INNER_COUNTER_TAG, args.getName());
        return builder.build();
    }

    /**
     * Counter callback is build for async counter. This callback will be
     * invoked when the async collection is triggered.
     * @param pjp join point
     * @param field field value represent the value of the metric
     * @return the callback consumer
     */
    public Consumer<ObservableLongMeasurement> counterCallback(
            ProceedingJoinPoint pjp, Field field, String metricName) {
        return measurement -> {
            try {
                FieldMethodArgsWrapper args =
                        new FieldMethodArgsWrapper(field.getName(), null);
                Attributes attrs = buildAttributes(metricName, args);
                if (attrs != null) {
                    measurement.record((long) field.get(pjp.getTarget()), attrs);
                } else {
                    measurement.record((long) field.get(pjp.getTarget()));
                }
            } catch (IllegalAccessException e) {
                log.error("failed get field long value: {}", e.getMessage());
            }
        };
    }
    
    /**
     * Gauge callback is build for async counter. This callback will be
     * invoked when the async collection is triggered.
     * @param pjp join point
     * @param field field value represent the value of the metric
     * @return the callback consumer
     */
    private Consumer<ObservableDoubleMeasurement> gaugeCallback(
            ProceedingJoinPoint pjp, Field field, String metricName) {
        return measurement -> processGaugeCallback(pjp, field, metricName, measurement);
    }
    
    void processGaugeCallback(ProceedingJoinPoint pjp,
                              Field field,
                              String metricName,
                              ObservableDoubleMeasurement measurement) {
        try {
            FieldMethodArgsWrapper args =
                    new FieldMethodArgsWrapper(field.getName(), null);
            Attributes attrs = buildAttributes(metricName, args);
            Number num = (Number) field.get(pjp.getTarget());
            if (attrs != null) {
                measurement.record(num.doubleValue(), attrs);
            } else {
                measurement.record(num.doubleValue());
            }
        } catch (IllegalAccessException e) {
            log.error("failed get field double value: {}", e.getMessage());
        }
    }
    
    /**
     * The JointPoint of the MetricCollection. It will initialize the fields
     * and the methods of the target class.
     */
    @Around("@within(io.cloudapp.api.observabilities.MetricCollection)")
    public Object aop(ProceedingJoinPoint pjp) throws Throwable {
        String className = getPointClass(pjp).getName();
        if (!initializedClassesCache.containsKey(className)) {
            initialMeter(pjp);
        }
        return pjp.proceed();
    }
    
    /**
     * Handle the field of the MetricCollection when setter method is invoked.
     * Trigger the related metric to record value.
     */
    @Around("@within(io.cloudapp.api.observabilities.MetricCollection) && " +
            "execution(* set*(..))")
    public Object filedHandler(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        // get the updated filed name, e.g: setName -> name
        String fieldName = getUpdatedFiledName(methodName);
        // if not match the pattern, return directly
        if (fieldName == null) {
            return pjp.proceed();
        }
        Class<?> pjpClass = getPointClass(pjp);
        Field field;
        try {
            field = getFieldByName(pjpClass, fieldName);
            processField(pjp, field);
        } catch (NoSuchFieldException e) {
            log.warn("failed to get field: {}", e.getMessage());
        }  catch (IllegalAccessException e) {
            log.warn("illegal access to get field: {}", e.getMessage());
        }
        return pjp.proceed();
    }
    
    /**
     * Process the field setter event, get the metric from metrics and record
     * the value.
     */
    private void processField(ProceedingJoinPoint pjp, Field field)
            throws IllegalAccessException {
        field.setAccessible(true);
        if (field.isAnnotationPresent(Metric.class)) {
            Metric meterAnno = field.getAnnotation(Metric.class);
            if (!meterAnno.async()) {
                String metricName = meterAnno.name();
                Object metric = Metrics.get(metricName);
                // parse field attributes
                FieldMethodArgsWrapper args = new FieldMethodArgsWrapper(
                        field.getName(), null);
                Attributes attributes = buildAttributes(metricName, args);
                if (metric == null) {
                    log.warn("failed get metric {} in cache, maybe " +
                                     "initialized failed.", metricName);
                    return;
                }
                String originFieldValue = field.get(pjp.getTarget()).toString();
                String newFieldValue = pjp.getArgs()[0].toString();
                // metric type: histogram, invoke record method to store value
                if (metric instanceof DoubleHistogram) {
                    DoubleHistogram dh = (DoubleHistogram) metric;
                    double value = Double.parseDouble(newFieldValue);
                    if (attributes == null) {
                        dh.record(value);
                    } else {
                        dh.record(value, attributes);
                    }
                }
                // metric type: gauge, invoke set method to store value
                if (metric instanceof DoubleGauge) {
                    DoubleGauge dg = (DoubleGauge) metric;
                    double value = Double.parseDouble(newFieldValue);
                    if (attributes == null) {
                        dg.set(value);
                    } else {
                        dg.set(value, attributes);
                    }
                }
                // metric type: counter, invoke add method to store value
                if (metric instanceof LongCounter) {
                    LongCounter lc = (LongCounter) metric;
                    long delta = Long.parseLong(newFieldValue) -
                            Long.parseLong(originFieldValue);
                    if (attributes == null) {
                        lc.add(delta);
                    } else {
                        lc.add(delta, attributes);
                    }
                }
            }
        }
    }
    
    /**
     * JoinPoint of the method annotated by InvokeCount.
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("@annotation(io.cloudapp.api.observabilities.InvokeCount)")
    public Object invokeCountHandler(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        Object[] methodArgs = pjp.getArgs();
        String metricName = getMetricNameOfInvokeCount(pjp);
        String methodName = pjp.getSignature().getName();
        
        if (!Metrics.containsKey(metricName)) {
            log.warn("InvokeCount annotation: {} not initialized", metricName);
            return result;
        }

        Object metric = Metrics.get(metricName);
        if (metric instanceof LongCounter) {
            LongCounter lc = (LongCounter) metric;
            FieldMethodArgsWrapper args = new FieldMethodArgsWrapper(
                    methodName, methodArgs);
            Attributes attrs = buildAttributes(metricName, args);
            if (attrs != null) {
                lc.add(1L, attrs);
            } else {
                lc.add(1L);
            }
        }

        return result;
    }
    
    /**
     * Get the InvokeCount join point's metric name.
     */
    private String getMetricNameOfInvokeCount(ProceedingJoinPoint pjp)
            throws NoSuchMethodException {
        String methodName = pjp.getSignature().getName();
        // consider overload scenario, get the parameters type of the
        // join point method first
        Class<?>[] parameterTypes = ((MethodSignature) pjp.getSignature()).getParameterTypes();
        Method invokedMethod = getPointClass(pjp).getMethod(
                methodName, parameterTypes);

        InvokeCount invokeCount = invokedMethod.getAnnotation(InvokeCount.class);
        if (invokeCount == null) {
            return null;
        }

        if (Strings.isBlank(invokeCount.name())) {
            return methodName;
        }

        return invokeCount.name();
    }
    
    // Get pojo setter join point method related field name
    private String getUpdatedFiledName(String methodName) {
        if (!methodName.startsWith("set")) {
            return null;
        }
        String fieldName = methodName.substring(3);
        if (!fieldName.isEmpty()) {
            return Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
        } else {
            return null;
        }
    }
    
    /**
     * Get OpenTelemetry Instance.
     */
    public OpenTelemetry getOpenTelemetry() {
        return GlobalOpenTelemetry.getOpenTelemetry();
    }
    
    /**
     * get meter instance or create meter instance when not exist.
      */
    private Meter getMeter(ProceedingJoinPoint pjp, String scopeName,
                           String scopeVersion) {
        String meterKey = generateMeterKey(pjp, scopeName, scopeVersion);
        
        if (Meters.get(meterKey) != null) {
            return Meters.get(meterKey);
        }
        Meter meter = getOpenTelemetry()
                      .getMeterProvider()
                      .meterBuilder(scopeName)
                      .setInstrumentationVersion(scopeVersion)
                      .build();
        
        Meters.put(meterKey, meter);
        return meter;
    }
    
    private String generateMeterKey(ProceedingJoinPoint pjp, String scopeName,
                                    String scopeVersion) {
        if (scopeName == null || scopeVersion.isEmpty()) {
            scopeName = getPointClass(pjp).getName();
        }

        if (scopeVersion == null || scopeVersion.isEmpty()) {
            scopeVersion = DEFAULT_SCOPE_VERSION;
        }
        
        return scopeName + ":" + scopeVersion;
    }
    
    public Class<?> getPointClass(ProceedingJoinPoint pjp) {
        if(pjp.getTarget() == null) {
            return pjp.getSignature().getDeclaringType();
        }
        
        return pjp.getTarget().getClass();
    }
    
    private Field getFieldByName(Class<?> clazz, String fieldName)
            throws NoSuchFieldException {
        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            return clazz.getDeclaredField(fieldName);
        }
    }
}
