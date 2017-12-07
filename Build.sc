#!/usr/bin/env amm
import ammonite.ops._
import ammonite.ops.ImplicitWd._

%sbt('stage)
%docker('build, "--tag", "web-app", ".")
