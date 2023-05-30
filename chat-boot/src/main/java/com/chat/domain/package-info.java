package com.chat.domain;
/**
 * 领域包
 * 1、其下划分好各个领域功能,例如system
 * 2、system下细化为各种包,由repository定义仓储层接口
 * 3、仓储包下实现此处定义的repository接口调用mapper,实现仓储层交互
 * 4、service包下业务逻辑实现application应用包定义的业务逻辑接口,service下的实现类可以调用该领域包下的repository
 * 5、model下划分entity(DO) VO 等
 * */