/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import scala.collection.mutable.ListBuffer

class Log {

  private val commits: ListBuffer[Commit] = ListBuffer()
  
  def addCommit(commit: Commit) = commits += (commit)
  def getCommits: List[Commit] = commits.toList
  
}
