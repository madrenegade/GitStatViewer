/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.util

import java.io.File
import org.jfree.chart.ChartUtilities
import org.jfree.chart.JFreeChart

object Chart {
  def save(outputPath: String, chart: JFreeChart) = {
    val filename = chart.getTitle.getText
    
    val file = new File(outputPath + "/" + filename + ".png")
    ChartUtilities.saveChartAsPNG(file, chart, 1024, 400)
    
    filename + ".png"
  }
}
