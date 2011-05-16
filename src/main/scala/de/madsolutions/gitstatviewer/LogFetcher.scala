/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import java.io.File
import scala.io.Source

object LogFetcher {
  def fetch(directory: String): Option[String] = {
    val process = Runtime.getRuntime.exec("git log", Array[String](), new File(directory))
    
    val log = process.waitFor match {
      case 0 => {
          val stream = process.getInputStream
          Some(Source.fromInputStream(stream).toStream.mkString)
      }
      case _ => None
    }
    
    log
  }
}
