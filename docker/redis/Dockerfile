# 基础镜像
FROM redis:5.0.8
# author
MAINTAINER XSINXCOS

EXPOSE 6379
# 挂载目录
VOLUME /home/order/redis
# 创建目录
RUN mkdir -p /home/order/redis
# 指定路径
WORKDIR /home/order/redis
# 复制conf文件到路径
COPY ./conf/redis.conf /home/order/redis/redis.conf

