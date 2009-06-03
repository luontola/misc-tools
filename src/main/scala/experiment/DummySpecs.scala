package experiment

class DummySpecWithoutChildExamples extends RecursiveSpecification {
  TestSpy.append("root")
}

class DummySpecWithOneChildExample extends RecursiveSpecification {
  TestSpy.append("root")
  "a" in {
    TestSpy.append(",a")
  }
}

class DummySpecWithTwoChildExamples extends RecursiveSpecification {
  TestSpy.append("root")
  "a" in {
    TestSpy.append(",a")
  }
  "b" in {
    TestSpy.append(",b")
  }
}

class DummySpecWithNestedChildExamples extends RecursiveSpecification {
  TestSpy.append("root")
  "a" in {
    TestSpy.append(",a")
    "aa" in {
      TestSpy.append(",aa")
    }
    "ab" in {
      TestSpy.append(",ab")
    }
  }
  "b" in {
    TestSpy.append(",b")
    "ba" in {
      TestSpy.append(",ba")
    }
    "bb" in {
      TestSpy.append(",bb")
    }
  }
}
