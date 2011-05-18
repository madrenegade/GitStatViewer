/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import java.util.Date
import scala.collection.mutable.ListBuffer

class Commit(val id: String) {
  var author: String = ""
  var date: Date = null
  var message: String = ""
  
  def addDiffLine(line: String) = diffBuffer += (line)
  def diff = this.diffBuffer.toList
  
  private var diffBuffer = ListBuffer[String]()
}
