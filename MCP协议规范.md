# MCP协议规范文档

## 概述
本文档详细说明MCP（Model Context Protocol）协议在本项目中的实现规范、支持格式和扩展功能。

## MCP协议基础

### 协议定义
MCP（Model Context Protocol）是一种标准化协议，用于客户端与AI模型服务之间的交互。它定义了如何发现、调用和管理AI模型提供的工具和服务。

### 核心概念
- **工具（Tool）**: AI模型提供的可调用功能
- **工具发现**: 客户端发现服务提供哪些工具的机制
- **传输方式**: 客户端与服务端通信的方式（HTTP、SSE、WebSocket等）

## 协议实现

### 当前支持的传输方式
目前项目主要支持SSE（Server-Sent Events）传输方式：

#### SSE连接配置
```java
String baseUrl = optionalServer.get().getUrl();
String sseEndpoint = "/sse/";
HttpClientSseClientTransport httpClientSseClientTransport = 
    HttpClientSseClientTransport.builder(baseUrl)
        .sseEndpoint(sseEndpoint)
        .build();
```

#### 客户端能力声明
```java
McpSyncClient client = McpClient.sync(httpClientSseClientTransport)
    .requestTimeout(Duration.ofSeconds(20L))
    .capabilities(ClientCapabilities.builder()
        .roots(true)
        .sampling()
        .build())
    .build();
```

### 工具发现机制

#### 标准工具列表请求
```java
// 初始化连接
client.initialize();
client.ping(); // 测试连接

// 获取工具列表
io.modelcontextprotocol.spec.McpSchema.ListToolsResult listTools = client.listTools();
```

#### 工具信息结构
每个工具包含以下标准字段：
- **name** (String): 工具名称，唯一标识符
- **description** (String): 工具描述，说明工具功能
- **inputSchema** (JsonSchema): 输入参数的JSON Schema定义
- **outputSchema** (Map<String, Object>): 输出结果的结构定义

### 支持的工具格式

#### 1. 标准格式
```json
{
  "tools": [
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
          "name": {"type": "string"},
          "email": {"type": "string"}
        }
      }
    }
  ]
}
```

#### 2. 包装在result对象中
```json
{
  "result": {
    "tools": [
      {
        "name": "get_user_info",
        "description": "获取用户信息",
        "inputSchema": {},
        "outputSchema": {}
      }
    ]
  }
}
```

#### 3. 根数组格式
```json
[
  {
    "name": "get_user_info",
    "description": "获取用户信息",
    "inputSchema": {},
    "outputSchema": {}
  }
]
```

#### 4. 包装在data对象中
```json
{
  "data": {
    "tools": [
      {
        "name": "get_user_info",
        "description": "获取用户信息",
        "inputSchema": {},
        "outputSchema": {}
      }
    ]
  }
}
```

## JSON Schema支持

### 输入参数模式
工具的`inputSchema`字段遵循JSON Schema规范：

#### 基本类型
```json
{
  "type": "object",
  "properties": {
    "name": {"type": "string"},
    "age": {"type": "integer"},
    "active": {"type": "boolean"},
    "score": {"type": "number"}
  }
}
```

#### 复杂结构
```json
{
  "type": "object",
  "properties": {
    "user": {
      "type": "object",
      "properties": {
        "name": {"type": "string"},
        "contacts": {
          "type": "array",
          "items": {"type": "string"}
        }
      }
    }
  }
}
```

#### 验证约束
```json
{
  "type": "object",
  "properties": {
    "email": {
      "type": "string",
      "format": "email"
    },
    "age": {
      "type": "integer",
      "minimum": 0,
      "maximum": 150
    }
  },
  "required": ["email"]
}
```

### 输出模式
工具的`outputSchema`字段定义返回值结构：

#### 简单返回值
```json
{
  "type": "object",
  "properties": {
    "result": {"type": "string"},
    "success": {"type": "boolean"}
  }
}
```

#### 复杂返回值
```json
{
  "type": "object",
  "properties": {
    "data": {
      "type": "object",
      "properties": {
        "users": {
          "type": "array",
          "items": {
            "type": "object",
            "properties": {
              "id": {"type": "string"},
              "name": {"type": "string"}
            }
          }
        }
      }
    },
    "pagination": {
      "type": "object",
      "properties": {
        "page": {"type": "integer"},
        "total": {"type": "integer"}
      }
    }
  }
}
```

## 协议扩展

### 客户端能力声明
当前客户端声明以下能力：
- **roots**: 支持根目录操作
- **sampling**: 支持采样功能

#### 能力声明代码
```java
.capabilities(ClientCapabilities.builder()
    .roots(true)
    .sampling()
    .build())
```

### 超时配置
```java
.requestTimeout(Duration.ofSeconds(20L))
```

### 连接测试
```java
client.initialize();
client.ping(); // 测试连接有效性
```

## 错误处理

### 协议级错误
MCP协议定义的标准错误类型：
- **ParseError**: 解析错误
- **InvalidRequest**: 无效请求
- **MethodNotFound**: 方法未找到
- **InvalidParams**: 参数无效
- **InternalError**: 内部错误

### 实现错误处理
```java
try {
    List<McpTool> tools = mcpServerService.getMcpTools(id);
    return new ResponseEntity<>(tools, HttpStatus.OK);
} catch (Exception e) {
    return new ResponseEntity<>("Failed to get tools: " + e.getMessage(), 
                              HttpStatus.INTERNAL_SERVER_ERROR);
}
```

## 兼容性考虑

### 版本兼容性
- 使用`io.modelcontextprotocol.sdk`库版本0.11.2
- 向下兼容标准MCP协议格式
- 支持多种响应格式解析

### 扩展兼容性
- 易于添加新的传输方式（HTTP、WebSocket等）
- 支持自定义工具格式
- 可扩展的客户端能力声明

## 性能优化

### 连接管理
```java
// 当前实现：每次请求创建新连接
// 优化建议：实现连接池复用
```

### 工具列表缓存
```java
// 当前实现：每次获取都请求远程服务
// 优化建议：添加缓存机制
@Cacheable(value = "tools", key = "#serverId")
public List<McpTool> getMcpTools(Long serverId) throws Exception {
    // 实现...
}
```

## 安全考虑

### 传输安全
- **HTTPS支持**: 通过URL配置支持HTTPS
- **请求头安全**: 支持自定义认证头
- **超时控制**: 防止恶意长连接

### 数据安全
- **Schema验证**: 输入输出都经过JSON Schema验证
- **数据转换**: 安全的数据类型转换
- **异常处理**: 完善的异常捕获和处理

## 未来扩展

### 计划支持的功能
1. **WebSocket传输**: 添加WebSocket协议支持
2. **HTTP传输**: 添加标准HTTP协议支持
3. **工具调用**: 实现工具调用功能
4. **会话管理**: 添加会话状态管理
5. **批量操作**: 支持批量工具调用

### 协议扩展点
1. **新的工具格式**: 支持更多响应格式
2. **扩展能力**: 添加更多客户端能力
3. **认证机制**: 添加标准认证协议支持
4. **流式响应**: 支持流式数据传输

## 测试验证

### 协议兼容性测试
```java
// 测试不同格式的工具列表响应
@Test
void testStandardFormat() {
    // 测试标准格式 { "tools": [...] }
}

@Test
void testResultWrapperFormat() {
    // 测试 { "result": { "tools": [...] } } 格式
}

@Test
void testArrayFormat() {
    // 测试根数组格式 [ ... ]
}
```

### 边界条件测试
```java
// 测试空工具列表
@Test
void testEmptyToolsList() {
    // 验证空数组处理
}

// 测试超大工具列表
@Test
void testLargeToolsList() {
    // 验证性能和内存使用
}
```

## 调试和监控

### 协议级调试
```java
// 启用详细日志
logging.level.io.modelcontextprotocol=DEBUG
```

### 连接监控
```java
// 监控连接状态和性能指标
// 记录请求响应时间
// 统计成功/失败率
```

## 最佳实践

### 工具设计建议
1. **清晰的命名**: 工具名称应简洁明确
2. **完整的描述**: 提供详细的工具功能说明
3. **严格的Schema**: 定义准确的输入输出格式
4. **合理的超时**: 设置适当的请求超时时间

### 客户端实现建议
1. **连接复用**: 尽可能复用连接以提高性能
2. **错误重试**: 实现合理的错误重试机制
3. **超时控制**: 设置适当的超时时间
4. **日志记录**: 记录关键操作和错误信息

### 服务端实现建议
1. **格式兼容**: 支持多种响应格式
2. **性能优化**: 优化工具列表的生成和传输
3. **错误处理**: 提供清晰的错误信息
4. **安全控制**: 实现适当的认证和授权机制
