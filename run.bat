@echo off
title MCP Manager

echo 正在检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java运行环境，请先安装Java 11或更高版本
    echo 解决方案:
    echo 1. 访问 https://www.oracle.com/java/technologies/downloads/ 下载并安装Java
    echo 2. 或访问 https://openjdk.org/ 获取OpenJDK
    echo 3. 安装后请确保JAVA_HOME环境变量已正确配置
    pause
    exit /b 1
)

echo 正在检查Maven环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven，请先安装Maven 3.6或更高版本
    echo 解决方案:
    echo 1. 访问 https://maven.apache.org/download.cgi 下载Maven
    echo 2. 解压后请确保MAVEN_HOME环境变量已正确配置
    echo 3. 将Maven的bin目录添加到PATH环境变量中
    pause
    exit /b 1
)

echo 正在构建项目...
mvn clean package

if %errorlevel% equ 0 (
    echo 项目构建成功!
    echo 正在启动应用...
    echo 应用将在 http://localhost:8080 上运行
    java -jar target/mcp-manager-0.0.1-SNAPSHOT.jar
) else (
    echo 项目构建失败，请检查错误信息
    echo 常见问题及解决方案:
    echo 1. 如果遇到数据库连接问题，请检查README.md中的故障排除部分
    echo 2. 确保pom.xml中已添加sqlite-dialect依赖
    echo 3. 确保application.properties中使用了正确的数据库方言配置
    pause
    exit /b 1
)
