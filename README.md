马上就毕业设计选题了，跟导师商量了一下想做个论坛，正好手头也缺一个正八经的 SpringBoot 项目（之前只写过案例 demo），所以正好先做个类似的练练手，顺便把我简历上的 SSM 项目替换下来

本项目是一个仿牛客社区的论坛项目，打算实现邮箱注册、验证码登录、发帖、评论、私信、点赞、关注、统计网站访问次数等基本功能，进阶一点的像系统通知，Elasticsearch 全文搜索、生成 pdf
文件、上传云服务器等功能也会去做，这里写一下开发文档方便回顾总结

## 搭建开发环境

* 构建工具：Maven
* 数据库：MySQL、Redis
* 应用服务器：Tomcat
* VCS 工具：Git

后端使用 SpringBoot 整合 Spring Framework 进行开发，前端使用 Thymeleaf，持久层操作暂时使用 MyBatis + HikariCP（之前一直用 Druid）

首先配置 DataSource，设置基本连接信息、最大线程数，最小空闲线程数，最大空闲时间等

mybatis，设置 mapper 文件的位置、实体类包名、使用主键等

关闭 thymeleaf 缓存，方便修改模板不用重启

### 数据库设计

[sql 地址](https://github.com/huaxin0304/Code-Community/tree/master/sql)

#### 用户表

user:

字段          |类型         |解释
:---:       |:---:      |:---:
id          |int        |主键、自增
username    |varchar    |用户名，创建索引
password    |varchar    |用户密码
salt        |varchar    |加密盐值
email       |varchar    |用户邮箱，创建索引
type        |int    |用户类型：0 普通、1 管理员、2 版主
status      |int    |用户状态：0 未激活、1 已激活
activation_code |varchar        |激活码
header_url      |varchar        |用户头像地址
create_time     |timestamp      |注册时间

解释：

* 对常用字段如用户名和邮箱进行索引的创建
* 密码采用加盐的方式进行存储，保证安全性

#### 帖子表

discuss_post:

字段	|类型	|备注
:---:       |:---:      |:---:
id	        |int	    |主键、自增
user_id	    |int	    |发帖的用户 id，创建索引
title	    |varchar	|帖子表标题
content	    |text	    |帖子内容
type	    |int	    |帖子类型：0 普通、1 置顶
comment_count	|int	|评论数量
status	    |int	|帖子状态：0 普通、1 精华、2 拉黑
create_time	    |timestamp	    |评论发表时间

#### 数据库访问测试

用户相关操作：

* 创建对应 user 表的 User 实体类
* 创建 UserMapper 接口，使用 @Mapper 注解
* 创建 user-mapper.xml，重复 sql 语句可以写在 <sql id = "xxx"> 标签，通过 <include refid="xxx"/> 引用

## 代码编写

### 开发社区首页

功能拆分：开发社区首页，显示前 10 个帖子；开发分页组件，分页显示所有帖子

用到的表是 discuss_post 数据库表，包括帖子 id、发帖人 id、标题、内容、类型、状态、发帖时间、评论数量（为了提高效率，避免关联查询，因此冗余存储）、分数（用于进行热度排名）。

#### 开发数据层

帖子相关操作：

创建对应 discuss_post 表的 DisscussPost 实体类、DisscussPostMapper 接口

分页查询中用户 id 是可选参数，通过动态 SQL 选择，如果为 0 就不使用

创建 disscusspost-mapper.xml

`where status != 2` 拉黑的帖子不展现

`<if test="userId!=0">` userID 为 0 时不使用，按照类型，发帖时间排序

#### 开发业务层

创建 DiscussPostService 类，可以分页查询帖子和帖子数量

创建 UserService 类，实现根据 id 查询用户功能，因为显示帖子时不显示用户 id，而是显示用户名

---

视图层没什么说的，用的是网上现有的前端素材，使用 Thymeleaf 对其进行动态更换

把每个帖子做成一个 map 集合，在前端遍历输出即可

#### 分页组件的开发

`后续会使用 PageHelper 进行重构`

创建 Page 实体类，封装分页信息，包括当前页码、显示限制、帖子总数、查询路径等。显示的起始页不能小于 1，最大页不能超过 total

在 index.html 中，当 page.rows > 0 时显示分页信息

如果 page.current 等于 1 或 page.total，代表是首页或末页，此时不能点击上一页和下一页，使用 Bootstrap 分页组件中 `disabled` 属性实现

### 开发登录模块

#### 添加邮件发送功能

导入 `spring-boot-starter-mail` 依赖，进行端口和主机的配置，用户密码使用 smtp 的授权码而不是邮箱登录密码，否则报错如下：

```
Authentication failed; nested exception is javax.mail.AuthenticationFailedException: 535 Error: authentication failed
```

编写邮箱工具类方便操作，在工具类里打开 html 格式

#### 开发注册功能

首先将功能进行拆分，子功能包括：

* 进行导航栏跳转，为方便操作进行了公共页面的抽取操作
* 进行填写字段的检验操作（后端方面），主要判断字段是否为空及数据库是否已经存在
* 进行存储操作，考虑随机头像，密码加盐加密，激活码与当前状态
* 验证邮箱，使用 `userId` + 激活码的方式进行激活操作，更新状态

期间使用 map 存储错误信息，验证时对冗余结果进行封装；前端进行字段回填与错误提示

#### 验证码功能

导入 Google 的 Kaptcha 依赖后使用 Configuration 类进行 Bean 的声明，封装一个验证码生成的接口，将 text 写入到 Session 中后将 text 绘制成 image 显示在页面上

#### 登录功能

考虑到状态的判断，在用户登录时进行登录凭证的封装。Dao 层封装 login_ticket 的插入，status 更新，和登录凭证的查询

用户登录时进行格式的校验，状态的校验和密码比对与封装，将每阶段信息封装到 map 中进行前端界面的显示

用户退出登录时将 ticket 的 status 更新为 1

#### 进行登录状态检查

使用拦截器完成对登录状态的检查，具体流程如下：

> 客户端发起请求 --> Interceptor 进行拦截 --> 进行方法处理（获取 Cookie 中的登录凭证 --> 检验有效性后获取 User 对象） --> 将 User 在视图上进行解析

为防止并发冲突使用 ThreadLocal 进行 User Object 的存储