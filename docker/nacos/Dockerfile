# 基础镜像
FROM nacos/nacos-server:v2.0.4-slim
# author
MAINTAINER XSINXCOS

EXPOSE 8848
# 挂载目录
VOLUME /home/order/nacos
# 创建目录
RUN mkdir -p /home/order/nacos
# 指定路径
WORKDIR /home/order/nacos
# 复制conf文件到路径
COPY ./conf/nacos.conf /home/order/nacos/nacos.conf

