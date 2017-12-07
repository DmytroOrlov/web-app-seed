package it

import java.net.URI
import java.util.UUID
import java.util.concurrent.TimeoutException

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, MustMatchers}
import play.api.libs.ws.ahc.AhcWSClient
import play.api.test.Helpers._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.sys.process._

class ServiceRecoveryTest extends FlatSpec with ScalaFutures with Eventually with TypeCheckedTripleEquals with MustMatchers with BeforeAndAfterAll with com.typesafe.scalalogging.StrictLogging {
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(60.seconds, 1.second)

  implicit val system: ActorSystem = ActorSystem(getClass.getSimpleName)
  implicit val mat: ActorMaterializer = ActorMaterializer()
  val ws = AhcWSClient()

  override protected def afterAll(): Unit = {
    ws.close()
    mat.shutdown()
    system.terminate()
    s"helm delete --purge $uuid".!
    s"kubectl delete namespace $uuid".!
  }

  val uuid = UUID.randomUUID()
  val kubectl = s"kubectl --namespace=$uuid"
  lazy val kubeIp: String = {
    val url = "kubectl config view -o jsonpath={.clusters[0].cluster.server}".!!
    new URI(url.trim).getHost
  }

  def port(service: String): String = s"$kubectl get services/$service --output=jsonpath={.spec.ports[0].nodePort}".!!.trim

  def serviceUrl(service: String) = s"http://$kubeIp:${port(service)}/"

  "helm" should "be available" in {
    eventually {
      "helm ls".! must ===(0)
    }
    s"helm install --set secret_key=IT_TEST --name=$uuid --namespace=$uuid src/it/resources/web".! must ===(0)
  }
  "health" should "decay and recover" in {
    val requestTimeout = 1.second
    val request = ws.url(serviceUrl("web") + "health").withRequestTimeout(requestTimeout)
    eventually {
      request.get.futureValue.status must ===(OK)
    }
    logger.info("service started")
    eventually {
      a[TimeoutException] mustBe thrownBy(Await.result(request.get, requestTimeout))
    }
    logger.info("service decayed")
    eventually {
      request.get.futureValue.status must ===(OK)
    }
  }
}
