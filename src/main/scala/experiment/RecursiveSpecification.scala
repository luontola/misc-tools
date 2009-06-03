package experiment

import collection.mutable.{ArrayBuffer, Buffer}
import java.util.ArrayList

class RecursiveSpecification {
  private[experiment] var currentExample: Example = new Example(this, getClass.getSimpleName, Nil) in {}

  implicit def forExample(desc: String): Example = {
    println("forExample: " + desc)
    currentExample.newChildExample(desc)
  }

  def execute(targetPath: List[Int]): SpecRunResult = {
    currentExample.execute(targetPath)
  }
}

class Example(val context: RecursiveSpecification, val description: String, val currentPathReversed: List[Int]) {
  val currentPath = currentPathReversed.reverse
  var executed: Boolean = false
  var exampleBody: () => Any = null
  val childExamples: Buffer[Example] = new ArrayBuffer()

  def in(expectations: => Any): this.type = {
    exampleBody = () => expectations
    this
  }

  def newChildExample(desc: String): Example = {
    val child = new Example(context, desc, childExamples.length :: currentPathReversed)
    childExamples.append(child)
    child
  }

  def execute(targetPath: List[Int]): SpecRunResult = {
    println("execute: " + description)
    println(targetPath)
    executeThisExample()
    //    executeSelectedChildExample(targetPath)

    val currentIsLeafExample = (childExamples.length == 0)
    val firstVisitToCurrentExample = (targetPath == Nil)

    val childExampleIndexToExecute = {
      if (currentIsLeafExample) {
        None
      } else if (firstVisitToCurrentExample) {
        Some(0)
      } else {
        Some(targetPath.first)
      }
    }

    val newUnexecutedIndexes = {
      if (currentIsLeafExample) {
        Nil
      } else if (firstVisitToCurrentExample) {
        for (i <- 1 until childExamples.length) yield i
      } else {
        Nil
      }
    }
    val newUnexecutedPaths = {
      newUnexecutedIndexes.map((i) => (i :: currentPathReversed).reverse).toList
    }

    childExampleIndexToExecute match {
      case Some(i) => {
        val childResult = childExamples(i).execute(targetPath.drop(1))
        new SpecRunResult(
          currentPath :: childResult.executedPaths,
          List.concat(childResult.newUnexecutedPaths, newUnexecutedPaths)
          )
      }
      case None => {
        new SpecRunResult(List(currentPath), newUnexecutedPaths)
      }
    }
  }

  private def executeThisExample() {
    assert(exampleBody != null, "No example body; the 'in' method must be called first")
    assert(!executed, "This example has already been executed")
    executed = true
    context.currentExample = this
    exampleBody.apply()
  }

  //  private def executeSelectedChildExample(targetPath: List[Int]) {
  //    def childExampleToExecute = {
  //      if (childExamples.length == 0) {
  //        None
  //      } else if (targetPath == Nil) {
  //        Some(childExamples.first)
  //      } else {
  //        Some(childExamples(targetPath.first))
  //      }
  //    }
  //    childExampleToExecute match {
  //      case Some(example) => example.execute(targetPath.drop(1))
  //      case None => null
  //    }
  //  }
}
