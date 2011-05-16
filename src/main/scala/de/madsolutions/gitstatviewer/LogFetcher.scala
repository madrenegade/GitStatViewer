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
    Source.fromInputStream(process.getInputStream).toStream.mkString
  }
}
