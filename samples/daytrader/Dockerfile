FROM websphere-liberty:javaee8
RUN mkdir /config/libs
COPY daytrader-ee7/target/daytrader-ee7-1.0-SNAPSHOT.ear /config/dropins/daytrader-ee7.ear
COPY server-for-helm.xml /config/server.xml
COPY daytrader-ee7-wlpcfg/shared/resources/Daytrader7SampleDerbyLibs/derby-10.10.1.1.jar /config/libs
RUN installUtility install --acceptLicense defaultServer
ENV LICENSE accept