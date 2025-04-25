# spero-seed-base-backend

🚀 **一个高度模块化、开箱即用的企业级Java后端开发框架**
集成高频业务场景解决方案，助力快速构建稳健的Spring Boot应用



## ✨ 核心特性

### 🧩**依赖管理模块**

- 集中管理项目所有第三方依赖及其版本
- 强制依赖版本锁定
- 适用于maven



### 📚 **智能接口文档生成模块**
- 基于knife4j自动生成RESTful接口文档
- 支持结合环境配置接口文档功能是否开启
- 支持导出Markdown、Html、Word 和 Json 格式文档



### 🧠 **Redis集成模块**
- 支持Lettuce连接池与集群模式
- 提供注解式缓存管理 (`@RedisCache`)



### 🗃️ **数据源管理模块**
- 动态数据源切换
- 集成Druid连接池
- 集成MyBatis-Plus增强ORM功能
- 支持创建时间与更新时间的自动插入



### ⚙️ **核心基础模块**
- 请求响应信息封装和分页请求封装
- 封装任务线程池，支持动态设置线程数量，结合了日志追踪ID透传
- 支持声明式和编程式进行参数校验
- 支持加密springboot配置项



### 🌐 **Web应用模块**
- 统一异常处理
- 日志追踪ID透传（MDC + SLF4J）
- 支持请求和响应流可重复读
- 请求信息与响应信息日志输出



## 🛠️ 技术栈  

- **基础框架**: Spring Boot 2.x+Spring Web 5.x
- **数据层**: MyBatis-Plus + Druid + Redis 6.x
- **工具链**: Lombok + Hutool + Fastjson2
- **文档生成**: Swagger 3.0 + Knife4j



## 🚀 快速开始  

```bash
# 1. 克隆项目
https://github.com/spero-seed/spero-seed-base-backend.git

# 2. 打开 spero-seed-dependencies 工程，修改pom文件中的私服配置，部署到私服仓库

# 3. 打开 spero-seed-base 工程，修改pom文件中的私服配置，部署到私服仓库
```



## 📂 项目结构  

```
├── spero-seed-dependencies     # 依赖管理模块
├── spero-seed-base             # Web应用模块
    ├── spero-seed-core      	# 核心基础模块
    ├── spero-seed-doc     	    # 接口文档模块
    ├── spero-seed-web       	# Web应用模块
    ├── spero-seed-datasource   # 数据源管理模块
    └── spero-seed-redis		# Redis模块
```



## 📦配置项

spero-seed-core

```yml
speroseed:
	threadPoolSize: 配置内置的任务线程池，默认线程数是逻辑处理核心的2倍
```



spero-seed-doc

```yml
springdoc:
	title: 标题
	description: 简介
	author: 作者
	version: 版本
	excludeProfiles:
		- 通过列表的形式配置需要排除的环境。对于排除的环境，不会开启接口文档
	auth:
		enable: 开启认证模式
		username: 用户名
		password: 密码
```



## 🤝 贡献指南  

欢迎提交Issue/Pull Request

---

**License**: MIT  

---

