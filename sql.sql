-- iam.login_registered_services definition

CREATE TABLE `login_registered_services` (
                                             `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
                                             `gmt_create` datetime DEFAULT NULL COMMENT 'create time',
                                             `gmt_modified` datetime DEFAULT NULL COMMENT 'modify time',
                                             `body` varchar(8000) NOT NULL COMMENT 'service内容',
                                             `evaluation_order` bigint NOT NULL COMMENT '排序字段',
                                             `evaluation_priority` bigint NOT NULL COMMENT '权重 优先级字段',
                                             `name` varchar(255) NOT NULL COMMENT '名称',
                                             `service_id` varchar(255) NOT NULL COMMENT '匹配域名信息',
                                             `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
                                             `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1604 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='login的service表';

-- iam.service_sequence definition

CREATE TABLE `service_sequence` (
                                    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'primary key',
                                    `gmt_create` datetime DEFAULT NULL COMMENT 'create time',
                                    `gmt_modified` datetime DEFAULT NULL COMMENT 'modify time',
                                    `next_val` bigint NOT NULL COMMENT '下个值',
                                    `creator` varchar(32) DEFAULT NULL COMMENT '创建人',
                                    `modifier` varchar(32) DEFAULT NULL COMMENT '修改人',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='service唯一ID生成表';

-- iam.iam_user definition

CREATE TABLE `iam_user` (
                            `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID自增',
                            `tenant_code` varchar(32) DEFAULT NULL COMMENT '租户ID',
                            `display_name` varchar(64) NOT NULL COMMENT '展示名称',
                            `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
                            `mobile` varchar(32) DEFAULT NULL COMMENT '联系电话',
                            `user_id` varchar(32) NOT NULL COMMENT '用户唯一ID',
                            `account_type` varchar(8) DEFAULT NULL COMMENT '账号类型：LOGIN/OPEN_API',
                            `pwd_policy` varchar(5096) DEFAULT NULL COMMENT '密码策略',
                            `password` varchar(1024) DEFAULT NULL COMMENT '密码',
                            `status` char(1) NOT NULL COMMENT '状态 0锁定 1有效',
                            PRIMARY KEY (`user_id`),
                            UNIQUE KEY `uk_id` (`id`),
                            UNIQUE KEY `uk_mobile` (`mobile`),
                            KEY `idx_tenant_code` (`tenant_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3863 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';