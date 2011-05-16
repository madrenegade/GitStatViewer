/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

class LogAnalyzer {

  def analyze(log: Log): Option[Statistics] = {
    val totalCommits = log.getCommits.length
    val generalStatistics = new GeneralStatistics(totalCommits)
    
    Some(new Statistics(generalStatistics))
  }
  
}
