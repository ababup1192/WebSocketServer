package controllers

import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.mvc._
import services.ChatService
import scala.concurrent.ExecutionContext.Implicits.global


object Application extends Controller with ChatService {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def chat = WebSocket.tryAccept[String] { request =>
    chatService.start().mapTo[(Iteratee[String, _], Enumerator[String])].map {
      streams => Right(streams)
    }
  }

}