#!/usr/bin/env amm
import ammonite.ops._
import ammonite.ops.ImplicitWd._

%minikube('start)
%helm('init)

%sbt('stage)
val d = %%('minikube, "docker-env").out.lines.filter(s => !s.startsWith("#")).flatMap(_.split("export ").filter(_.nonEmpty)).map(_.split("=")).collect { case Array(k, v) => k -> v.split("\"").filter(_.nonEmpty).head }.toMap
%docker('build, "--tag", "web-app", ".",
  DOCKER_TLS_VERIFY=d("DOCKER_TLS_VERIFY"),
  DOCKER_HOST=d("DOCKER_HOST"),
  DOCKER_CERT_PATH=d("DOCKER_CERT_PATH"),
  DOCKER_API_VERSION=d("DOCKER_API_VERSION")
)
%sbt("it:test")
