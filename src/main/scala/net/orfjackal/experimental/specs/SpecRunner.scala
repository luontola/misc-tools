package net.orfjackal.experimental.specs

class SpecRunner(listener: SpecRunListener) {
  def run[S <: RecursiveSpecification](specClass: Class[S]): SpecRunResult = {
    run(specClass, Nil)
  }

  def run[S <: RecursiveSpecification](specClass: Class[S], targetPath: List[Int]): SpecRunResult = {
    val spec = specClass.newInstance()
    spec.execute(targetPath, listener)
  }
}
