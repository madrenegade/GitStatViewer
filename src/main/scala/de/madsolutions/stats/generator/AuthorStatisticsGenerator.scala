/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.stats.generator

import de.madsolutions.gitstatviewer.Commit
import de.madsolutions.gitstatviewer.Log
import de.madsolutions.util.DateHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import scala.collection.mutable.HashMap
import scala.xml.Elem

// TODO: cumulated added lines per author
// TODO: cumulated deleted lines per author
// TODO: cumulated effective lines per author
// TODO: commits per author over time

class AuthorStatisticsGenerator extends StatGenerator {

  def name = "Author"
  
  private var log: Log = null
  
  private val commits = HashMap[String, List[Commit]]()
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    log.authors foreach {
      authorName: String => {
        commits += ((authorName, log.commits filter (_.author == authorName)))
      }
    }
    
    <author-stats>
      <authors>
        {
          log.authors.sorted map {
            authorName: String => {
              val commitsFromAuthor = commits.getOrElse(authorName, List())
              val numCommits = commitsFromAuthor.length
              <author name={authorName} commits={numCommits.toString}>
                <percentageOfAllCommits>{(numCommits.asInstanceOf[Double] / log.commits.length).toString}</percentageOfAllCommits>
                <firstCommit>{commitsFromAuthor.map(_.date).sorted.head}</firstCommit>
                <lastCommit>{commitsFromAuthor.map(_.date).sorted.last}</lastCommit>
                <addedLines>{addedLines(commitsFromAuthor).toString}</addedLines>
                <deletedLines>{deletedLines(commitsFromAuthor).toString}</deletedLines>
                <avgCommitsPerDay>{avgCommitsPerDay(commitsFromAuthor, age).toString}</avgCommitsPerDay>
                <activeDays>{commitsByDay(commitsFromAuthor).size.toString}</activeDays>
              </author>
            }
          }
        }
      </authors>
    </author-stats>
  }
  
  private def age = {
    val last = log.commits.map (_.date).sorted.last
    val first = log.commits.map (_.date).sorted.head
    
    DateHelper.timeSpanBetween(first, last).inDays
  }
  
  private def avgCommitsPerDay(commits: List[Commit], age: Double) = {
    commits.size.toDouble / age
  }
  
  private def commitsByDay(commits: List[Commit]) = {
    commits groupBy {
      c: Commit => DateHelper.day(c.date)
    }
  }
            
  private def addedLines(commits: List[Commit]) = {
    commits.map (_.addedLines).reduceLeft(_+_)
  }
  
  private def deletedLines(commits: List[Commit]) = {
    commits.map (_.diff.count {
       line => {line.startsWith("-") && !line.startsWith("---")}
      }
    ).reduceLeft(_+_)
  }
}
