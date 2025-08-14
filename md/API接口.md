# MCP服务管理器API接口文档

## 概述
本项目提供RESTful API接口用于管理MCP服务和获取工具信息。所有API接口都以`/api/mcp-servers`为前缀。

## 基础URL
```
http://localhost:8080/api/mcp-servers
```

## 错误处理
所有API接口遵循标准HTTP状态码：
- 200: 请求成功
- 201: 创建成功
- 400: 请求参数错误
- 404: 资源未找到
- 409: 资源冲突（如重复的服务名称）
- 500: 服务器内部错误

## API接口详情

### 1. 获取所有MCP服务
**接口**: `GET /api/mcp-servers`  
**描述**: 获取系统中所有已配置的MCP服务列表  
**请求参数**: 无  
**响应格式**: `application/json`  
**响应内容**: McpServer对象数组  
**成功响应示例**:
```json
[
  {
    "id": 1,
    "name": "user info system",
    "type": "sse",
    "url": "http://127.0.0.1:10002/sse/",
    "timeout": 60,
    "headers": {
      "Authorization": "Bearer 54bfe0d6-803f-4f60-9bed-0d7709731011"
    }
  }
]
```

### 2. 根据ID获取MCP服务
**接口**: `GET /api/mcp-servers/{id}`  
**描述**: 根据服务ID获取特定MCP服务的详细信息  
**请求参数**: 
- Path参数: `id` (Long) - MCP服务ID  
**响应格式**: `application/json`  
**响应内容**: McpServer对象  
**成功响应示例**:
```json
{
  "id": 1,
  "name": "user info system",
  "type": "sse",
  "url": "http://127.0.0.1:10002/sse/",
  "timeout": 60,
  "headers": {
    "Authorization": "Bearer 54bfe0d6-803f-4f60-9bed-0d7709731011"
  }
}
```
**错误响应**:
- 404: 服务不存在

### 3. 创建或更新MCP服务
**接口**: `POST /api/mcp-servers`  
**描述**: 创建新的MCP服务或更新现有服务  
**请求参数**: 
- Body参数: McpServer对象（JSON格式）  
**请求体示例**:
```json
{
  "name": "user info system",
  "type": "sse",
  "url": "http://127.0.0.1:10002/sse/",
  "timeout": 60,
  "headers": {
    "Authorization": "Bearer 54bfe0d6-803f-4f60-9bed-0d7709731011"
  }
}
```
**响应格式**: `application/json`  
**响应内容**: 创建或更新后的McpServer对象  
**成功响应状态码**: 201 (Created)  
**错误响应**:
- 400: 请求参数错误
- 409: 服务名称已存在

### 4. 删除MCP服务
**接口**: `DELETE /api/mcp-servers/{id}`  
**描述**: 根据服务ID删除MCP服务  
**请求参数**: 
- Path参数: `id` (Long) - MCP服务ID  
**响应格式**: 无内容  
**成功响应状态码**: 204 (No Content)  
**错误响应**:
- 404: 服务不存在

### 5. 测试MCP服务连接
**接口**: `POST /api/mcp-servers/test`  
**描述**: 测试MCP服务连接的有效性  
**请求参数**: 
- Body参数: McpServer对象（JSON格式，用于测试配置）  
**请求体示例**:
```json
{
  "name": "test service",
  "type": "sse",
  "url": "http://127.0.0.1:10002/sse/",
  "timeout": 60,
  "headers": {
    "Authorization": "Bearer 54bfe0d6-803f-4f60-9bed-0d7709731011"
  }
}
```
**响应格式**: `text/plain`  
**成功响应示例**: "Connection successful"  
**错误响应**:
- 500: 连接失败，返回错误信息

### 6. 获取MCP服务的工具列表
**接口**: `GET /api/mcp-servers/{id}/tools`  
**描述**: 根据服务ID获取该MCP服务提供的工具列表  
**请求参数**: 
- Path参数: `id` (Long) - MCP服务ID  
**响应格式**: `application/json`  
**响应内容**: McpTool对象数组  
**成功响应示例**:
```json
[
  {
    "name": "get_user_info",
    "description": "获取用户信息",
    "inputSchema": {
      "type": "object",
      "properties": {
        "userId": {
          "type": "string",
          "description": "用户ID"
        }
      },
      "required": ["userId"]
    },
    "outputSchema": {
      "type": "object",
      "properties": {
        "name": {
          "type": "string"
        },
        "email": {
          "type": "string"
        }
      }
    }
  }
]
```
**工具展示规范**:
- 前端工具列表弹窗应展示为四列表格：工具名称、描述、输入参数、返回值
- 描述字段仅展示 **Responses:** 之前的内容
- 每个工具行应包含"查看详情"操作按钮，点击后展示工具的完整描述信息
**错误响应**:
- 404: 服务不存在
- 500: 获取工具列表失败

## 数据模型定义

### McpServer对象
```json
{
  "id": 1,
  "name": "service name",
  "type": "sse|http",
  "url": "http://example.com/sse/",
  "timeout": 60,
  "headers": {
    "Header-Key": "Header-Value"
  }
}
```

### McpTool对象
```json
{
  "name": "tool name",
  "description": "tool description",
  "inputSchema": {},
  "outputSchema": {}
}
```

## CORS配置
接口支持跨域请求，允许所有来源访问（`@CrossOrigin(origins = "*")`）。
