# docker build --tag web-app .
FROM dmytroorlov/jdk

EXPOSE 9000

HEALTHCHECK --interval=10s --timeout=1s --retries=3 \
  CMD curl --silent --fail http://localhost:9000/health || exit 1

WORKDIR /opt

RUN touch /opt/production.conf
COPY target/universal/stage /opt/web

CMD ["/opt/web/bin/web-app-seed", "-Dpidfile.path=/dev/null", "-Dconfig.file=/opt/production.conf"]
