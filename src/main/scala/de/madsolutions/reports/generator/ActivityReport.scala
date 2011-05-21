/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.reports.generator

import scala.xml.Elem

class ActivityReport extends ReportGenerator {

  def name = "Activity"
  
  private var outputPath: String = null
  
  def generateReport(outputPath: String, stats: Elem): Elem = {
    this.outputPath = outputPath
    
    val authors = (stats \ "author-activity" \ "authors")(0)
    
    <div>
//      <img src={addedLinesOverTimeChart(authors)} />
    </div>
  }
  
//  private def addedLinesOverTimeChart(authors: Node) = {
//    val dataset = new DefaultCategoryDataset
//    
//    (authors \ "author") foreach {
//      author: Node => {
//        val added = (author \ "addedLines").text.toInt
//        val deleted = (author \ "deletedLines").text.toInt
//        val effectiveApprox = added - deleted
//      
//        dataset.addValue(added.toDouble, "Added", (author \ "@name").text)
//        dataset.addValue(deleted.toDouble, "Deleted", (author \ "@name").text)
//        dataset.addValue(effectiveApprox.toDouble, "Approximated total", (author \ "@name").text)
//      }
//    }
//    
//    val chart = ChartFactory.createBarChart3D("CodeLines", "Author", "Lines of code", dataset, PlotOrientation.VERTICAL, true, true, false)
//    Chart.save(outputPath, chart)
//  }
  
}
