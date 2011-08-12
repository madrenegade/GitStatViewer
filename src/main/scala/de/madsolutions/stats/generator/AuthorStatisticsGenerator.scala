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
import de.madsolutions.util.Cache

// TODO: cumulated added lines per author
// TODO: cumulated deleted lines per author
// TODO: cumulated effective lines per author
// TODO: commits per author over time

class AuthorStatisticsGenerator extends StatGenerator {

  def name = "Author"
  
  private var log: Log = null
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    <author-stats>
      <authors>
        {
          log.authors.sorted map {
            authorName: String => {
              val commitsFromAuthor = Cache.commitsByAuthor.getOrElse(authorName, List())
              val numCommits = commitsFromAuthor.length
              <author name={authorName} commits={numCommits.toString}>
                <percentageOfAllCommits>{(numCommits.asInstanceOf[Double] / log.commits.length).toString}</percentageOfAllCommits>
                <firstCommit>{commitsFromAuthor.map(_.date).sorted.head}</firstCommit>
                <lastCommit>{commitsFromAuthor.map(_.date).sorted.last}</lastCommit>
                <addedLines>{addedLines(commitsFromAuthor).toString}</addedLines>
                <deletedLines>{deletedLines(commitsFromAuthor).toString}</deletedLines>
                <avgCommitsPerDay>{avgCommitsPerDay(commitsFromAuthor, Cache.projectAge).toString}</avgCommitsPerDay>
                <activeDays>{commitsByDay(commitsFromAuthor).size.toString}</activeDays>
              </author>
            }
          }
        }
      </authors>
    </author-stats>
  }
  
  private def avgCommitsPerDay(commits: List[Commit], age: Double) = {
    commits.size.toDouble / age
  }
  
  private def commitsByDay(commits: List[Commit]) = {
    commits groupBy {
      c: Commit => DateHelper.day(c.date)
    }
  }
            
  private def addedLines(commits: List[Commit]) = commits map (_.addedLines) reduceLeft(_+_)
  
  private def deletedLines(commits: List[Commit]) = commits map (_.deletedLines) reduceLeft(_+_)
}
