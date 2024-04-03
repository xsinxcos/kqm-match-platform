# Kqm-Match-Platform

**Kqm-match-platform（搭子匹配系统）**是基于Spring-Cloud的分布式微服务的社交小程序项目

## 介绍

Kqm-match-platform 是一款运行于微信生态的社交搭子匹配小程序，用户可在该产品里发
布寻找搭子的帖子，然后和同类型的，行程相近，活动相似的帖子进行匹配。双方用户可
在小程序中进行沟通，选择合适的社交搭子。传统校园墙会发布的一些类似拼单、拼车、
找运动类搭子的求助，但是朋友圈会淹没这些消息，导致求助者有一定概率获得不到帮助，
针对这一痛点，我们开发了这个小程序，对求助信息进行分类，使用开源的推荐引擎，基
于物品的协同过滤、基于用户的协同过滤，给发帖的用户实时匹配合适的搭子，然后进行
沟通。用户还可在首页查看他人的帖子，寻找志同道合，行程合适的小伙伴，并进行沟通，
亦可收藏、评论他人的帖子——Meet 也可作为一个简单的交友平台。

## 技术栈

本项目以Spring-Cloud、SpringBoot作为微服务架构，Nacos作为配置和资源调度中心，使用Open-Feign作为远程调用，MySQL作为数据库、Redis作为缓存中间件、以轻量级的MeiliSeach作为搜索中心、以协同过滤算法为主作为用户爱好推荐算法、集成Spring-Security作为权限校验中心、聊天系统使用了WebSocket作为其通信协议

## 模块介绍

采用 MVC 架构思想搭建各个模块层级，并使用相关设计模式优化架构，使项目易阅读、可扩展
项目树如下

```
kqm-match-platform: 主项目
├─chaos-match-platform-auth 权限校验模块
├─chaos-match-platform-chat	聊天模块
├─chaos-match-platform-common 通用模块
│  ├─chaos-match-platform-common-cache 通用缓存模块
│  ├─chaos-match-platform-common-core  通用核心模块
│  ├─chaos-match-platform-common-database 通用数据库模块
│  └─chaos-match-platform-common-security 通用安全模块
├─chaos-match-platform-feign-api 内部调用API模块
│  ├─match-platform-api-post	帖子模块的内部API
│  ├─match-platform-api-user	用户模块内部API
│  └─match-platform-api-weixin	微信模块内部API
├─chaos-match-platform-gateway	网关模块
├─chaos-match-platform-log		日志模块（未完成）
├─chaos-match-platform-post		帖子模块
├─chaos-match-platform-user		用户模块
├─chaos-match-platform-weixin	微信API对接模块
```

## 架构图

![img](http://www.kdocs.cn/api/v3/office/copy/bG9oZGsyeHRaU1hBTm9yTlZqbmhaNVVJQmpzd1RMT1RrekZ6VHVJTXI2MnB1ck8zOUpTeDJrOXZFSDV5N0ZHSkkweWcyRzVMNnY0d0JKT2h5clZkZm9mb3BBZmpnbUdmN25VTDlkcm1qcWdremNuZ3BiRzdnMWpUYUhQVCthSW9LeGdYeXYzeVNYcUR2cVk4WWp0OU9XdHFYdkVvdElOWEdtNFR0bW5EWjBWelJPdUZaMytBQ3VvOGNMR09Pc2ovMkUxZWhOYmlUOWZ3SXpxbDRLOHEwSHRsRTlWVUhCbG9NakFVZ1ExWThsbXdwRFFITlpUQk1HeVlrRFRzczNjQWwvM0tQS2lraEhzPQ==/attach/object/db452ca6abd0f9a82c31f5b14337c9a5e6fb2167?)

![img](http://www.kdocs.cn/api/v3/office/copy/bG9oZGsyeHRaU1hBTm9yTlZqbmhaNVVJQmpzd1RMT1RrekZ6VHVJTXI2MnB1ck8zOUpTeDJrOXZFSDV5N0ZHSkkweWcyRzVMNnY0d0JKT2h5clZkZm9mb3BBZmpnbUdmN25VTDlkcm1qcWdremNuZ3BiRzdnMWpUYUhQVCthSW9LeGdYeXYzeVNYcUR2cVk4WWp0OU9XdHFYdkVvdElOWEdtNFR0bW5EWjBWelJPdUZaMytBQ3VvOGNMR09Pc2ovMkUxZWhOYmlUOWZ3SXpxbDRLOHEwSHRsRTlWVUhCbG9NakFVZ1ExWThsbXdwRFFITlpUQk1HeVlrRFRzczNjQWwvM0tQS2lraEhzPQ==/attach/object/cd38fd13270d243cbfd52980110c9db9f9b994e9?)
