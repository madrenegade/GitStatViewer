/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.stats.generator

import de.madsolutions.gitstatviewer.Commit
import de.madsolutions.gitstatviewer.Log
import de.madsolutions.util.DateHelper
import java.util.Calendar
import java.util.GregorianCalendar
import scala.collection.SortedMap
import scala.xml.Elem

class ActivityStatisticsGenerator extends StatGenerator {
  
  def name = "Activity"

  private var log: Log = null
  
  def analyze(log: Log): Elem = {
    this.log = log
    
    <activity>
      <weekly>
        {
          SortedMap[Int, Int]() ++ commitsByWeek map {
            (kv: (Int, Int)) => {
              <numCommits weeksAgo={kv._1.toString}>{kv._2.toString}</numCommits>
            }
          }
        }
      </weekly>
      <hourOfDay>
        {
          SortedMap[Int, Int]() ++ commitsByHour map {
            (kv: (Int, Int)) => {
              <numCommits hour={kv._1.toString}>{kv._2.toString}</numCommits>
            }
          }
        }
      </hourOfDay>
      <dayOfWeek>
        {
          SortedMap[Int, Int]() ++ commitsByDay map {
            (kv: (Int, Int)) => {
              <numCommits day={kv._1.toString}>{kv._2.toString}</numCommits>
            }
          }
        }
      </dayOfWeek>
    </activity>
  }
  
  private def commitsByWeek = {
    log.commits.groupBy {
      c: Commit => {
        DateHelper.timeSpanBetweenNowAnd(c.date).inWeeks.toInt
      }
    }.map {
      (kv: (Int, List[Commit])) => {
        (kv._1, kv._2.length)
      }
    }
  }
  
  private def commitsByHour = {
    log.commits.groupBy {
      c: Commit => {
        DateHelper.hour(c.date).toInt
      }
    }.map {
      (kv: (Int, List[Commit])) => {
        (kv._1, kv._2.length)
      }
    }
  }
  
  private def commitsByDay = {
    log.commits.groupBy {
      c: Commit => {
        DateHelper.dayOfWeek(c.date)
      }
    }.map {
      (kv: (Int, List[Commit])) => {
        (kv._1, kv._2.length)
      }
    }
  }
}
