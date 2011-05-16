/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer


import java.text.SimpleDateFormat
import java.util.Date
import java.util.GregorianCalendar
import javax.xml.datatype.DatatypeFactory

class LogExtractor {
  
  private val CommitLineRE = """commit ([a-z0-9]{40})""".r
  private val AuthorLineRE = """Author: ([\w\s<>@\\.]+)""".r
  private val DateLineRE = """Date:[\s]*([\w\d\s:+]+)""".r
  
  private val log = new Log
  private var currentCommit: Commit = null
  
  private var parsingDiff = false

  def extractFrom(logData: String): Option[Log] = {
    
    logData.lines foreach processLine
    
    
    Some(log)
  }
  
  private def processLine(line: String) = line.trim match {
    case CommitLineRE(id) => {
        parsingDiff = false
        currentCommit = new Commit(id)
        log.addCommit(currentCommit)
    }
    case AuthorLineRE(author) => {
        currentCommit.author = author
    }
    case DateLineRE(date) => {
        val formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy Z")
        println(formatter.parse(date))
    }
    case s: String => {
        if(parsingDiff) {
          
        } else {
          currentCommit.message += s
        }
    }
  }
}
