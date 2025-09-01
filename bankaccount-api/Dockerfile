FROM bellsoft/liberica-openjdk-centos:21
ARG BUILD_VERSION="1.0.0"
ARG MAINTAINER_NAME
LABEL image.name="bankaccount-api"
LABEL image.version="${BUILD_VERSION}"
LABEL maintainer="${MAINTAINER_NAME}"
COPY target/bankaccount-api-*.jar bankaccount-api.jar
ENV USER_MEMORY="-Xms256m -Xmx512m"
EXPOSE 8181
ENTRYPOINT ["sh","-C","java","${USER_MEMORY}","-jar","/bankaccount-api.jar"]