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
      <tr>
        <td>Total commits:</td>
        <td>{(stats \ "general" \ "numCommits").text}</td>
      </tr>
      <tr>
        <td>Authors:</td>
        <td>
          <ol>
            {
              (stats \ "general" \ "authors" \ "author") map {
                n: Node => {
                  <li>{n.text}</li>
                }
              }
            }
          </ol>
        </td>
      </tr>
      <tr>
        <td>First commit:</td>
        <td>{(stats \ "general" \ "firstCommit").text}</td>
      </tr>
      <tr>
        <td>Last commit:</td>
        <td>{(stats \ "general" \ "lastCommit").text}</td>
      </tr>
    </table>
  }
  
}
