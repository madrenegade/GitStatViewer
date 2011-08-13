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
    val activity = (stats \ "activity")(0)
    val firstCommit = DateHelper parse ((stats \ "general" \ "firstCommit").text, "EEE MMM dd hh:mm:ss Z yyyy")
    val lastCommit = DateHelper parse ((stats \ "general" \ "lastCommit").text, "EEE MMM dd hh:mm:ss Z yyyy")

    <div>
      <img src={ addedLinesOverTimeChart(authors, firstCommit, lastCommit) }/>
      <hr/>
      <img src={ deletedLinesOverTimeChart(authors, firstCommit, lastCommit) }/>
      <hr/>
      <img src={ totalLinesOverTimeChart(authors, firstCommit, lastCommit) }/>
      <hr/>
      <img src={ commitsByMonthChart(activity, firstCommit, lastCommit) }/>
      <hr/>
      <img src={ commitsByWeekChart(activity, firstCommit, lastCommit) }/>
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
  
  private def totalLinesOverTimeChart(authors: Node, firstCommit: Date, lastCommit: Date) = {

    val dataset = new TimeSeriesCollection

    val firstDay = new Day(firstCommit)
    val lastDay = new Day(lastCommit)

    (authors \ "author") foreach {
      author: Node =>
        {
          val series = new TimeSeries((author \ "@name").text)
          var currentDay = firstDay

          while (currentDay.compareTo(lastDay) <= 0) {
            val totalLines = (author \ "activity").find { n: Node => (n \ "@date").text == DateHelper.format(currentDay.getStart) } match {
              case Some(activity) => (activity \ "totalLines").text.toInt
              case None => 0
            }

            series.add(currentDay, totalLines)
            currentDay = currentDay.next.asInstanceOf[Day]
          }

          dataset.addSeries(series)
        }
    }

    val chart = ChartFactory.createTimeSeriesChart("TotalLinesOfCode", "Date", "Lines of code", dataset, true, true, false)
    Chart.save(outputPath, chart)
  }

  private def commitsByMonthChart(activity: Node, firstCommit: Date, lastCommit: Date) = {

    val dataset = new DefaultCategoryDataset

    val firstMonth = new Month(firstCommit)
    val lastMonth = new Month(lastCommit)

    var currentMonth = firstMonth

    while (currentMonth.compareTo(lastMonth) <= 0) {
      val numCommits = (activity \ "monthOfYear" \ "numCommits").find { n: Node => (n \ "@month").text.toInt == currentMonth.getMonth } match {
        case Some(node) => node.text.toInt
        case None => 0
      }

      //series.add(currentMonth, numCommits)
      dataset.addValue(numCommits, "Month", currentMonth)
      currentMonth = currentMonth.next.asInstanceOf[Month]
    }

    val chart = ChartFactory.createBarChart("CommitsByMonth", "Date", "# commits", dataset, PlotOrientation.VERTICAL, true, true, false)
    Chart.save(outputPath, chart)
  }
  
  private def commitsByWeekChart(activity: Node, firstCommit: Date, lastCommit: Date) = {

    val dataset = new DefaultCategoryDataset

    val firstWeek = new Week(firstCommit)
    val lastWeek = new Week(lastCommit)

    var currentWeek = firstWeek

    while (currentWeek.compareTo(lastWeek) <= 0) {
      val numCommits = (activity \ "weekly" \ "numCommits").find { n: Node => (n \ "@weeksAgo").text.toInt == DateHelper.timeSpanBetweenNowAnd(currentWeek.getStart()).inWeeks.toInt } match {
        case Some(node) => node.text.toInt
        case None => 0
      }

      dataset.addValue(numCommits, "Weeks ago", DateHelper.timeSpanBetweenNowAnd(currentWeek.getStart).inWeeks.toInt)
      currentWeek = currentWeek.next.asInstanceOf[Week]
    }

    val chart = ChartFactory.createBarChart("CommitsByWeek", "Date", "# commits", dataset, PlotOrientation.VERTICAL, true, true, false)
    Chart.save(outputPath, chart)
  }
}
