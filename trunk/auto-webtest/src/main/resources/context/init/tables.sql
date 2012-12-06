create table  CASE_FLOW_MAIN
(
       case_id           INTEGER not null,
       sys_id            INTEGER,
       case_name         VARCHAR2(100),
       start_url         VARCHAR2(500),
       case_type         VARCHAR2(2),
       case_desc         VARCHAR2(4000),
       creator_id        INTEGER,
       created_time      DATETIME,
       modifier_id       INTEGER,
       modified_time     DATETIME
);
alter  table CASE_FLOW_MAIN
       add constraint PK_CASE_FLOW_MAIN_case_id primary key (case_id);


create table  CASE_FLOW_STEP
(
       step_id           INTEGER not null,
       case_id           INTEGER,
       oper_name         VARCHAR2(3),
       page_element      VARCHAR2(100),
       element_location  VARCHAR2(3),
       content           VARCHAR2(4000),
       var               VARCHAR2(100),
       desc              VARCHAR2(4000),
       order             INTEGER,
       creator_id        INTEGER,
       created_time      DATETIME,
       modifier_id       INTEGER,
       modified_time     DATETIME
);
alter  table CASE_FLOW_STEP
       add constraint PK_CASE_FLOW_STEP_step_id primary key (step_id);


create table  CASE_DATA
(
       data_id           INTEGER not null,
       case_id           INTEGER,
       desc              VARCHAR2(4000),
       creator_id        INTEGER,
       created_time      DATETIME,
       modifier_id       INTEGER,
       modified_time     DATETIME
);
alter  table CASE_DATA
       add constraint PK_CASE_DATA_data_id primary key (data_id);


create table  CASE_DATA_STEP
(
       rec_id            INTEGER not null,
       data_id           INTEGER,
       step_id           INTEGER,
       content           VARCHAR2(4000),
       var               VARCHAR2(4000),
       creator_id        INTEGER,
       created_time      DATETIME,
       modifier_id       INTEGER,
       modified_time     DATETIME
);
alter  table CASE_DATA_STEP
       add constraint PK_CASE_DATA_STEP_rec_id primary key (rec_id);


create table  CASE_SYSTEM
(
       sys_id            INTEGER not null,
       sys_name          VARCHAR2(100),
       domain_url        VARCHAR2(500),
       sys_desc          VARCHAR2(4000),
       creator_id        INTEGER,
       created_time      DATETIME,
       modifier_id       INTEGER,
       modified_time     DATETIME
);
alter  table CASE_SYSTEM
       add constraint PK_CASE_SYSTEM_sys_id primary key (sys_id);


create table  CASE_STATISTIC
(
       stat_id           INTEGER not null,
       case_id           INTEGER,
       data_id           INTEGER,
       result_status     VARCHAR2(1),
       fail_desc         VARCHAR2(4000),
       exec_time         DATETIME,
       version           VARCHAR2(100)
);
alter  table CASE_STATISTIC
       add constraint PK_CASE_STATISTIC_stat_id primary key (stat_id);


alter  table CASE_FLOW_MAIN
       add constraint FK_CASE_FLOW_MAIN_sys_id foreign key (sys_id)
       references CASE_SYSTEM(sys_id);

alter  table CASE_FLOW_STEP
       add constraint FK_CASE_FLOW_STEP_case_id foreign key (case_id)
       references CASE_FLOW_MAIN(case_id);

alter  table CASE_DATA
       add constraint FK_CASE_DATA_case_id foreign key (case_id)
       references CASE_FLOW_MAIN(case_id);

alter  table CASE_DATA_STEP
       add constraint FK_CASE_DATA_STEP_data_id foreign key (data_id)
       references CASE_DATA(data_id);
alter  table CASE_DATA_STEP
       add constraint FK_CASE_DATA_STEP_step_id foreign key (step_id)
       references CASE_FLOW_STEP(step_id);



alter  table CASE_STATISTIC
       add constraint FK_CASE_STATISTIC_case_id foreign key (case_id)
       references CASE_FLOW_MAIN(case_id);
alter  table CASE_STATISTIC
       add constraint FK_CASE_STATISTIC_data_id foreign key (data_id)
       references CASE_DATA(data_id);