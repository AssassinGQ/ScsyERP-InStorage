drop table if exists t_in_storage_form;

create table t_in_storage_form (
  id            BIGINT(20)  NOT NULL AUTO_INCREMENT   COMMENT 'ID',
  corporation   BIGINT(20)  NOT NULL                    COMMENT '所属承运方ID',
  #   create_time DATETIME   NOT NULL DEFAULT now()  COMMENT '创建时间',
  #   update_time DATETIME   NOT NULL DEFAULT now()  COMMENT '最后修改时间',
  create_time   DATETIME    NOT NULL DEFAULT now()    COMMENT '创建时间',
  update_time   DATETIME    NOT NULL DEFAULT now()    COMMENT '最后修改时间',
  delete_time   DATETIME                                COMMENT '删除时间',
  is_deleted    BOOLEAN     NOT NULL DEFAULT false    COMMENT '数据是否已经删除',
  project       BIGINT(20)  NOT NULL                    COMMENT '所属项目',
  instorage_status  VARCHAR(15) NOT NULL                COMMENT '入库单状态',
  instorage_number  VARCHAR(30) NOT NULL                COMMENT '入库单号',
  instorage_time    DATETIME    NOT NULL DEFAULT now() COMMENT '入库时间',
  warehouse     BIGINT(20)  NOT NULL                     COMMENT '入库仓库',
  truck         BIGINT(20)  NOT NULL                     COMMENT '入库时运输车辆',
  pick_worker   BIGINT(20)  NOT NULL                     COMMENT '提货人',
  lister        BIGINT(20)  NOT NULL                     COMMENT '制单人',
  drive_workers TEXT                                      COMMENT '行车工们',
  lift_workers  TEXT                                      COMMENT '起重工们',
  account_status VARCHAR(15)                              COMMENT '记账状态',
  total_amount  INT(4)                                    COMMENT '总件数',
  total_volume  DOUBLE(5,2)                               COMMENT '总体积',
  total_weight  DOUBLE(5,2)                               COMMENT '总重量',
  products      TEXT                                      COMMENT '明细',
  working_product  BIGINT(20)                            COMMENT '当前正在入库的货物',
  if_completed  BOOLEAN     NOT NULL DEFAULT FALSE      COMMENT '当前入库单是否已经完成',
  PRIMARY KEY (id)
);

alter table t_in_storage_form comment '入库单信息表';

## 权限的初始化数据
insert into t_in_storage_form(corporation, project, instorage_status, instorage_number, instorage_time, warehouse, truck, pick_worker, lister, if_completed) values (1, 1, "正在入库", "rkh12345", now(), 1, 1, 1, 1, true);