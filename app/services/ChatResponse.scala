package services

import play.api.libs.json.Json

case class ChatResponse(room: String, name: String, text: String) {
  override def toString = {
    Json.toJson(Map("room" -> room, "name" -> name, "text" -> text)).toString()
  }
}