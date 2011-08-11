/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.stats.generator

import de.madsolutions.gitstatviewer.Commit
import de.madsolutions.gitstatviewer.Log
import de.madsolutions.util.DateHelper
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.SortedMap
import scala.collection.mutable.HashMap
import scala.xml.Elem
import de.madsolutions.util.Cache

class AuthorActivityStatisticsGenerator extends StatGenerator {

  def name = "AuthorActivity"
  
  private var log: Log = null
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    <author-activity>
      <authors>
        {
          log.authors.sorted map {
            authorName: String => {
              val commitsFromAuthor = Cache.commitsByAuthor.getOrElse(authorName, List())
              
              <author name={authorName}>
                {
                  SortedMap[String, List[Commit]]() ++ commitsByDay(commitsFromAuthor) map {
                    (kv: (String, List[Commit])) => {
                      <activity date={kv._1}>
                        <addedLines>{addedLines(kv._2)}</addedLines>
                        <deletedLines>{deletedLines(kv._2)}</deletedLines>
                      </activity>
                    }
                  }
                }
              </author>
            }
          }
        }
      </authors>
    </author-activity>
  }
  
  private def addedLines(commits: List[Commit]) = {
    commits map {
      c: Commit => {
        c.addedLines
      }
    } reduceLeft(_+_)
  }
  
  private def deletedLines(commits: List[Commit]) = {
    commits map {
      c: Commit => {
        c.deletedLines
      }
    } reduceLeft(_+_)
  }
  
  private def commitsByDay(commits: List[Commit]) = {
    val fmt = new SimpleDateFormat("yyyy-MM-dd")
    
    commits.groupBy {
      c: Commit => {
        DateHelper.day(c.date)
      }
    }.map {
      (kv: (Date, List[Commit])) => {
        (fmt.format(kv._1), kv._2)
      }
    }
  }
}
