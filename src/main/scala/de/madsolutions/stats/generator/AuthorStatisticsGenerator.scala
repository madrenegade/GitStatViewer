/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.stats.generator

import de.madsolutions.gitstatviewer.Commit
import de.madsolutions.gitstatviewer.Log
import scala.collection.mutable.HashMap
import scala.xml.Elem

class AuthorStatisticsGenerator extends StatGenerator {

  private var log: Log = null
  
  private val commits = HashMap[String, List[Commit]]()
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    authors foreach {
      authorName: String => {
        commits += ((authorName, log.getCommits filter (_.author == authorName)))
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
                <percentageOfAllCommits>{(numCommits.asInstanceOf[Double] / log.getCommits.length).toString}</percentageOfAllCommits>
                <firstCommit>{commitsFromAuthor.map(_.date).sorted.head}</firstCommit>
                <lastCommit>{commitsFromAuthor.map(_.date).sorted.last}</lastCommit>
              </author>
            }
          }
        }
      </authors>
    </author-stats>
  }
  
  private def authors: List[String] = {
    log.getCommits map (_.author) distinct
  }
}
