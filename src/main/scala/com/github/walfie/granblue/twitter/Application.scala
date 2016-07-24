package com.github.walfie.granblue.twitter

import twitter4j._
import twitter4j.conf.ConfigurationBuilder

// TODO: Make this not so bad
object Application {

  class StatusListener(twitter: Twitter) extends StatusAdapter {
    val replacements = Seq(
      "I need backup!" -> "参加者募集！",
      "Battle ID: " -> "参戦ID：",
      "Lvl " -> "Lv",
      "Qinglong" -> "青竜",
      "Xuanwu" -> "玄武",
      "Baihu" -> "白虎",
      "Zhuque" -> "朱雀",
      "Agni" -> "アグニス",
      "Neptune" -> "ネプチューン",
      "Titan" -> "ティターン",
      "Zephyrus" -> "ゼピュロス",
      "Revenge of Bonito" -> "モドリカツウォヌス",
      "Bonito" -> "カツウォヌス",
      "Gugalanna" -> "グガランナ"
    )

    override def onStatus(status: Status): Unit = {
      val original = status.getText

      val replaced = replacements.foldLeft(original) {
        case (acc, (before, after)) => acc.replaceAll(before, after)
      }

      if (original != replaced) {
        twitter.updateStatus(new StatusUpdate(replaced))
        twitter.destroyStatus(status.getId)
      }
    }
  }


  def main(args: Array[String]): Unit = {
    val twitter = TwitterFactory.getSingleton
    val twitterStream = TwitterStreamFactory.getSingleton
    val listener = new StatusListener(twitter)

    twitterStream.addListener(listener)
    twitterStream.user()
  }
}

