package counter

import javax.inject.Singleton
import monix.execution.atomic._

@Singleton
class Counter {
  val value: Atomic[Int] = Atomic(0)
}
