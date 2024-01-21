# 以jdk8为基础镜像
FROM openjdk:8
# 描述
LABEL description="match-platform-gateway"
# 暴露接口
EXPOSE 10010
# 将主机中的jar包添加到镜像中
ADD target/chaos-match-platform-gateway.jar .
# 运行jar包
ENTRYPOINT ["java", "-jar","chaos-match-platform-gateway.jar"]