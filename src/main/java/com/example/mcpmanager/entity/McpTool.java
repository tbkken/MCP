package com.example.mcpmanager.entity;

import java.util.Map;

import io.modelcontextprotocol.spec.McpSchema.JsonSchema;

public class McpTool {
    private String name;
    private String description;
    private JsonSchema inputSchema;
    private Map<String, Object> outputSchema;

    public McpTool() {}

    public McpTool(String name, String description, JsonSchema inputSchema, Map<String, Object> outputSchema) {
        this.name = name;
        this.description = description;
        this.inputSchema = inputSchema;
        this.outputSchema = outputSchema;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JsonSchema getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(JsonSchema inputSchema) {
        this.inputSchema = inputSchema;
    }

    public Map<String, Object> getOutputSchema() {
        return outputSchema;
    }

    public void setOutputSchema(Map<String, Object> outputSchema) {
        this.outputSchema = outputSchema;
    }
}
