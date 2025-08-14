package com.example.mcpmanager.entity;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "mcp_servers")
public class McpServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String url;

    @Column
    private Integer timeout;

    @ElementCollection
    @CollectionTable(name = "mcp_server_headers", joinColumns = @JoinColumn(name = "server_id"))
    @MapKeyColumn(name = "header_key")
    @Column(name = "header_value")
    private Map<String, String> headers;

    // Constructors
    public McpServer() {}

    public McpServer(String name, String type, String url, Integer timeout, Map<String, String> headers) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.timeout = timeout;
        this.headers = headers;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
