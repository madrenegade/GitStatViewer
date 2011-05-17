/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import de.madsolutions.stats.generator.AuthorStatisticsGenerator
import de.madsolutions.stats.generator.GeneralStatisticsGenerator
import de.madsolutions.stats.generator.StatGenerator
import scala.collection.SortedSet
import scala.xml.Elem
import scala.xml.XML

class LogAnalyzer {
  
  private var log: Log = null
  private val generators = List[StatGenerator](
    new GeneralStatisticsGenerator,
    new AuthorStatisticsGenerator
  )
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    val totalCommits = this.log.getCommits.length
    
    <statistics>
      {generators map {
          g: StatGenerator => {
            g.analyze(log)
          }
        }
      }
    </statistics>
  }
}
