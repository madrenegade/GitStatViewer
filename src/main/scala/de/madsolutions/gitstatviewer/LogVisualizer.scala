/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import de.madsolutions.reports.generator.ReportGenerator
import scala.xml.Elem

class LogVisualizer(stats: Elem) {
  
  private def reporters = List[ReportGenerator]()

  def generateReport(outputPath: String) = {
    reporters foreach {
      _.generateReport(outputPath, stats)
    }
  }
  
}
