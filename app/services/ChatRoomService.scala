package services

import akka.actor.{ActorRef, _}
import akka.pattern.ask
import akka.util.Timeout
import models.chat._
import play.api.libs.iteratee.{Enumerator, Iteratee}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class ChatRoomServiceImpl {
  private[this] implicit val timeout = Timeout(1 second)

  def join(chatRoom: ActorRef) = (chatRoom ? Join).map {
    case Joined(enumerator) => (in(chatRoom), enumerator)
    case _ => error(chatRoom)
  }

  private def in(chatRoom: ActorRef): Iteratee[String, String] = {
    Iteratee.foreach[String](text => chatRoom ! Talk(text)).map(_ => "")
  }

  private def error(chatRoom: ActorRef) = {
    val response = ChatResponse(room = chatRoom.toString(), name = "system", text = "can not join")
    (Iteratee.ignore[String], Enumerator[String](response.toString))
  }
}

trait ChatRoomService {
  def chatRoomService = new ChatRoomServiceImpl
}
