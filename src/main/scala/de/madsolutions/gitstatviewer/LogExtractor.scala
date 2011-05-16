/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import scala.io.Source

class LogExtractor {

  def extractFrom(logData: String): Option[Log] = {
    val log = new Log
    
    logData.split("commit") filter(!_.trim.isEmpty) foreach {
      s: String => {
        log.addCommit(extractCommit(s))
      }
    }
    
    Some(log)
  }
  
  private def extractCommit(commitData: String): Commit = {
    
    val lines = commitData.lines
    val commit = new Commit(lines.next)
    
    commit
  }
}
