package net.orfjackal.experimental.specs

trait SpecRunListener {
  def fireBeginExample(currentPath: List[Int])

  def fireFinishExample(currentPath: List[Int])
}
