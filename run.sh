#!/bin/bash

# MCP Manager 启动脚本

echo "正在检查Java环境..."
if ! command -v java &> /dev/null
then
    echo "错误: 未找到Java运行环境，请先安装Java 11或更高版本"
    echo "解决方案:"
    echo "1. 访问 https://www.oracle.com/java/technologies/downloads/ 下载并安装Java"
    echo "2. 或访问 https://openjdk.org/ 获取OpenJDK"
    echo "3. 安装后请确保JAVA_HOME环境变量已正确配置"
    exit 1
fi

export M2_HOME="/Users/fangnan/Desktop/soft/apache-maven-3.9.11"
PATH="${M2_HOME}/bin:${PATH}"
export PATH

echo "正在检查Maven环境..."
if ! command -v mvn &> /dev/null
then
    echo "错误: 未找到Maven，请先安装Maven 3.6或更高版本"
    echo "解决方案:"
    echo "1. 访问 https://maven.apache.org/download.cgi 下载Maven"
    echo "2. 解压后请确保MAVEN_HOME环境变量已正确配置"
    echo "3. 将Maven的bin目录添加到PATH环境变量中"
    exit 1
fi

echo "正在构建项目..."
mvn clean package

if [ $? -eq 0 ]; then
    echo "项目构建成功!"
    echo "正在启动应用..."
    echo "应用将在 http://localhost:8080 上运行"
    java -jar target/mcp-manager-0.0.1-SNAPSHOT.jar
else
    echo "项目构建失败，请检查错误信息"
    echo "常见问题及解决方案:"
    echo "1. 如果遇到数据库连接问题，请检查README.md中的故障排除部分"
    echo "2. 确保pom.xml中已添加sqlite-dialect依赖"
    echo "3. 确保application.properties中使用了正确的数据库方言配置"
    exit 1
fi
