/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.stats.generator

import de.madsolutions.gitstatviewer.Commit
import de.madsolutions.gitstatviewer.Log
import java.util.Date
import scala.collection.SortedSet
import scala.xml.Elem

class GeneralStatisticsGenerator extends StatGenerator {

  private var log: Log = null
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    <general>
      <numCommits>{log.getCommits.size}</numCommits>
      <authors>
        {
          authors.sorted map {
            author: String => {
              <author>{author}</author>
            }
          }
        }
      </authors>
      <firstCommit>{firstCommit}</firstCommit>
      <lastCommit>{lastCommit}</lastCommit>
    </general>
  }
  
  private def authors: List[String] = {
    log.getCommits map (_.author) distinct
  }
  
  private def firstCommit: Date = {
    log.getCommits.map (_.date).sorted.head
  }
  
  private def lastCommit: Date = {
    log.getCommits.map (_.date).sorted.last
  }
}
