/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import java.text.SimpleDateFormat

class LogExtractor {
  
  private val CommitLineRE = """commit ([a-z0-9]{40})""".r
  private val AuthorLineRE = """Author: ([\w\s-]+)(<[\w\s\\(\\)\\.@-]+>)?""".r
  private val DateLineRE = """Date:[\s]*([\w\d\s:+]+)""".r
  private val CommitMessageLineRE = """[\s]{0,4}(.*)""".r
  private val DiffLineRE = """(diff --git (.+) (.+))""".r
  
  private val log = new Log
  private var currentCommit: Commit = null
  
  private var parsingDiff = false

  def extractFrom(logData: String): Log = {
    logData.lines foreach processLine
    log
  }
  
  private def processLine(line: String) = line.trim match {
    case CommitLineRE(id) => {
        parsingDiff = false
        currentCommit = new Commit(id)
        log.addCommit(currentCommit)
    }
    case AuthorLineRE(author, email) => {
        currentCommit.author = author.trim
    }
    case DateLineRE(date) => {
        val formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy Z")
        currentCommit.date = formatter.parse(date)
    }
    case DiffLineRE(message, fileA, fileB) => {
        parsingDiff = true
        currentCommit.addDiffLine(message)
    }
    case CommitMessageLineRE(message) => {
        if(!message.trim.isEmpty) {
          if(parsingDiff) {
            currentCommit.addDiffLine(message)
          } else {
            currentCommit.message += message
          }
        }
    }
  }
}
