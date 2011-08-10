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

  def name = "General"

  private var log: Log = null

  private final val MILLISECONDS_PER_SECOND = 1000
  private final val SECONDS_PER_MINUTE = 60
  private final val MINUTES_PER_HOUR = 60
  private final val HOURS_PER_DAY = 24

  private final val MILLISECONDS_PER_DAY = MILLISECONDS_PER_SECOND *
    SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY

  def analyze(log: Log): Elem = {
    this.log = log

    <general>
      <numCommits>{ log.commits.size }</numCommits>
      <authors>
        {
          authors.sorted map {
            author: String =>
              {
                <author>{ author }</author>
              }
          }
        }
      </authors>
      <firstCommit>{ firstCommit }</firstCommit>
      <lastCommit>{ lastCommit }</lastCommit>
      <linesOfCode>{ linesOfCode }</linesOfCode>
      <commitsPerDay>{ commitsPerDay }</commitsPerDay>
      <projectAge>{ projectAge.floor.toInt } days</projectAge>
    </general>
  }

  private def authors = {
    log.commits map (_.author) distinct
  }

  private def firstCommit = {
    log.commits.map(_.date).sorted.head
  }

  private def lastCommit = {
    log.commits.map(_.date).sorted.last
  }

  private def linesOfCode = {
    log.commits.foldLeft(0)((lines, commit) => lines + (commit.addedLines - commit.deletedLines))
  }

  private def commitsPerDay = {
    val days = (lastCommit.getTime() - firstCommit.getTime()).toDouble / MILLISECONDS_PER_DAY.toDouble
    log.commits.size.toDouble / days
  }

  private def projectAge = {
    (new Date().getTime() - firstCommit.getTime()).toDouble / MILLISECONDS_PER_DAY.toDouble
  }
}
