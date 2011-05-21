/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import scala.collection.mutable.ListBuffer

class Log {

  private val commitBuffer: ListBuffer[Commit] = ListBuffer()
  
  def addCommit(commit: Commit) = commitBuffer += (commit)
  def commits: List[Commit] = commitBuffer.toList
  
  def authors = commits map (_.author) distinct
}
