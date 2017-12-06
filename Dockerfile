# docker build --tag web-app-bin .
FROM dmytroorlov/jdk

EXPOSE 9000

HEALTHCHECK --interval=5s --timeout=2s --retries=12 \
  CMD curl --silent --fail http://localhost:9000/health || exit 1

WORKDIR /opt

RUN touch /opt/production.conf
COPY target/universal/stage /opt/web

CMD ["/opt/web/bin/web-app-seed", "-Dpidfile.path=/dev/null", "-Dconfig.file=/opt/production.conf"]
