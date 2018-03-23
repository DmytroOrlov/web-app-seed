lazy val `web-app-seed` = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(IntegrationTest)
  .settings(
    inThisBuild(Seq(
      version := "1.0-SNAPSHOT",
      organization := "com.github.dmytroorlov",
      scalaVersion := "2.12.5"
    ))
  )
  .settings(
    Defaults.itSettings ++ Seq(
      libraryDependencies ++= Seq(
        guice,
        "io.monix" %% "monix" % "3.0.0-RC1",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
          "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test,it"
      )
    )
  )
