package experiment

import collection.mutable.{ArrayBuffer, Buffer}
import java.util.ArrayList

class RecursiveSpecification {
  private[experiment] var currentExample: Example = new Example(this, getClass.getSimpleName, Nil) in {}

  implicit def forExample(desc: String): Example = {
    println("forExample: " + desc)
    currentExample.newChildExample(desc)
  }

  def execute(targetPath: List[Int]): SpecResults = {
    currentExample.execute(targetPath)
  }
}

class Example(val context: RecursiveSpecification, val description: String, val reversePath: List[Int]) {
  var executed: Boolean = false
  var exampleBody: () => Any = null
  val childExamples: Buffer[Example] = new ArrayBuffer()

  def in(expectations: => Any): this.type = {
    exampleBody = () => expectations
    this
  }

  def newChildExample(desc: String): Example = {
    val child = new Example(context, desc, childExamples.length :: reversePath)
    childExamples.append(child)
    child
  }

  def execute(targetPath: List[Int]): SpecResults = {
    println("execute: " + description)
    println(targetPath)
    executeThisExample()
    executeSelectedChildExample(targetPath)
    new SpecResults(Nil)
  }

  private def executeThisExample() {
    assert(exampleBody != null, "No example body; the 'in' method must be called first")
    assert(!executed, "This example has already been executed")
    executed = true
    context.currentExample = this
    exampleBody.apply()
  }

  private def executeSelectedChildExample(targetPath: List[Int]) {
    def childExampleToExecute = {
      if (childExamples.length == 0) {
        None
      } else if (targetPath == Nil) {
        Some(childExamples.first)
      } else {
        Some(childExamples(targetPath.first))
      }
    }
    childExampleToExecute match {
      case Some(example) => example.execute(targetPath.drop(1))
      case None => null
    }
  }
}
