FROM bellsoft/liberica-openjdk-centos:21
ARG BUILD_VERSION="1.0.0"
ARG MAINTAINER_NAME
LABEL image.name="payment-api"
LABEL image.version="${BUILD_VERSION}"
LABEL maintainer="${MAINTAINER_NAME}"
COPY target/payment-api-*.jar payment-api.jar
ENV USER_MEMORY="-Xms256m -Xmx512m"
EXPOSE 8282
ENTRYPOINT ["sh","-C","java","${USER_MEMORY}","-jar","/payment-api.jar"]