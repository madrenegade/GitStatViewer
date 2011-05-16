/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

object App {

  def main(args: Array[String]) {
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
    val logExtractor = new LogExtractor
    val log = logExtractor.extractFrom(logData)
    
    println("Analyzing logs")
    val analyzer = new LogAnalyzer
    val statistics = analyzer.analyze(log)
    
    val end = System.currentTimeMillis
    
    println("Logmining took " + (end-start) + "ms")
    
    val printer = new scala.xml.PrettyPrinter(80, 4)
    println(printer.format(statistics))
  }
  
}
