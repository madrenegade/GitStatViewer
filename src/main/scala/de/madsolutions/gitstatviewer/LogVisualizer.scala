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

class LogVisualizer(stats: Elem) {
  
  private val indexFile = "report.html"
  private val backHref = "../" + indexFile
  
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
    <html lang="en">
      <head>
        <title>{title}</title>
        <style type="text/css">
        	html, body, div, p {{
        		margin: 0;
        		padding: 0;
        		border: 0;
        	}}

        	.main {{
        		margin:0 auto; width:85%;
        	}}

        	.dp20, .dp25, .dp33, .dp50, .dp100 {{
        		float:left; display: inline; *margin-left:-0.04em;
        	}}

        	.dp20{{width:20%;}}
        	.dp25{{width:25%;}}
        	.dp33{{width:33.33%;}}
        	.dp50{{width:50%;}}
        	.dp100{{width:100%;}}
        	.clear{{ clear:both;}}
        </style>
      </head>
      <body>
        <div class="main">
        	<div class="dp100"><h1>{title}</h1></div>
        	<div class="dp100">{body}</div>
        	<div class="dp100">
        		<h3>
        			<span style="font-style: italic">GitStatViewer</span>
        			{EntityRef("#169")} 2011 Stefan Bradl
        		</h3>
        	</div>
        </div>
      </body>
    </html>
  }
}