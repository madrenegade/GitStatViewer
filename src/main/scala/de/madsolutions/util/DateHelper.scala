/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.util

import java.util.Date
import java.util.GregorianCalendar
import java.util.Calendar
import java.text.SimpleDateFormat

object DateHelper {
  
  def parse(s: String, fmt: String = "EEE MMM dd hh:mm:ss yyyy Z") = {
    val formatter = new SimpleDateFormat(fmt)
    formatter.parse(s)
  }
  
  def format(d: Date, fmt: String = "yyyy-MM-dd") = {
    val formatter = new SimpleDateFormat(fmt)
    formatter.format(d)
  }
  
  def timeSpanBetween(start: Date, end: Date): TimeSpan = {
    val t1 = start.getTime
    val t2 = end.getTime
    
    new TimeSpan(t2-t1)
  }
  
  def timeSpanBetweenNowAnd(date: Date) = timeSpanBetween(date, new Date())
  
  def hour(date: Date) = {
    calendar(date).get(Calendar.HOUR_OF_DAY)
  }
  
  def dayOfWeek(date: Date) = calendar(date).get(Calendar.DAY_OF_WEEK)
  
  def day(date: Date) = {
    val fmt = new SimpleDateFormat("yyyy-MM-dd")
    
    fmt.parse(fmt.format(date))
  }
  
  private def calendar(date: Date) = {
    val calendar = new GregorianCalendar
    calendar.setTime(date)
    
    calendar
  }
}
