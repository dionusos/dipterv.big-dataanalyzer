# docker stop big; docker rm big; docker rmi big
# docker build -t big .
# docker run --name big -p 3001:3000 -p 8081:8080 -td big sh /app/start.sh
# docker exec -it big bash
FROM big_base
WORKDIR /app

ADD . /app

RUN tar -zxf big-gui-0.1.0.tgz
RUN cd /app/package && npm install
RUN mkdir log

RUN echo "java -jar -Ddatasources.path=datasources backend-1.0-SNAPSHOT.jar 1>/app/log/out.log 2>/app/log/err.log & \n" > start.sh
RUN echo "cd package && npm start 1>/app/log/gui_out.log 2>/app/loggui_err.log & \n" >> start.sh
RUN chmod +x start.sh
