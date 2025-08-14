package com.example.mcpmanager.controller;

import com.example.mcpmanager.entity.McpServer;
import com.example.mcpmanager.entity.McpTool;
import com.example.mcpmanager.service.McpServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mcp-servers")
@CrossOrigin(origins = "*")
public class McpServerController {

    @Autowired
    private McpServerService mcpServerService;

    // 获取所有MCP服务
    @GetMapping
    public List<McpServer> getAllMcpServers() {
        return mcpServerService.getAllMcpServers();
    }

    // 根据ID获取MCP服务
    @GetMapping("/{id}")
    public ResponseEntity<McpServer> getMcpServerById(@PathVariable Long id) {
        Optional<McpServer> mcpServer = mcpServerService.getMcpServerById(id);
        if (mcpServer.isPresent()) {
            return new ResponseEntity<>(mcpServer.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 创建或更新MCP服务
    @PostMapping
    public ResponseEntity<McpServer> createMcpServer(@RequestBody McpServer mcpServer) {
        // 检查是否已存在同名服务
        if (mcpServer.getId() == null && mcpServerService.existsByName(mcpServer.getName())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        McpServer savedMcpServer = mcpServerService.saveMcpServer(mcpServer);
        return new ResponseEntity<>(savedMcpServer, HttpStatus.CREATED);
    }

    // 删除MCP服务
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMcpServer(@PathVariable Long id) {
        mcpServerService.deleteMcpServer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 测试MCP服务连接
    @PostMapping("/test")
    public ResponseEntity<String> testMcpServerConnection(@RequestBody McpServer mcpServer) {
        // 这里应该实现实际的连接测试逻辑
        // 目前只是模拟测试
        try {
            // 模拟测试逻辑
            // 实际应用中，这里应该尝试连接到MCP服务并验证其可用性
            Thread.sleep(1000); // 模拟网络延迟
            return new ResponseEntity<>("Connection successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Connection failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 获取MCP服务的工具列表
    @GetMapping("/{id}/tools")
    public ResponseEntity<?> getMcpServerTools(@PathVariable Long id) {
        try {
            List<McpTool> tools = mcpServerService.getMcpTools(id);
            return new ResponseEntity<>(tools, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to get tools: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
