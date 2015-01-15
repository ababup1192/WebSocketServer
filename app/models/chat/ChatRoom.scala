package models.chat

import akka.actor.Actor
import play.api.libs.iteratee.{Concurrent, Enumerator}

case object Join

case class Leave(message:String)

case class Talk(text: String)

case class Joined(enumerator: Enumerator[String])

case class ChatRoom(roomId: String) extends Actor {
  private[this] val (enumerator, channel) = Concurrent.broadcast[String]

  def receive = {
    case Join => join()
    case Leave => channel.push("")
    case Talk(text) => channel.push(text)
  }

  private def join() = {
    sender ! Joined(enumerator)
  }

}
