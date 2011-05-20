/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import scala.xml.XML
import scala.util.logging.ConsoleLogger

object GitStatViewer extends App {
  
  val directory = args match {
    case Array() => {
        println("No diretory specified - using current working directory")
        "."
      }
    case Array(x) => {
        println("using specified directory")
        x
      }
    case _ => {
        println("Invalid number of arguments")
        sys.exit(1)
      }
  }
    
  val start = System.currentTimeMillis
    
  println("Fetching logs")
  val logData = LogFetcher.fetch(directory)
    
  println("Extracting logs")
  val logExtractor = new LogExtractor //with ConsoleLogger
  val log = logExtractor.extractFrom(logData)
    
  println("Analyzing logs")
  val analyzer = new LogAnalyzer
  val statistics = analyzer.analyze(log)
    
  val end = System.currentTimeMillis
    
  println("Logmining took " + (end-start) + "ms")
    
  val printer = new scala.xml.PrettyPrinter(80, 4)
  val pretty = printer format statistics
  
  println(pretty)  
  
  XML.save("git-statistics.xml", XML.loadString(pretty), "UTF-8", xmlDecl = true)
  
  // TODO: transform to xhtml using xslt
}
