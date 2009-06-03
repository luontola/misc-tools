package experiment

class SpecRunner {
  def run[S <: RecursiveSpecification](specClass: Class[S]): SpecResults = {
    run(specClass, Nil)
  }

  def run[S <: RecursiveSpecification](specClass: Class[S], targetPath: List[Int]): SpecResults = {
    val spec = specClass.newInstance()
    spec.execute(targetPath)
  }
}
