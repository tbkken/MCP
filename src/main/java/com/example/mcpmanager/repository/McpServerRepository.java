package com.example.mcpmanager.repository;

import com.example.mcpmanager.entity.McpServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface McpServerRepository extends JpaRepository<McpServer, Long> {
    Optional<McpServer> findByName(String name);
    boolean existsByName(String name);
}
