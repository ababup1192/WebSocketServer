package services

import akka.actor._
import models.chat.ChatRoomRepository
import play.api.libs.iteratee._
import scala.concurrent._
import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global

class ChatServiceImpl extends ChatRoomRepository with ChatRoomService {
  def start() = repository.chatRoom("status").flatMap {
    case chatRoom: ActorRef => chatRoomService.join(chatRoom)
    case _ => error
  }

  private def error = future {
    val response = ChatResponse(room = "none", name = "system", text = "chat room not found")
    (Iteratee.ignore[String], Enumerator[String](response.toString))
  }
}

trait ChatService {
  def chatService = new ChatServiceImpl
}
