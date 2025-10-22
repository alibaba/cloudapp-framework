DROP TABLE IF EXISTS `user_money_a`;
CREATE TABLE `user_money_a` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `money` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `txc_undo_log`;
CREATE TABLE `txc_undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `xid` varchar(100) NOT NULL COMMENT '全局事务ID',
  `branch_id` bigint(20) NOT NULL COMMENT '分支事务ID',
  `rollback_info` longblob NOT NULL COMMENT 'LOG',
  `status` int(11) NOT NULL COMMENT '状态',
  `server` varchar(32) NOT NULL COMMENT '服务器',
  PRIMARY KEY (`id`),
  KEY `idx_unionkey` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='事务日志表';