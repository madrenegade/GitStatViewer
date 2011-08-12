package de.madsolutions.util
import scala.collection.mutable.HashMap
import scala.xml.Node
import de.madsolutions.gitstatviewer.Commit
import de.madsolutions.gitstatviewer.Log
import java.util.Date

object Cache {

  private final val MILLISECONDS_PER_SECOND = 1000
  private final val SECONDS_PER_MINUTE = 60
  private final val MINUTES_PER_HOUR = 60
  private final val HOURS_PER_DAY = 24

  private final val MILLISECONDS_PER_DAY = MILLISECONDS_PER_SECOND *
    SECONDS_PER_MINUTE * MINUTES_PER_HOUR * HOURS_PER_DAY

  private var commits: List[Commit] = null
  var authors: List[String] = null

  def init(log: Log) {
    commits = log.commits.sortBy(_.date)
    authors = log.authors.sorted
  }

  lazy val firstCommit = commits.head
  lazy val lastCommit = commits.last

  lazy val linesOfCode = {
    commits.foldLeft(0)((lines, commit) => lines + (commit.approximatedLines))
  }

  lazy val commitsPerDay = {
    val days = (lastCommit.date.getTime() - firstCommit.date.getTime()).toDouble / MILLISECONDS_PER_DAY.toDouble
    commits.size.toDouble / days
  }

  lazy val projectAge = DateHelper.timeSpanBetween(firstCommit.date, lastCommit.date).inDays

  lazy val commitsByWeek = {
    commits.groupBy {
      c: Commit =>
        {
          DateHelper.timeSpanBetweenNowAnd(c.date).inWeeks.toInt
        }
    }.map {
      (kv: (Int, List[Commit])) =>
        {
          (kv._1, kv._2.length)
        }
    }
  }

  lazy val commitsByDay = {
    commits.groupBy {
      c: Commit =>
        {
          DateHelper.timeSpanBetweenNowAnd(c.date).inDays.toInt
        }
    }.map {
      (kv: (Int, List[Commit])) =>
        kv._2.firstOption match {
          case Some(commit) => (DateHelper.format(commit.date), kv._2.length)
          case None => (null, kv._2.length)
        }
    }
  }

  lazy val commitsByHour = {
    commits.groupBy {
      c: Commit =>
        {
          DateHelper.hour(c.date).toInt
        }
    }.map {
      (kv: (Int, List[Commit])) =>
        {
          (kv._1, kv._2.length)
        }
    }
  }

  lazy val commitsByWeekDay = {
    commits.groupBy {
      c: Commit =>
        {
          DateHelper.dayOfWeek(c.date)
        }
    }.map {
      (kv: (Int, List[Commit])) =>
        {
          (kv._1, kv._2.length)
        }
    }
  }

  lazy val commitsByMonth = {
    commits.groupBy {
      c: Commit =>
        {
          DateHelper.monthOfYear(c.date)
        }
    }.map {
      (kv: (Int, List[Commit])) =>
        {
          (kv._1, kv._2.length)
        }
    }
  }

  lazy val commitsByAuthor = {
    commits.groupBy {
      c: Commit =>
        {
          c.author
        }
    }
  }

  def addedLinesFor(commits: List[Commit]) = {
    commits map {
      c: Commit =>
        {
          c.addedLines
        }
    } reduceLeft (_ + _)
  }

  def deletedLinesFor(commits: List[Commit]) = {
    commits map {
      c: Commit =>
        {
          c.deletedLines
        }
    } reduceLeft (_ + _)
  }
}