/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.reports.generator

import java.io.File
import org.jfree.chart.ChartFactory
import org.jfree.data.general._
import org.jfree.data.xy._
import org.jfree.data.category._
import org.jfree.chart.plot.PlotOrientation
import scala.xml.Elem
import scala.xml.Node
import de.madsolutions.util.Chart

class AuthorReport extends ReportGenerator {

  def name = "Authors"
  
  private var outputPath: String = null

  def generateReport(outputPath: String, stats: Elem): Elem = {
    this.outputPath = outputPath
    
    val authors = (stats \ "author-stats" \ "authors")(0)
    
    <div>
      <img src={commitsChart(authors)} />
      <hr />
      <img src={addedLinesChart(authors)} />
    </div>
  }
  
  private def addedLinesChart(authors: Node) = {
    val dataset = new DefaultCategoryDataset
    
    (authors \ "author") foreach {
      author: Node => {
        val added = (author \ "addedLines").text.toInt
        val deleted = (author \ "deletedLines").text.toInt
        val effectiveApprox = added - deleted
      
        dataset.addValue(added.toDouble, "Added", (author \ "@name").text)
        dataset.addValue(deleted.toDouble, "Deleted", (author \ "@name").text)
        dataset.addValue(effectiveApprox.toDouble, "Approximated total", (author \ "@name").text)
      }
    }
    
    val chart = ChartFactory.createBarChart3D("CodeLines", "Author", "Lines of code", dataset, PlotOrientation.VERTICAL, true, true, false)
    Chart.save(outputPath, chart)
  }
  
  private def commitsChart(authors: Node) = {
    val dataset = new DefaultPieDataset
    
    (authors \ "author") foreach {
      author: Node => {
        dataset.setValue((author \ "@name").text, (author \ "@commits").text.toDouble)
      }
    }
    
    val chart = ChartFactory.createPieChart3D("Commits", dataset, true, true, false)
    Chart.save(outputPath, chart)
  }
}
