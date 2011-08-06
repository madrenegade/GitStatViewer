/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import de.madsolutions.reports.generator.ReportGenerator
import java.io.File
import scala.xml.Elem
import scala.xml.XML
import scala.xml.EntityRef
import scala.xml.dtd.DocType
import scala.xml.dtd.SystemID

import org.fusesource.scalate._

class LogVisualizer(stats: Elem) {
  
  private val indexFile = "report.html"
  private val backHref = "../" + indexFile
  
  private val engine = new TemplateEngine
  
  protected val reporters: List[ReportGenerator] = List()

  def generateReport(outputPath: String) = {
    val dir = new File(outputPath)
    dir.mkdir

    val report = generatePage("GitStatViewer - Report",
                              <div>{
          reporters map {
            generator: ReportGenerator => {
              val dir = new File(outputPath + "/" + generator.name)
              dir.mkdir

              val partialReport = <div>
                {generator.generateReport(outputPath + "/" + generator.name, stats)}
                <hr />
                <a href={backHref}>Back</a>
              </div>

              XML.save(outputPath + "/" + generator.name + "/" + indexFile, generatePage(generator.name, partialReport), "UTF-8", xmlDecl = true)

              <a href={reportUrl(generator.name)}>{generator.name}</a><br />
            }
          }
        }</div>)

    XML.save(outputPath + "/" + indexFile, report, "UTF-8", xmlDecl = false, new DocType("html", SystemID("about:legacy-compat"), Nil))
  }

  private def reportUrl(name: String) = "./" + name + "/" + indexFile

  private def generatePage(title: String, body: Elem) = {
    val templateSource = TemplateSource.fromFile("template.mustache")
    
    val attributes = Map(
        "title" -> title,
        "body" -> body
    )

    XML.loadString(engine.layout(templateSource, attributes))
  }
}