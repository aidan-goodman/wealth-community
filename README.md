
马上就毕业设计选题了，跟导师商量了一下想做个论坛，正好手头也缺一个正八经的 SpringBoot 项目（之前只写过案例 demo），所以正好先做个类似的练练手，顺便把我简历上的 SSM 项目替换下来

本项目是一个仿牛客社区的论坛项目，打算实现邮箱注册、验证码登录、发帖、评论、私信、点赞、关注、统计网站访问次数等基本功能，进阶一点的像系统通知，Elasticsearch 全文搜索、生成 pdf 文件、上传云服务器等功能也会去做，这里写一下开发文档方便回顾总结

## 搭建开发环境

* 构建工具：Apache Maven
* 集成开发工具：IntelliJ IDEA
* 数据库：MySQL、Redis
* 应用服务器：Apache Tomcat
* 版本控制工具：Git

后端使用 SpringBoot 整合 Spring Framework 进行开发，前端使用 Thymeleaf，持久层操作暂时使用 MyBatis+HikariCP（之前一直用 Druid）

首先配置 DataSource，设置基本连接信息、最大线程数，最小空闲线程数，最大空闲时间等

mybatis，设置 mapper 文件的位置、实体类包名、使用主键等

关闭 thymeleaf 缓存，方便修改模板不用重启


### 数据库设计

[sql 地址](https://github.com/huaxin0304/Code-Community/tree/master/sql)

字段含义写在注释上

#### 数据库访问测试

用户相关操作：

* 创建对应 user 表的 User 实体类
* 创建 UserMapper 接口，使用 @Mapper 注解
* 创建 user-mapper.xml，重复 sql 语句可以写在 <sql id = "xxx"> 标签，通过 <include refid="xxx"/> 引用

### 开发社区首页

功能拆分：开发社区首页，显示前 10 个帖子；开发分页组件，分页显示所有帖子

用到的表是 discuss_post 数据库表，包括帖子 id、发帖人 id、标题、内容、类型、状态、发帖时间、评论数量（为了提高效率，避免关联查询，因此冗余存储）、分数（用于进行热度排名）。

#### 开发数据层

帖子相关操作：

创建对应 discuss_post 表的 DisscussPost 实体类、DisscussPostMapper 接口

分页查询中用户 id 是可选参数，通过动态 SQL 选择，如果为 0 就不使用，在开发用户个人主页查询用户发帖记录时需要使用 ，如果只有一个参数，并且在动态 SQL 的 <if> 里使用，必须使用 @Param 加别名

创建 disscusspost-mapper.xml

`where status != 2` 拉黑的帖子不展现

`<if test="userId!=0">` userID 为 0 时不使用，按照类型，发帖时间排序

开发业务层
创建 DiscussPostService 类，可以分页查询帖子和帖子数量

创建 UserService 类，实现根据 id 查询用户功能，因为显示帖子时不显示用户 id，而是显示用户名
