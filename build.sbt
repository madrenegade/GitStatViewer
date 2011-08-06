name := "GitStatViewer"
 
scalaVersion := "2.9.0-1"

resolvers += "Fusesource Repository" at "http://repo.fusesource.com/nexus/content/repositories/public"

resolvers += "JBoss Repos" at "http://repository.jboss.org/nexus/content/groups/public-jboss"
 
libraryDependencies ++= Seq(
  "junit" % "junit" % "4.8.2" % "test->default",
  "jfree" % "jfreechart" % "1.0.13" % "compile->default",
  "org.slf4j" % "slf4j-log4j12" % "1.6.1",
  "org.fusesource.scalate" % "scalate-core" % "1.5.0"% "compile->default"
)

scalacOptions in Compile ++= Seq(
	"-deprecation"
)
