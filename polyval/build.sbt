import Dependencies._

ThisBuild / organization       := "com.polyval"
ThisBuild / scalaVersion       := "2.13.11"
ThisBuild / evictionErrorLevel := Level.Warn
ThisBuild / scalafixDependencies += Libraries.OrganizeImports

lazy val compilerOptions = Seq(
  "-Ymacro-annotations",
  "-Xsource:3",
  "-Yrangepos",
  "-Wconf:cat=unused:error",
  "-deprecation"
)

lazy val root = (project in file(".")).settings(
  name := "polyval",
  libraryDependencies ++= Seq(
    Libraries.CatsEffect,
    Libraries.CatsKernel,
    Libraries.CatsEffectStd,
    Libraries.CirceCore,
    Libraries.CirceGeneric,
    Libraries.CirceParser,
    Libraries.CommonsCsv,
    Libraries.OrganizeImports,
    Libraries.Fs2Core,
    Libraries.Fs2Io,
    Libraries.Http4sCirce,
    Libraries.Http4sDsl,
    Libraries.Http4sEmber,
    Libraries.Log4Cats,
    Libraries.Logback % Runtime,
    Libraries.Log4JCore,
    Libraries.Poi,
    Libraries.PoiOoxml,
    Libraries.PureConfig,
    Compiler.BetterMonadicFor,
    Compiler.KindProjector,
    Compiler.SemanticDB
  ),
  scalacOptions ++= compilerOptions,
  fork := true
)

addCommandAlias("lint", "scalafmtAll; scalafixAll --rules OrganizeImports; scalafmtSbt")
addCommandAlias(
  "build",
  "clean; all scalafmtCheckAll scalafmtSbtCheck compile test it:compile it:test doc"
)
