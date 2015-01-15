package models.chat

import akka.actor.{Actor, Props}

trait ChatRoomFactory {
  this: Actor =>
  def createChatRoom(roomId: String) = context.actorOf(Props(classOf[ChatRoom], roomId), roomId)

}
