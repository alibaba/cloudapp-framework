package com.taobao.txc.sample;

import javax.annotation.PostConstruct;

import com.taobao.txc.client.aop.annotation.TxcTransaction;
import com.taobao.txc.common.TxcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class BizService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BizService.class);

    @Autowired
    private JdbcTemplate jdbcTemplateA;

    @Autowired
    private JdbcTemplate jdbcTemplateB;

    @TxcTransaction
    public void doTransfer(int money) {
        LOGGER.info("GTS全局事务开始, 事务ID: " + TxcContext.getCurrentXid());
        // 从数据源 A 的帐户扣款
        jdbcTemplateA.update("update user_money_a set money = money - ? where id = 1", money);
        if (new Random().nextInt(100) < 10) {
            throw new RuntimeException("A 库转出时发生了一个随机异常 ");
        }

        // 从数据源 B 的帐户存款
        jdbcTemplateB.update("update user_money_b set money = money + ? where id = 1", money);
        if (new Random().nextInt(100) < 30) {
            throw new RuntimeException("B 库转入时发生了一个随机异常");
        }

        //余额不足时抛异常，GTS会回滚前两次操作
        int money_a = jdbcTemplateA.queryForObject("select money from user_money_a where id = 1", Integer.class);
        if (money_a < 0) {
            throw new RuntimeException("A 库金额不足, 交易失败");
        }
    }

    @PostConstruct
    public void initData() {
        jdbcTemplateA.update("delete from user_money_a where id = 1");
        jdbcTemplateB.update("delete from user_money_b where id = 1");
        jdbcTemplateA.update("insert into user_money_a(id, money) values(1, 5000)");
        jdbcTemplateB.update("insert into user_money_b(id, money) values(1, 0)");
        LOGGER.info("\n\n数据初始化完成.......\n\n");
    }
}
