FROM websphere-liberty:webProfile8
RUN mkdir /config/libs
COPY blog-ear/target/blog-ear.ear /config/dropins/blog-ear.ear
COPY transaction-test/com.ibm.ws.Transaction.test.impl.XAFlowCallbackImpl_1.0.jar /liberty/wlp/lib
COPY transaction-test/xaflow-1.0.mf /liberty/wlp/lib/features
COPY server-for-docker.xml /config/server.xml
COPY mysql-lib/mysql-connector-java-8.0.12.jar /config/libs
ENV LICENSE accept