/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.reports.generator

import scala.xml.Elem
import scala.xml.Node
import org.jfree.data.category._
import org.jfree.data.time._
import org.jfree.chart.ChartFactory
import de.madsolutions.util.Chart
import org.jfree.chart.plot.PlotOrientation
import java.text.SimpleDateFormat
import java.util.Date
import de.madsolutions.util.DateHelper

class ActivityReport extends ReportGenerator {

  def name = "Activity"

  private var outputPath: String = null

  def generateReport(outputPath: String, stats: Elem): Elem = {
    this.outputPath = outputPath

    val authors = (stats \ "author-activity" \ "authors")(0)
    val firstCommit = DateHelper parse ((stats \ "general" \ "firstCommit").text, "EEE MMM dd hh:mm:ss Z yyyy")
    val lastCommit = DateHelper parse ((stats \ "general" \ "lastCommit").text, "EEE MMM dd hh:mm:ss Z yyyy")

    <div>
      <img src={ addedLinesOverTimeChart(authors, firstCommit, lastCommit) }/>
      <hr/>
      <img src={ deletedLinesOverTimeChart(authors, firstCommit, lastCommit) }/>
    </div>
  }

  private def addedLinesOverTimeChart(authors: Node, firstCommit: Date, lastCommit: Date) = {

    val dataset = new TimeSeriesCollection

    val firstDay = new Day(firstCommit)
    val lastDay = new Day(lastCommit)

    (authors \ "author") foreach {
      author: Node =>
        {
          val series = new TimeSeries((author \ "@name").text)
          var currentDay = firstDay

          while (currentDay.compareTo(lastDay) <= 0) {
            val addedLines = (author \ "activity").find { n: Node => (n \ "@date").text == DateHelper.format(currentDay.getStart) } match {
              case Some(activity) => (activity \ "addedLines").text.toInt
              case None => 0
            }

            series.add(currentDay, addedLines)
            currentDay = currentDay.next.asInstanceOf[Day]
          }

          dataset.addSeries(series)
        }
    }

    val chart = ChartFactory.createTimeSeriesChart("AddedLinesOfCode", "Date", "Lines of code", dataset, true, true, false)
    Chart.save(outputPath, chart)
  }

  private def deletedLinesOverTimeChart(authors: Node, firstCommit: Date, lastCommit: Date) = {

    val dataset = new TimeSeriesCollection

    val firstDay = new Day(firstCommit)
    val lastDay = new Day(lastCommit)

    (authors \ "author") foreach {
      author: Node =>
        {
          val series = new TimeSeries((author \ "@name").text)
          var currentDay = firstDay

          while (currentDay.compareTo(lastDay) <= 0) {
            val deletedLines = (author \ "activity").find { n: Node => (n \ "@date").text == DateHelper.format(currentDay.getStart) } match {
              case Some(activity) => (activity \ "deletedLines").text.toInt
              case None => 0
            }

            series.add(currentDay, deletedLines)
            currentDay = currentDay.next.asInstanceOf[Day]
          }

          dataset.addSeries(series)
        }
    }

    val chart = ChartFactory.createTimeSeriesChart("DeletedLinesOfCode", "Date", "Lines of code", dataset, true, true, false)
    Chart.save(outputPath, chart)
  }
}
