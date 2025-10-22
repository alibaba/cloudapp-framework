package com.taobao.txc.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableFeignClients
public class CloudAppTXCDemoApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudAppTXCDemoApplication.class);

    @Autowired
    private BizService bizService;

    @Autowired
    private JdbcTemplate jdbcTemplateA;

    @Autowired
    private JdbcTemplate jdbcTemplateB;

    public static void main(String[] args) {
        SpringApplication.run(CloudAppTXCDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        int balanceA = jdbcTemplateA.queryForObject("select money from user_money_a where id = 1", Integer.class);
        int balanceB = jdbcTemplateB.queryForObject("select money from user_money_b where id = 1", Integer.class);
        LOGGER.info("当前余额: A: " + balanceA + "   B: " + balanceB + "   total：" + (balanceA + balanceB));
        LOGGER.info("--------------------------------------------------------------------------------------------");
        LOGGER.info("即将开始模拟交易20次, 每次会从 A 转账 300 到 B, 转入/转出时会随机发生异常, 金额不足时也会抛出异常");
        LOGGER.info("--------------------------------------------------------------------------------------------\n\n\n");
        Thread.sleep(3000);

        for (int i = 1; i <= 20; ++i) {
            LOGGER.info("第[" + i + "] 次交易开始");
            try {
                bizService.doTransfer(300);
                LOGGER.info("第[" + i + "] 次交易结束! ");
            } catch (Exception e) {
                LOGGER.info("第[" + i + "] 次交易异常! 异常原因 " + e.getMessage() + ". 金额已经回滚.");
            }
            balanceA = jdbcTemplateA.queryForObject("select money from user_money_a where id = 1", Integer.class);
            balanceB = jdbcTemplateB.queryForObject("select money from user_money_b where id = 1", Integer.class);
            LOGGER.info("当前余额校验: A: " + balanceA + "   B: " + balanceB + "   total：" + (balanceA + balanceB));
            LOGGER.info("--------------------------------------------------------------------------------------------");
            Thread.sleep(1000);
        }

        LOGGER.info("\n\n demo 运行结束");
        System.exit(0);
    }
}
