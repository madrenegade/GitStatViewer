/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.util

class TimeSpan(ms: Long) {
  
  def inMilliseconds = ms.toDouble
  
  def inSeconds = inMilliseconds / 1000.0
  def inMinutes = inSeconds / 60.0
  def inHours = inMinutes / 60.0
  def inDays = inHours / 24.0
  def inWeeks = inDays / 7.0
  
}

object TimeSpan {
  def apply(ms: Long) = new TimeSpan(ms)
}