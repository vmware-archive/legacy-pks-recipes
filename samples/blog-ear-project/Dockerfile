FROM websphere-liberty:webProfile8
RUN mkdir /config/libs
COPY blog-ear/target/blog-ear.ear /config/dropins/blog-ear.ear
COPY server-for-docker.xml /config/server.xml
COPY mysql-lib/mysql-connector-java-8.0.12.jar /config/libs
ENV LICENSE accept