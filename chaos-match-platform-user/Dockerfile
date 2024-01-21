# 以jdk8为基础镜像
FROM openjdk:8
# 描述
LABEL description="match-platform-user"
# 暴露接口
EXPOSE 8073
# 将主机中的jar包添加到镜像中
ADD target/chaos-match-platform-user.jar .
# 运行jar包
ENTRYPOINT ["java", "-jar","chaos-match-platform-user.jar"]