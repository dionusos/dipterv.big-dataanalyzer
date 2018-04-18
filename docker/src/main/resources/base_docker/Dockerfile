# docker stop big; docker rm big; docker rmi big
# docker build -t big_base .
# docker run --name big -p 3001:3000 -p 8081:8080 -td big
# docker exec -it big bash
FROM ubuntu:18.04

RUN apt-get update && apt-get install -y curl sudo less
RUN apt-get install -y openjdk-8-jdk nodejs npm
