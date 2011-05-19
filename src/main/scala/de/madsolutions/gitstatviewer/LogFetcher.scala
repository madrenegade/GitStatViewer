/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import java.io.File
import scala.io.Source

object LogFetcher {
  def fetch(directory: String): String = {
    val process = Runtime.getRuntime.exec("git log -p", Array[String](), new File(directory))
    val is = process.getInputStream
    
    val builder = StringBuilder.newBuilder
    
    var i = 0
    
    while(i != -1) {
      i = is.read
      builder.append(i.toChar)
    }
    
    builder.mkString
  }
}
