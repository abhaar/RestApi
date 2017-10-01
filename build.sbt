name := "restapi"
 
version := "1.0"
      
lazy val `restapi` = (project in file(".")).enablePlugins(PlayJava)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( javaJdbc , javaWs )
libraryDependencies += "org.ehcache" % "ehcache" % "3.3.1"
libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"
libraryDependencies += guice

//unmanagedResourceDirectories in Test +=  (baseDirectory.value /"target/web/public/test" )

fork in test := true
//baseDirectory in test := file("/")
//scriptClasspath := Seq("../conf/", "*")
//PlayKeys.externalizeResources := false
javaOptions in test += "-Dconfig.resource=conf/application.test.conf"