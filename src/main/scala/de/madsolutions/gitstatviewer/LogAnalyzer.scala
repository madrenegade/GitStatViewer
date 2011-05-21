/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import de.madsolutions.stats.generator.ActivityStatisticsGenerator
import de.madsolutions.stats.generator.AuthorActivityStatisticsGenerator
import de.madsolutions.stats.generator.AuthorStatisticsGenerator
import de.madsolutions.stats.generator.GeneralStatisticsGenerator
import de.madsolutions.stats.generator.StatGenerator
import scala.collection.SortedSet
import scala.xml.Elem
import scala.xml.XML

class LogAnalyzer {
  
  private var log: Log = null
  
  val generators = List[StatGenerator](
    new GeneralStatisticsGenerator,
    new AuthorStatisticsGenerator,
    new ActivityStatisticsGenerator,
    new AuthorActivityStatisticsGenerator
  )
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    val totalCommits = this.log.commits.length
    
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
