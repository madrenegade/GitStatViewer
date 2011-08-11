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
import de.madsolutions.util.Cache

class GeneralStatisticsGenerator extends StatGenerator {

  def name = "General"

  private var log: Log = null

  def analyze(log: Log): Elem = {
    this.log = log

    <general>
      <numCommits>{ log.commits.size }</numCommits>
      <authors>
        {
          Cache.authors map {
            author: String =>
              {
                <author>{ author }</author>
              }
          }
        }
      </authors>
      <firstCommit>{ Cache.firstCommit.date }</firstCommit>
      <lastCommit>{ Cache.lastCommit.date }</lastCommit>
      <linesOfCode>{ Cache.linesOfCode }</linesOfCode>
      <commitsPerDay>{ Cache.commitsPerDay }</commitsPerDay>
      <projectAge>{ Cache.projectAge.floor.toInt } days</projectAge>
    </general>
  }

  
}
