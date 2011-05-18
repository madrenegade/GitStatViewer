/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.stats.generator

import de.madsolutions.gitstatviewer.Commit
import de.madsolutions.gitstatviewer.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import scala.collection.mutable.HashMap
import scala.xml.Elem

class AuthorStatisticsGenerator extends StatGenerator {

  private var log: Log = null
  
  private val commits = HashMap[String, List[Commit]]()
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    authors foreach {
      authorName: String => {
        commits += ((authorName, log.commits filter (_.author == authorName)))
      }
    }
    
    <author-stats>
      <authors>
        {
          authors.sorted map {
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
    
    val calendar1 = new GregorianCalendar
    calendar1.setTime(first)
    
    val calendar2 = new GregorianCalendar
    calendar2.setTime(last)
    
    val t1 = calendar1.getTimeInMillis
    val t2 = calendar2.getTimeInMillis
    
    (t2-t1).toDouble / 1000.0 / 60.0 / 60.0 / 24.0
  }
  
  private def avgCommitsPerDay(commits: List[Commit], age: Double) = {
    commits.size.toDouble / age
  }
  
  // TODO: map Day -> CountOfCommits
  // TODO: reduce sum count of commits
  private def commitsByDay(commits: List[Commit]) = {
    val fmt = new SimpleDateFormat("yyyy-MM-dd")
    
    commits groupBy {
      c: Commit => fmt.format(c.date)
    }
  }
            
  private def addedLines(commits: List[Commit]) = {
    commits.map (_.diff.count {
       line => {line.startsWith("+") && !line.startsWith("+++")}
      }
    ).reduceLeft(_+_)
  }
  
  private def deletedLines(commits: List[Commit]) = {
    commits.map (_.diff.count {
       line => {line.startsWith("-") && !line.startsWith("---")}
      }
    ).reduceLeft(_+_)
  }
  
  private def authors = log.commits map (_.author) distinct
}
