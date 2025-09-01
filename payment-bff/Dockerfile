FROM bellsoft/liberica-openjdk-centos:21
ARG BUILD_VERSION="1.0.0"
ARG MAINTAINER_NAME
LABEL image.name="payment-bff"
LABEL image.version="${BUILD_VERSION}"
LABEL maintainer="${MAINTAINER_NAME}"
COPY target/payment-bff-*.jar payment-bff.jar
ENV USER_MEMORY="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["sh","-c","java ${USER_MEMORY} -jar /payment-bff.jar"]