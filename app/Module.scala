import javax.inject.Singleton

import com.google.inject.{AbstractModule, Provides}
import monix.execution.Scheduler

import scala.concurrent.ExecutionContext

class Module extends AbstractModule {
  override def configure(): Unit = ()

  @Singleton
  @Provides
  def scheduler(ec: ExecutionContext): Scheduler = Scheduler(ec)
}
