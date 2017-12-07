lazy val `web-app-seed` = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    inThisBuild(Seq(
      version := "1.0-SNAPSHOT",
      organization := "com.github.dmytroorlov",
      scalaVersion := "2.12.4",
      libraryDependencies ++= Seq(
        guice,
        "io.monix" %% "monix" % "2.3.0",
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
      )
    ))
  )
