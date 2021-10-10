
马上就毕业设计选题了，跟导师商量了一下想做个论坛，正好手头也缺一个正八经的 SpringBoot 项目（之前只写过案例 demo），所以正好先做个类似的练练手，顺便把我简历上的 SSM 项目替换下来

本项目是一个仿牛客社区的论坛项目，打算实现邮箱注册、验证码登录、发帖、评论、私信、点赞、关注、统计网站访问次数等基本功能，进阶一点的像系统通知，Elasticsearch 全文搜索、生成 pdf 文件、上传云服务器等功能也会去做，这里写一下开发文档方便回顾总结

## 搭建开发环境

• 构建工具：Apache Maven
• 集成开发工具：IntelliJ IDEA
• 数据库：MySQL、Redis
• 应用服务器：Apache Tomcat
• 版本控制工具：Git

后端使用 SpringBoot 整合 Spring Framework 进行开发，前端使用 Thymeleaf，持久层操作暂时使用 MyBatis+HikariCP（之前一直用 Druid）

### 数据库设计

[sql 地址](https://github.com/huaxin0304/Code-Community/tree/master/sql)
