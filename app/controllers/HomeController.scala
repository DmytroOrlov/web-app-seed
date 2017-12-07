package controllers

import javax.inject._

import counter.Counter
import monix.execution.{FutureUtils, Scheduler}
import play.api._
import play.api.mvc._

import scala.concurrent.duration._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(c: Counter, cc: ControllerComponents)(implicit scheduler: Scheduler) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  val health: Action[AnyContent] = Action.async(
    FutureUtils.delayedResult(c.value.getAndTransform(_ + 200).millis) {
      Ok("{}")
    }
  )
}
