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
import de.madsolutions.util.Cache

class ActivityStatisticsGenerator extends StatGenerator {

  def name = "Activity"

  private var log: Log = null

  def analyze(log: Log): Elem = {
    this.log = log

    <activity>
      <weekly>
        {
          Cache.commitsByWeek map {
            (kv: (Int, Int)) =>
              {
                <numCommits weeksAgo={ kv._1.toString }>{ kv._2.toString }</numCommits>
              }
          }
        }
      </weekly>
      <daily>
        {
          Cache.commitsByDay map {
            (kv: (String, Int)) =>
              {
                <numCommits date={ kv._1 }>{ kv._2.toString }</numCommits>
              }
          }
        }
      </daily>
      <hourOfDay>
        {
          Cache.commitsByHour map {
            (kv: (Int, Int)) =>
              {
                <numCommits hour={ kv._1.toString }>{ kv._2.toString }</numCommits>
              }
          }
        }
      </hourOfDay>
      <dayOfWeek>
        {
          Cache.commitsByWeekDay map {
            (kv: (Int, Int)) =>
              {
                <numCommits day={ kv._1.toString }>{ kv._2.toString }</numCommits>
              }
          }
        }
      </dayOfWeek>
      <monthOfYear>
        {
          Cache.commitsByMonth map {
            (kv: (Int, Int)) =>
              {
                <numCommits month={ kv._1.toString }>{ kv._2.toString }</numCommits>
              }
          }
        }
      </monthOfYear>
    </activity>
  }

}
