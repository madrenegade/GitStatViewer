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
    
    val logData = LogFetcher.fetch(directory) match {
      case Some(x) => x
      case None => sys.exit(2)
    }
    
    val logExtractor = new LogExtractor
    
    val log = logExtractor.extractFrom(logData) match {
      case Some(x) => x
      case None => sys.exit(3)
    }
    
    val analyzer = new LogAnalyzer
    
    val statistics = analyzer.analyze(log) match {
      case Some(x) => x
      case None => sys.exit(4)
    }
    
    println("Commits: " + statistics.general.numCommits)
    
  }
  
}
