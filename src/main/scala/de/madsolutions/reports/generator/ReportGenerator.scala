/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.reports.generator

import scala.xml.Elem

trait ReportGenerator {

  def generateReport(outputPath: String, stats: Elem): Unit
  
}
