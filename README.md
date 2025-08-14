# MCP服务管理器

MCP服务管理器是一个用于管理和查看MCP（Model Context Protocol）服务工具的Web应用程序。它支持通过HTTP和SSE方式连接到MCP服务器，并获取和展示服务器提供的工具信息。

## 功能特性

- 添加和管理MCP服务
- 支持HTTP和SSE连接方式
- 查看MCP服务提供的工具列表
- 显示工具的详细信息，包括：
  - 工具名称
  - 工具描述
  - 输入参数模式（inputSchema）
  - 输出结果模式（outputSchema）
- 测试MCP服务连接

## 技术栈

- 后端：Spring Boot + JPA + SQLite
- 前端：Vue 3 + Element Plus
- 构建工具：Maven

## 快速开始

### 运行应用

1. 确保已安装Java 11+和Maven
2. 在项目根目录下运行：
   ```bash
   ./run.sh
   ```
   或在Windows上运行：
   ```cmd
   run.bat
   ```
3. 应用启动后，访问 http://localhost:8080

### 使用说明

1. 添加MCP服务：
   - 点击"新增MCP服务"按钮
   - 选择服务类型（HTTP或SSE）
   - 输入服务URL
   - 可选：设置超时时间和请求头
   - 点击"测试连接"验证配置
   - 点击"确定"保存服务

2. 查看工具信息：
   - 在服务卡片上点击"查看工具"按钮
   - 查看工具列表，包括名称和描述
   - 点击"查看详情"查看工具的完整信息，包括输入参数和输出模式

## 详细文档

为了更好地理解和使用本项目，我们提供了以下详细的文档：

### 📋 用户文档
- [**用户手册.md**](md/用户手册.md) - 面向最终用户的详细使用指南，包含安装、配置、操作步骤和故障排除

### 🏗️ 开发文档
- [**项目概述.md**](md/项目概述.md) - 项目整体架构和技术栈介绍
- [**数据模型.md**](md/数据模型.md) - 数据库设计和实体类说明
- [**API接口.md**](md/API接口.md) - 后端RESTful API详细说明
- [**业务逻辑.md**](md/业务逻辑.md) - 核心业务逻辑和MCP协议处理
- [**前端界面.md**](md/前端界面.md) - 前端界面设计和交互逻辑
- [**MCP协议规范.md**](md/MCP协议规范.md) - MCP协议实现规范和兼容性说明

### ⚙️ 运维文档
- [**部署运维.md**](md/部署运维.md) - 部署方式、配置管理和故障排除
- [**开发指南.md**](md/开发指南.md) - 开发环境搭建、代码规范和扩展开发指南

## API接口

### MCP服务管理

- `GET /api/mcp-servers` - 获取所有MCP服务
- `GET /api/mcp-servers/{id}` - 根据ID获取MCP服务
- `POST /api/mcp-servers` - 创建或更新MCP服务
- `DELETE /api/mcp-servers/{id}` - 删除MCP服务
- `POST /api/mcp-servers/test` - 测试MCP服务连接

### 工具信息获取

- `GET /api/mcp-servers/{id}/tools` - 获取MCP服务的工具列表

## MCP协议支持

本应用支持MCP协议的工具发现功能，能够解析以下格式的工具信息：

1. 标准格式：`{ "tools": [...] }`
2. 包装在result对象中：`{ "result": { "tools": [...] } }`
3. 根数组格式：`[...]`
4. 包装在data对象中：`{ "data": { "tools": [...] } }`

每个工具包含以下信息：
- `name`：工具名称
- `description`：工具描述
- `inputSchema`：输入参数模式（JSON Schema格式）
- `outputSchema`：输出结果模式（JSON Schema格式）

## 项目结构

```
src/
├── main/
│   ├── java/com/example/mcpmanager/
│   │   ├── controller/     # 控制器层
│   │   ├── entity/         # 实体类
│   │   ├── repository/     # 数据访问层
│   │   ├── service/        # 服务层
│   │   └── util/           # 工具类
│   └── resources/
│       ├── static/         # 静态资源（前端页面）
│       └── application.properties  # 配置文件
```

## 构建和运行

```bash
# 清理并编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run

# 打包应用
mvn package
```

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。

## 许可证

MIT License
