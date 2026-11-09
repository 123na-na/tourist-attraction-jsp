# 旅游管理系统（Tourist Attraction）

Java Web 旅游景点 + 特产商城管理系统，采用 **Servlet + JSP + JDBC + MySQL** 经典分层架构（Eclipse WTP 工程）。

## 功能

- **景点展示**：旅游景点（`Scenic_spot`）信息浏览
- **特产商城**：商品（`Goods`）按分类（`Category` / `Type`）浏览、商品详情
- **购物车**：加入购物车、数量修改、删除、结算列表（`CartItem`）
- **用户体系**：注册、登录、验证码（`User` + 验证码 Servlet）、个人中心
- **图片管理**：商品图片上传与展示（`servlet_image` + commons-fileupload）
- **JSP 页面**：JSTL 标签渲染商品列表与详情页

## 技术栈

| 类别 | 技术 |
| --- | --- |
| Web | Java Servlet、JSP + JSTL |
| 数据库 | MySQL 8（mysql-connector-java 8.0.26）、JDBC |
| 容器 | Apache Tomcat |
| IDE/工程 | Eclipse WTP（`.classpath` / `.project`） |
| 依赖 | WEB-INF/lib 内置 jar（mysql-connector、commons-fileupload、JSTL 等） |

## 目录结构

```
tourist_attraction1/
└── src/main/
    ├── java/
    │   ├── bean/          # User / Goods / Category / Type / CartItem / Scenic_spot
    │   ├── dao/           # Db.java（连接）及各实体 DAO
    │   ├── service/       # 业务层
    │   └── servlet_*/     # 按模块划分的 Servlet（用户/商品/分类/购物车/图片/验证码）
    └── webapp/            # JSP 页面、WEB-INF/lib
```

## 启动方式

1. **数据库**：MySQL 中创建库 `tourism_system`（连接信息在 `src/main/java/dao/Db.java`：默认端口 `3316`、用户 `root`，**密码请修改为本地实际密码**）；
   > 说明：本项目当前未附带 `schema.sql` 初始化脚本，表结构以各 DAO 中使用的建表/字段为准，建议后续补充建库脚本以便一键启动。
2. **导入工程**：Eclipse（含 WTP）→ Import → Existing Projects into Workspace，选择 `tourist_attraction1`；
3. **部署运行**：配置 Tomcat 后运行，浏览器访问项目根路径（欢迎页/登录页）。

## 安全提醒

`dao/Db.java` 中硬编码了数据库连接密码且已进入公开仓库，请及时修改数据库密码并替换代码中的配置，避免泄露风险。

## 数据库配置（不写入源码）

`dao/Db.java` 不再硬编码连接信息，改为读取环境变量：

| 变量 | 说明 | 默认值 |
| --- | --- | --- |
| `TOURISM_DB_URL` | JDBC 连接串 | `jdbc:mysql://localhost:3316/tourism_system?...` |
| `TOURISM_DB_USER` | 数据库用户名 | `root` |
| `TOURISM_DB_PASSWORD` | 数据库密码 | 空（必须自行配置） |

Tomcat 启动前设置，例如（Windows）：

```
set TOURISM_DB_PASSWORD=你的密码
```
