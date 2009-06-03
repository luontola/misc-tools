package experiment

import org.specs.Specification

class RecursiveSpecificationSpec extends Specification {
  val runner = new SpecRunner()

  "Given a spec with no examples" should {
    "when it's run initially, the root example is executed" in {
      TestSpy.reset()
      runner.run(classOf[DummySpecWithoutChildExamples])
      TestSpy.get() must_== "root"
    }
  }

  "Given a spec with one child example" should {
    "when it's run initially, the child example is executed" in {
      TestSpy.reset()
      runner.run(classOf[DummySpecWithOneChildExample])
      TestSpy.get() must_== "root,a"
    }
  }

  "Given a spec with two child examples" should {
    "when it's run initially, the 1st child is executed" in {
      TestSpy.reset()
      runner.run(classOf[DummySpecWithTwoChildExamples])
      TestSpy.get() must_== "root,a"
    }
    "when the 1st child is run explicitly, the 1st child is executed" in {
      TestSpy.reset()
      runner.run(classOf[DummySpecWithTwoChildExamples], List(0))
      TestSpy.get() must_== "root,a"
    }
    "when the 2nd child is run explicitly, the 2nd child is executed" in {
      TestSpy.reset()
      runner.run(classOf[DummySpecWithTwoChildExamples], List(1))
      TestSpy.get() must_== "root,b"
    }
  }

  "Gicen a spec with nested child examples" should {
    val pathA = List(0)
    val pathAA = List(0, 0)
    val pathAB = List(0, 1)
    val pathB = List(1)
    val pathBA = List(1, 0)
    val pathBB = List(1, 1)

    "when it's run initially, the first nested leaf child example is executed" in {
      TestSpy.reset()
      runner.run(classOf[DummySpecWithNestedChildExamples])
      TestSpy.get() must_== "root,a,aa"
    }
//    "when it's run initially, a list of found unexecuted examples is returned" in {
//      TestSpy.reset()
//      val results = runner.run(classOf[DummySpecWithNestedChildExamples])
//      results.foundUnexecutedExamples must_== List(pathAB, pathB)
//    }
  }
}
