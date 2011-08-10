/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.reports.generator

import scala.xml.Elem
import scala.xml.Node

class GeneralReport extends ReportGenerator {

  def name = "General"

  def generateReport(outputPath: String, stats: Elem): Elem = {
    <table>
      {
        reportData(stats) map {
          kv: (String, Node) =>
            {
              <tr>
                <td>{ kv._1 }:</td>
                <td>{ kv._2 }</td>
              </tr>
            }
        }
      }
    </table>
  }

  private def reportData(stats: Elem) = Map[String, Node](
    "Authors" -> <ol>
                   {
                     (stats \ "general" \ "authors" \ "author") map {
                       author: Node =>
                         {
                           <li>{ author.text }</li>
                         }
                     }
                   }
                 </ol>,
    "Project age" -> (stats \ "general" \ "projectAge").head,
    "Total commits" -> (stats \ "general" \ "numCommits").head,
    "First commit" -> (stats \ "general" \ "firstCommit").head,
    "Last commit" -> (stats \ "general" \ "lastCommit").head,
    "Lines of code" -> (stats \ "general" \ "linesOfCode").head,
    "Commits per day" -> (stats \ "general" \ "commitsPerDay").head)
}
