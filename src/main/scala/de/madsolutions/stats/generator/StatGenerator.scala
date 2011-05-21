/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.stats.generator

import de.madsolutions.gitstatviewer.Log
import scala.xml.Elem

trait StatGenerator {
  def analyze(log: Log): Elem
  
  def name: String
}
