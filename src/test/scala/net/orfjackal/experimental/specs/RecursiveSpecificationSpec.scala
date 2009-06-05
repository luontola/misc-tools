package net.orfjackal.experimental.specs

import org.specs._
import mock.Mockito

class RecursiveSpecificationSpec extends SpecificationWithJUnit with Mockito {
  val listener = mock[SpecRunListener]
  val runner = new SpecRunner(listener)

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

  "Given a spec with nested child examples" should {
    val root = Nil
    val pathA = List(0)
    val pathAA = List(0, 0)
    val pathAB = List(0, 1)
    val pathB = List(1)
    val pathBA = List(1, 0)
    val pathBB = List(1, 1)
    val pathBC = List(1, 2)

    "when it's run initially, the first nested leaf child example is executed" in {
      TestSpy.reset()
      runner.run(classOf[DummySpecWithNestedChildExamples])
      TestSpy.get() must_== "root,a,aa"
    }
    "when it's run initially, a list of executed examples is returned" in {
      val result = runner.run(classOf[DummySpecWithNestedChildExamples])
      result.executedPaths must_== List(root, pathA, pathAA)
    }
    "when it's run initially, a list of new unexecuted examples is returned" in {
      val result = runner.run(classOf[DummySpecWithNestedChildExamples])
      result.newUnexecutedPaths must_== List(pathAB, pathB)
    }
    "when it's run initially, the spec notifies about examples as they are run" in {
      runner.run(classOf[DummySpecWithNestedChildExamples])
      (
              listener.fireBeginExample(root) on listener) then
              (listener.fireBeginExample(pathA) on listener) then
              (listener.fireBeginExample(pathAA) on listener) then
              (listener.fireFinishExample(pathAA) on listener) then
              (listener.fireFinishExample(pathA) on listener) then
              (listener.fireFinishExample(root) on listener) were calledInOrder
      listener had noMoreCalls
    }

    "when an unexecuted leaf example is executed, a list of executed examples is returned" in {
      val result = runner.run(classOf[DummySpecWithNestedChildExamples], pathAB)
      result.executedPaths must_== List(root, pathA, pathAB)
    }
    "when an unexecuted leaf example is executed, no more new unexecuted examples are found" in {
      val result = runner.run(classOf[DummySpecWithNestedChildExamples], pathAB)
      result.newUnexecutedPaths must_== Nil
    }

    "when an unexecuted non-leaf example is executed, a list of executed examples is returned" in {
      val result = runner.run(classOf[DummySpecWithNestedChildExamples], pathB)
      result.executedPaths must_== List(root, pathB, pathBA)
    }
    "when an unexecuted non-leaf example is executed, a list of new unexecuted examples is returned" in {
      val result = runner.run(classOf[DummySpecWithNestedChildExamples], pathB)
      result.newUnexecutedPaths must_== List(pathBB, pathBC)
    }
  }
}
