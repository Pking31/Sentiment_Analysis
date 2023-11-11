import sbt._

object Dependencies {
  object V {
    val BetterMonadicFor = "0.3.1"
    val CatsEffect       = "3.5.2"
    val Circe            = "0.14.6"
    val CommonsCsv       = "1.10.0"
    val Fs2              = "3.9.2"
    val Http4s           = "0.23.23"
    val KindProjector    = "0.13.2"
    val Log4Cats         = "2.6.0"
    val Log4JCore        = "2.21.1"
    val Logback          = "1.4.11"
    val OrganizeImports  = "0.6.0"
    val Poi              = "5.2.4"
    val PureConfig       = "0.17.4"
    val SemanticDB       = "4.8.12"
  }

  object Libraries {
    def http4s(artifact: String): ModuleID = "org.http4s" %% s"http4s-$artifact" % V.Http4s
    def circe(artifact: String): ModuleID  = "io.circe"   %% s"circe-$artifact"  % V.Circe

    val CatsEffect      = "org.typelevel"           %% "cats-effect"        % V.CatsEffect
    val CatsEffectStd   = "org.typelevel"           %% "cats-effect-std"    % V.CatsEffect
    val CatsKernel      = "org.typelevel"           %% "cats-effect-kernel" % V.CatsEffect
    val CirceCore       = circe("core")
    val CirceGeneric    = circe("generic")
    val CirceParser     = circe("parser")
    val CommonsCsv      = "org.apache.commons"       % "commons-csv"        % V.CommonsCsv
    val Fs2Core         = "co.fs2"                  %% "fs2-core"           % V.Fs2
    val Fs2Io           = "co.fs2"                  %% "fs2-io"             % V.Fs2
    val Http4sCirce     = http4s("circe")
    val Http4sDsl       = http4s("dsl")
    val Http4sEmber     = http4s("ember-server")
    val Log4Cats        = "org.typelevel"           %% "log4cats-slf4j"     % V.Log4Cats
    val Log4JCore       = "org.apache.logging.log4j" % "log4j-core"         % V.Log4JCore
    val Logback         = "ch.qos.logback"           % "logback-classic"    % V.Logback
    val OrganizeImports = "com.github.liancheng"    %% "organize-imports"   % V.OrganizeImports
    val Poi             = "org.apache.poi"           % "poi"                % V.Poi
    val PoiOoxml        = "org.apache.poi"           % "poi-ooxml"          % V.Poi
    val PureConfig      = "com.github.pureconfig"   %% "pureconfig"         % V.PureConfig
  }

  object Compiler {

    val BetterMonadicFor = compilerPlugin("com.olegpy" %% "better-monadic-for" % V.BetterMonadicFor)
    val KindProjector    = compilerPlugin("org.typelevel" %% "kind-projector" % V.KindProjector cross CrossVersion.full)
    val SemanticDB       = compilerPlugin("org.scalameta" % "semanticdb-scalac" % V.SemanticDB cross CrossVersion.full)
  }
}
