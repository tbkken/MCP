package com.example.mcpmanager.service;

import com.example.mcpmanager.entity.McpServer;
import com.example.mcpmanager.entity.McpTool;
import com.example.mcpmanager.repository.McpServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema.ClientCapabilities;

@Service
public class McpServerService {

    @Autowired
    private McpServerRepository mcpServerRepository;

    public List<McpServer> getAllMcpServers() {
        return mcpServerRepository.findAll();
    }

    public Optional<McpServer> getMcpServerById(Long id) {
        return mcpServerRepository.findById(id);
    }

    public McpServer saveMcpServer(McpServer mcpServer) {
        return mcpServerRepository.save(mcpServer);
    }

    public void deleteMcpServer(Long id) {
        mcpServerRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return mcpServerRepository.existsByName(name);
    }

    public Optional<McpServer> getMcpServerByName(String name) {
        return mcpServerRepository.findByName(name);
    }

    public List<McpTool> getMcpTools(Long serverId) throws Exception {

        Optional<McpServer> optionalServer = mcpServerRepository.findById(serverId);
        String baseUrl = optionalServer.get().getUrl();
        String sseEndpoint = "/sse/";
        HttpClientSseClientTransport httpClientSseClientTransport = HttpClientSseClientTransport.builder(baseUrl).sseEndpoint(sseEndpoint).build();
        McpSyncClient client = McpClient.sync(httpClientSseClientTransport).requestTimeout(Duration.ofSeconds(20L))
                    .capabilities(ClientCapabilities.builder()
                                          .roots(true)
                                          .sampling()
                                          .build())
                    .build();
        // Initialize connection
        client.initialize();
        client.ping();
        // List available tools
        io.modelcontextprotocol.spec.McpSchema.ListToolsResult listTools = client.listTools();

        return listTools.tools()
            .stream()
            .map(tool -> new McpTool(tool.name(), tool.description(), tool.inputSchema(), tool.outputSchema()))
            .toList();
    }
    
}
