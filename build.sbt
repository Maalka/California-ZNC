import WebKeys._

// TODO Replace with your project's/module's name
name := "california_znc"

// TODO Set your organization here; ThisBuild means it will apply to all sub-modules
organization in ThisBuild := "com.maalka"

// TODO Set your version here
version := "1.1.5.0"

scalaVersion in ThisBuild := "2.11.6"

maintainer in Linux := "Clay Teeter <clay.teeter@maalka.com>"
maintainer in Docker := "Clay Teeter <clay.teeter@maalka.com>"

packageSummary in Linux := "ZNC Tool"
packageDescription := "ZNC Tool"

dockerRepository := Some("maalka")
dockerBaseImage := "maalka/oracle8"
dockerUpdateLatest := true

lazy val root = (project in file(".")).enablePlugins(PlayScala, JavaAppPackaging)

resolvers += "emueller-bintray" at "http://dl.bintray.com/emueller/maven"
resolvers += "Artifactory Realm" at "https://jfrog.maalka.com/artifactory/libs-release-local/"

// Dependencies
libraryDependencies ++= Seq(
  filters,
  guice,
  ws,
  ehcache,
  // WebJars (i.e. client-side) dependencies
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test",
  "org.mockito" % "mockito-all" % "1.10.19",
  "org.webjars" % "requirejs" % "2.1.22",
  "org.webjars" % "jquery" % "2.1.3",
  "org.webjars" %% "webjars-play" % "2.6.3",
  "org.webjars" % "angularjs" % "1.4.7" exclude("org.webjars", "jquery"),
  "org.webjars" % "highcharts" % "4.2.3",
  "org.webjars" % "highstock" % "4.2.3",
  "org.webjars" % "matchmedia-ng" % "1.0.5",
  "org.webjars.bower" % "filesaver" % "1.3.3",
  "org.webjars.npm" % "ng-file-upload" % "12.2.13",

  "org.typelevel" %% "squants" % "1.3.1-maalka-1.0",

  "com.github.tototoshi" %% "scala-csv" % "1.2.1",

  "com.typesafe.akka" %% "akka-slf4j" % "2.5.15",
  "com.typesafe.akka" %% "akka-stream" % "2.5.15",
  "com.typesafe.play" %% "play-json-joda" % "2.6.8",
  "org.joda" % "joda-convert" % "1.9.2",
  "com.typesafe.play" %% "play-json" % "2.6.8",

  "com.eclipsesource" %% "play-json-schema-validator" % "0.9.4",

  "com.typesafe.play" %% "play-iteratees" % "2.6.1",
  "com.typesafe.play" %% "play-iteratees-reactive-streams" % "2.6.1"

)

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Xcheckinit", // runtime error when a val is not initialized due to trait hierarchies (instead of NPE somewhere else)
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

routesGenerator := InjectedRoutesGenerator

//
// sbt-web configuration
// https://github.com/sbt/sbt-web
//

AngularTemplatesKeys.module := "angular.module('maalka-templates', [])"
AngularTemplatesKeys.naming := {value : String => value.replace("\\", "/")}

// Configure the steps of the asset pipeline (used in stage and dist tasks)
// rjs = RequireJS, uglifies, shrinks to one file, replaces WebJars with CDN
// digest = Adds hash to filename
// gzip = Zips all assets, Asset controller serves them automatically when client accepts them
//pipelineStages := Seq(rjs, digest, gzip)
pipelineStages := Seq(rjs, digest, gzip)


// RequireJS with sbt-rjs (https://github.com/sbt/sbt-rjs#sbt-rjs)
// ~~~
RjsKeys.paths := Map("jsRoutes" -> ("/jsroutes" -> "empty:"),
  "filesaver" -> ("../lib/filesaver/" -> "empty:"),
  "angular" -> ("../lib/angularjs/" -> "empty:"),
  "jquery" -> ("../lib/jquery/" -> "empty:"))

//RjsKeys.mainModule := "main"

RjsKeys.optimize := "none"
RjsKeys.webJarCdns := Map.empty // disable cdn

excludeFilter in (Assets, LessKeys.less) := {
  val public = (baseDirectory.value / "public").getCanonicalPath
  new SimpleFileFilter({ f =>
    println("%s - %s".format(f.getCanonicalPath, (webJarsDirectory in Assets).value.getCanonicalPath))
    (f.getCanonicalPath startsWith public) ||
      (f.getCanonicalPath startsWith (webModuleDirectory in Assets).value.getCanonicalPath) ||
      (f.getCanonicalPath startsWith (webJarsDirectory in Assets).value.getCanonicalPath) ||
      f.isHidden
  })
}

includeFilter in rjs := GlobFilter("*.json") | GlobFilter("*.js") | GlobFilter("*.css") | GlobFilter("*.map")
excludeFilter in uglify := (excludeFilter in uglify).value || GlobFilter("*.min.js")


JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

// Asset hashing with sbt-digest (https://github.com/sbt/sbt-digest)
// ~~~
// md5 | sha1
//DigestKeys.algorithms := "md5"
//includeFilter in digest := "..."
//excludeFilter in digest := "..."

// HTTP compression with sbt-gzip (https://github.com/sbt/sbt-gzip)
// ~~~
// includeFilter in GzipKeys.compress := "*.html" || "*.css" || "*.js"
// excludeFilter in GzipKeys.compress := "..."

// JavaScript linting with sbt-jshint (https://github.com/sbt/sbt-jshint)
// ~~~
// JshintKeys.config := ".jshintrc"

// All work and no play...
emojiLogs
