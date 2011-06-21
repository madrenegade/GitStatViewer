/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import scala.xml.XML
import scala.util.logging.ConsoleLogger
import de.madsolutions.util.ProgramOptions
import de.madsolutions.stats.generator.StatGenerator
import de.madsolutions.reports.generator.ActivityReport
import de.madsolutions.reports.generator.AuthorReport
import de.madsolutions.reports.generator.GeneralReport

object GitStatViewer extends App with ConsoleLogger {
  
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
  
  log("Output will be saved to directory gitstats")
    
  val start = System.currentTimeMillis
    
  log("Fetching logs")
  val logData = LogFetcher.fetch(directory)
    
  log("Extracting logs")
  val logExtractor = new LogExtractor
  val log = logExtractor.extractFrom(logData)
    
  log("Analyzing logs")
  val analyzer = new LogAnalyzer
  val statistics = analyzer.analyze(log)
   
  val visualizer = new LogVisualizer(statistics) {  
    override val reporters = new GeneralReport :: new AuthorReport :: new ActivityReport :: Nil
  }
  
  visualizer.generateReport("gitstats")
  
  val end = System.currentTimeMillis
    
  println("Finished after " + (end-start) + "ms")
}
