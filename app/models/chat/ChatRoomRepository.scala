package models.chat

import akka.actor.{Actor, Props, _}
import akka.pattern.ask
import akka.util.Timeout
import play.api.Play.current
import play.api.libs.concurrent.Akka

import scala.concurrent.duration._
import scala.language.postfixOps

case class FindChatRoom(roomId: String)

class ChatRoomRepositoryActor extends Actor with ChatRoomFactory {
  implicit val timeout = Timeout(1 second)

  def receive = {
    case FindChatRoom(roomId) => sender ! chatRoom(roomId)
    case x => println(x.toString)
  }

  private def chatRoom(roomId: String) = context.child(roomId) match {
    case Some(room) => room
    case None => createChatRoom(roomId)
  }
}

object ChatRoomRepository {
  implicit val timeout = Timeout(1 second)
  private[this] val repository = Akka.system.actorOf(Props[ChatRoomRepositoryActor])

  def chatRoom(roomId: String) = repository ? FindChatRoom(roomId)
}

trait ChatRoomRepository {
  val repository = ChatRoomRepository
}
