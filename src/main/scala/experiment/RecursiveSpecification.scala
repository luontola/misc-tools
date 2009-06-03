package experiment

import collection.mutable.{ArrayBuffer, Buffer}
import java.util.ArrayList

class RecursiveSpecification {
  private[experiment] var currentExample: Example = new Example(this, getClass.getSimpleName, Nil) in {}

  implicit def forExample(desc: String): Example = {
    currentExample.newChildExample(desc)
  }

  def execute(targetPath: List[Int]): SpecRunResult = {
    currentExample.execute(targetPath)
  }
}

class Example(val context: RecursiveSpecification, val description: String, val currentPath: List[Int]) {
  var executed: Boolean = false
  var targetPath: List[Int] = null
  var exampleBody: () => Any = null
  val childExamples: Buffer[Example] = new ArrayBuffer()

  def in(expectations: => Any): this.type = {
    exampleBody = () => expectations
    this
  }

  def newChildExample(desc: String): Example = {
    val child = new Example(context, desc, currentPath ::: List(childExamples.length))
    childExamples.append(child)
    child
  }

  def execute(targetPath: List[Int]): SpecRunResult = {
    prepareForExecute(targetPath);
    val current = executeThisExample()
    val child = executeSelectedChildExample()
    mergeResults(current, child)
  }

  private def prepareForExecute(targetPath: List[Int]) {
    assert(exampleBody != null, "No example body; the 'in' method must be called first")
    assert(!executed, "This example has already been executed")
    executed = true
    this.targetPath = targetPath
    context.currentExample = this
  }

  private def executeThisExample(): SpecRunResult = {
    exampleBody.apply()
    new SpecRunResult(List(currentPath), newUnexecutedPaths);
  }

  private def executeSelectedChildExample(): Option[SpecRunResult] = {
    childIndexToExecute match {
      case Some(i) => Some(childExamples(i).execute(targetPath.drop(1)))
      case None => None
    }
  }

  private def mergeResults(current: SpecRunResult, maybeChild: Option[SpecRunResult]): SpecRunResult = {
    maybeChild match {
      case Some(child) =>
        new SpecRunResult(
          List.concat(current.executedPaths, child.executedPaths),
          List.concat(child.newUnexecutedPaths, current.newUnexecutedPaths))
      case None => current
    }
  }

  // helper methods

  private def newUnexecutedPaths = {
    val newUnexecutedIndexes = {
      if (currentIsLeafExample) {
        Nil
      } else if (firstVisitToCurrentExample) {
        for (i <- 1 until childExamples.length) yield i
      } else {
        Nil
      }
    }
    newUnexecutedIndexes.map((i) => currentPath ::: List(i)).toList
  }

  private def childIndexToExecute = {
    if (currentIsLeafExample) {
      None
    } else
    if (firstVisitToCurrentExample) {
      Some(0)
    } else {
      Some(targetPath.first)
    }
  }

  private def currentIsLeafExample = {
    assert(executed)
    childExamples.length == 0
  }

  private def firstVisitToCurrentExample = {
    assert(targetPath != null)
    targetPath == Nil
  }
}
