/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object LogFetcher {
  def fetch(directory: String): String = {
    val process = Runtime.getRuntime.exec("git log -p", Array[String](), new File(directory))
    val inputStream = process.getInputStream
    val bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
    
    val builder = StringBuilder.newBuilder
    
    var line: String = null
    
    do
    {
      line = bufferedReader.readLine
      builder.append(line + "\n")
    } while(line != null)
    
    builder.mkString
  }
}
