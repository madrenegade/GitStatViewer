/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.madsolutions.gitstatviewer

import java.util.Date

class Commit(val id: String) {
  var author: String = ""
  var date: Date = null
  var message: String = ""
}
