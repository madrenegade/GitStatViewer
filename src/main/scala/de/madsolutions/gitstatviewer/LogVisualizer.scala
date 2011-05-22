/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import de.madsolutions.reports.generator.ActivityReport
import de.madsolutions.reports.generator.AuthorReport
import de.madsolutions.reports.generator.GeneralReport
import de.madsolutions.reports.generator.ReportGenerator
import java.io.File
import scala.xml.Elem
import scala.xml.XML

class LogVisualizer(stats: Elem) {
  
  private def reporters = List[ReportGenerator](
    new GeneralReport,
    new AuthorReport,
    new ActivityReport
  )

  def generateReport(outputPath: String) = {
    val dir = new File(outputPath)
    dir.mkdir
    
    val report = generatePage("GitStatViewer - Report",
                              <div>{
          reporters map {
            generator: ReportGenerator => {
              // create xhtml from this
              val dir = new File(outputPath + "/" + generator.name)
              dir.mkdir
                  
              val partialReport = <div>
                {generator.generateReport(outputPath + "/" + generator.name, stats)}
                <hr />
                <a href="../report.xhtml">Back</a>
              </div>

              XML.save(outputPath + "/" + generator.name + "/report.xhtml", generatePage(generator.name, partialReport), "UTF-8", xmlDecl = true)

              <a href={reportUrl(generator.name)}>{generator.name}</a><br />
            }
          }
        }</div>)
    
    XML.save(outputPath + "/report.xhtml", report, "UTF-8", xmlDecl = true)
  }
  
  private def reportUrl(name: String) = "./" + name + "/report.xhtml"
  
  private def generatePage(title: String, body: Elem) = {
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>{title}</title>
      </head>
      <body>
        <h1>{title}</h1>
        {body}
      </body>
    </html>
  }
}
