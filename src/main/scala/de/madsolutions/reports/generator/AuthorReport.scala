/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.reports.generator

import scala.xml.Elem

class AuthorReport extends ReportGenerator {

  def name: String = "Authors"

  def generateReport(outputPath: String, stats: Elem): Elem = {
    <p></p>
  }
  
}
