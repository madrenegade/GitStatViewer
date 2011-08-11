/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import de.madsolutions.reports.generator.ReportGenerator
import scala.xml.Elem
import scala.xml.XML
import scala.xml.EntityRef
import scala.xml.dtd.DocType
import scala.xml.dtd.SystemID
import org.fusesource.scalate._
import java.io.File
import javax.imageio.ImageIO

class LogVisualizer(stats: Elem) {

  private val indexFile = "index.html"
  private val backHref = "./" + indexFile

  private val engine = new TemplateEngine

  private var outputPath: String = ""

  protected val reporters: List[ReportGenerator] = List()

  def generateReport(outputPath: String) = {
    this.outputPath = outputPath

    val dir = new File(outputPath)
    dir.mkdir

    val iconPath = outputPath + "/icons"
    val iconDir = new File(iconPath)
    iconDir.mkdir

    reporters.par foreach {
      generator: ReportGenerator =>
        {
          println("REPORT " + generator.name)
          val start = System.currentTimeMillis
          val partialReport = <div>
                                { generator.generateReport(outputPath + "/", stats) }
                                <hr/>
                                <a href={ backHref }>Back</a>
                              </div>

          copyIcon(generator)
          XML.save(outputPath + "/" + generator.name + ".html", generatePage(generator.name, partialReport), "UTF-8", xmlDecl = true)
          println("TIME: " + (System.currentTimeMillis - start))
        }
    }
    
    val report = generatePage("GitStatViewer - Report", <div></div>)

    XML.save(outputPath + "/" + indexFile, report, "UTF-8", xmlDecl = false, new DocType("html", SystemID("about:legacy-compat"), Nil))
  }

  private def reportUrl(name: String) = "./" + name + ".html"

  private def generatePage(title: String, body: Elem) = {
    val templateSource = TemplateSource.fromFile("template.mustache")

    val attributes = Map(
      "menu" -> menu,
      "title" -> title,
      "body" -> body)

    XML.loadString(engine.layout(templateSource, attributes))
  }

  private lazy val menu = reporters map {
    generator: ReportGenerator =>
      {
        <div class={ styleClass }>
          <a href={ reportUrl(generator.name) }>
            <p>
              <img style="height: 48px" src={ generatorIcon(generator) } alt={ generator.name } title={ generator.name }/>
            </p>
            <p>{ generator.name }</p>
          </a>
        </div>
      }
  }

  private lazy val styleClass = reporters.size match {
    case 1 => "dp100"
    case 2 => "dp50"
    case 3 => "dp33"
    case 4 => "dp25"
    case 5 => "dp20"
    case _ => ""
  }

  private def copyIcon(generator: ReportGenerator) {
    val url = this.getClass().getClassLoader().getResource(generator.name + ".png")

    if (url == null) return

    val img = ImageIO.read(url)

    ImageIO.write(img, "png", new File(outputPath + "/" + generatorIcon(generator)))
  }

  private def generatorIcon(generator: ReportGenerator) = "icons/" + generator.name + ".png"
}