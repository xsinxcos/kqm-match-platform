# 以jdk8为基础镜像
FROM ibm-semeru-runtimes:open-8-jre
# 描述
LABEL description="match-platform-auth"
# 暴露接口
EXPOSE 8071
# 将主机中的jar包添加到镜像中
ADD target/chaos-match-platform-auth.jar .
# 运行jar包
ENTRYPOINT ["java", "-jar","chaos-match-platform-auth.jar"]