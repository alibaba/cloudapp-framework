package com.alibaba.cloudapp.microservice.aliyun.demo;

import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

@Service(version = "1.0.0", group = "DUBBO")
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    
    // 模拟数据存储
    private static final Map<Long, Map<String, Object>> ITEM_STORAGE = new HashMap<>();
    
    static {
        // 初始化一些测试数据
        for (long i = 1; i <= 10; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", i);
            item.put("name", "商品" + i);
            item.put("category", i % 3 == 0 ? "数码" : (i % 2 == 0 ? "服装" : "图书"));
            item.put("price", BigDecimal.valueOf(100 + i * 10));
            item.put("stock", (int)(Math.random() * 100) + 10);
            ITEM_STORAGE.put(i, item);
        }
    }

    @Override
    public Map<String, Object> getItem(Long itemId) {
        if (itemId == null) {
            return Collections.emptyMap();
        }
        
        Map<String, Object> item = ITEM_STORAGE.get(itemId);
        logger.info("Get item for itemId: {}, result: {}", itemId, item != null);
        return item != null ? new HashMap<>(item) : Collections.emptyMap();
    }

    @Override
    public List<Map<String, Object>> getItemsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map<String, Object> item : ITEM_STORAGE.values()) {
            if (category.equals(item.get("category"))) {
                result.add(new HashMap<>(item));
            }
        }
        
        logger.info("Get items by category: {}, count: {}", category, result.size());
        return result;
    }

    @Override
    public boolean updateItemPrice(Long itemId, BigDecimal price) {
        if (itemId == null || price == null) {
            return false;
        }
        
        Map<String, Object> item = ITEM_STORAGE.get(itemId);
        if (item != null) {
            item.put("price", price);
            logger.info("Update item price for itemId: {}, newPrice: {}", itemId, price);
            return true;
        }
        
        return false;
    }

    @Override
    public List<Map<String, Object>> searchItems(String keyword, int page, int size) {
        if (keyword == null || keyword.isEmpty() || page < 0 || size <= 0) {
            return Collections.emptyList();
        }
        
        List<Map<String, Object>> allItems = new ArrayList<>();
        for (Map<String, Object> item : ITEM_STORAGE.values()) {
            String name = (String) item.get("name");
            if (name != null && name.contains(keyword)) {
                allItems.add(new HashMap<>(item));
            }
        }
        
        // 分页处理
        int start = page * size;
        int end = Math.min(start + size, allItems.size());
        List<Map<String, Object>> result = start < allItems.size() ? 
            allItems.subList(start, end) : Collections.emptyList();
        
        logger.info("Search items by keyword: {}, page: {}, size: {}, count: {}", 
                keyword, page, size, result.size());
        return result;
    }

    @Override
    public int checkStock(Long itemId) {
        if (itemId == null) {
            return 0;
        }
        
        Map<String, Object> item = ITEM_STORAGE.get(itemId);
        int stock = item != null ? (Integer) item.get("stock") : 0;
        
        logger.info("Check stock for itemId: {}, stock: {}", itemId, stock);
        return stock;
    }
}
