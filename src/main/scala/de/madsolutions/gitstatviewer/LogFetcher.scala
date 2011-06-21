/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import java.io.File
import scala.sys.process._

object LogFetcher {
  def fetch(directory: String): String = {
    Process("git log -p", new File(directory)) !!
  }
}
