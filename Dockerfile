# docker build --tag web-app .
FROM dmytroorlov/jdk

HEALTHCHECK --interval=10s --timeout=1s --retries=3 \
  CMD curl --silent --fail http://localhost:9000/health

WORKDIR /opt

COPY production.conf /opt/production.conf
COPY target/universal/stage /opt/web-app

EXPOSE 9000

CMD ["/opt/web-app/bin/web-app-seed", "-Dconfig.file=/opt/production.conf", "-Dpidfile.path=/dev/null"]
