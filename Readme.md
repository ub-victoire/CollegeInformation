# College Information: Inheritance and Access Control in Java

This project is a practical introduction to **multilevel inheritance** and **access control** in Java. It uses a small college example to show how one object can call methods declared in several classes.

The project is a learning exercise, not a complete college management system. Its methods print fixed example values; they do not store or retrieve student records.

## Learning objectives

After working through this lesson, you should be able to:

1. Explain classes, objects, and instance methods.
2. Identify direct and indirect superclasses in an inheritance chain.
3. Use `extends` to create a subclass and call inherited methods.
4. Describe Java's four member access levels.
5. Compile and run Java classes organized in a package.
6. Recognize when inheritance is appropriate and when another relationship is a better fit.

## Before you start

You should be familiar with basic Java syntax, methods, and running commands in a terminal. You need a JDK (Java Development Kit) with `javac` and `java` available on your `PATH`.

The project uses `var` for a local variable, so **JDK 10 or later** is required. No external dependencies or build tools are needed.

Check your installation:

```bash
java -version
javac -version
```

## Project structure

```text
CollegeInformation/
├── .gitignore
├── Readme.md
└── src/
    └── org/
        └── college/
            ├── College.java
            ├── Dept.java
            ├── Hostel.java
            └── Student.java
```

Each source file starts with `package org.college;`. The package groups the classes under the same name, and the directory structure mirrors that name. Since the classes are in the same package, they can refer to each other without imports.

Java is case-sensitive: `Dept` and `dept` are different names. The public class `Dept` is declared in `Dept.java`. The method `HostelName()` also begins with a capital `H`; although `hostelName()` would be more conventional, use the spelling in the source when calling it.

## 1. Classes, objects, and methods

A **class** defines a type and the behavior its objects can have. An **object** is an instance of a class. An **instance method** is called on an object:

```java
College college = new College();
college.collegeName();
```

Here, `new College()` creates a `College` object and `collegeName()` prints a line of text. This project also shows how an object of a subclass can use methods provided by its superclasses.

## 2. The inheritance chain

Inheritance lets a subclass extend a superclass. The subclass can add behavior and use accessible inherited members.

```text
College
   ↑
Dept
   ↑
Hostel
   ↑
Student
```

Each arrow means **“extends.”** For example, `Student` extends `Hostel`, which extends `Dept`, which extends `College`.

The declarations in this project are:

```java
public class College { ... }
public class Dept extends College { ... }
public class Hostel extends Dept { ... }
public class Student extends Hostel { ... }
```

This is **multilevel inheritance**: a class extends a class that itself extends another class. `Hostel` is the direct superclass of `Student`; `Dept` and `College` are indirect superclasses, also called ancestors. A Java class has at most one direct superclass, but it can have multiple ancestors. `College` implicitly extends `java.lang.Object`.

### A modeling caveat

This chain is useful for demonstrating Java syntax, but it is not a good model for a real college: a student is not a kind of hostel, and a hostel is not a kind of department. In a real application, a student could instead *have* a department or a reference to a hostel. Choose inheritance for a genuine **“is-a”** relationship; consider composition or association for **“has-a”** relationships.

## 3. Follow the source code

All eight methods in this example are public instance methods. Each is declared in one class and prints a fixed value.

| Class | Direct superclass | Methods declared in the class |
|---|---|---|
| `College` | `Object` (implicit) | `collegeName()`, `collegeCode()`, `collegeRank()` |
| `Dept` | `College` | `deptName()` |
| `Hostel` | `Dept` | `HostelName()` |
| `Student` | `Hostel` | `studentName()`, `studentDept()`, `studentId()` |

`Student` has access to the five public methods declared in its ancestors as well as the three methods it declares itself. The source does not override any of these eight methods.

The entry point is in `Student.java`:

```java
public static void main(String[] args) {
    var student = new Student();

    student.collegeName();
    student.collegeCode();
    student.collegeRank();
    student.deptName();
    student.HostelName();
    student.studentName();
    student.studentDept();
    student.studentId();
}
```

`main` is the entry point when the class is launched. It is `static`, so it belongs to the class rather than to an individual object. The eight methods it calls are instance methods, so `main` first creates a `Student` object and calls the methods through `student`.

`var` asks the compiler to infer the local variable's type from the initializer. In this example, `student` has type `Student`; it is not dynamically typed. The declaration could also be written:

```java
Student student = new Student();
```

### Trace an inherited method

For `student.collegeName()`, the method is declared in `College`. Because `Student` extends `Hostel`, `Hostel` extends `Dept`, and `Dept` extends `College`, the public method is available through the `Student` object.

```text
Student → Hostel → Dept → College: collegeName()
```

No separate `College` object is needed for this call, and the method does not need to be copied into every subclass.

## 4. What happens when a student is created?

```java
Student student = new Student();
```

This expression creates one `Student` object. Initialization includes the superclass constructor chain, from `Object` through `College`, `Dept`, and `Hostel` to `Student`. Because these classes do not declare constructors, Java supplies a no-argument constructor for each of them.

The superclass parts are initialized as part of the same object; creating a `Student` this way does **not** create separate `College`, `Dept`, and `Hostel` objects.

Inheritance is a way to express a relationship and reuse or specialize behavior. It does **not** automatically save memory: a subclass object includes its inherited instance state, and method code is not copied into every object.

## 5. Access control

Access modifiers determine where a member can be accessed. For ordinary class members, the following table summarizes the rules, ignoring Java module restrictions:

| Member access | Declaring class | Other classes in the same package | Subclass in another package | Unrelated class in another package |
|---|---|---|---|---|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes, with restrictions | No |
| No modifier (package-private) | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

Some important details:

- **Public:** accessible wherever the declaring type and member are accessible.
- **Protected:** accessible within the package and, outside the package, from subclass code subject to restrictions. For example, an outside-package subclass cannot access a protected instance member through an arbitrary superclass instance.
- **Package-private:** selected by omitting the access modifier; it is not written using a `default` access keyword.
- **Private:** accessible within the declaring class, but a private superclass method is not inherited by a subclass. Nested classes have special access rules beyond this introductory example.

Top-level classes can be `public` or package-private; they cannot be declared `private` or `protected`.

All four project classes are in `org.college`, and their methods are public. The current program therefore illustrates public inherited access, **not** the cross-package `protected` restrictions. Changing a method to package-private would still allow access from these same-package classes; it would not, by itself, demonstrate access from another package.

### Example: package-private method

This illustrative method has no access modifier:

```java
void collegeCode() {
    System.out.println("College Code: COL001");
}
```

It is accessible to other classes in the same package, but not to a class in a different package. The actual project keeps `collegeCode()` public.

## 6. Inheritance, overriding, and interfaces

The current example **inherits** methods without changing their implementations. A subclass can also **override** an inherited instance method to provide its own implementation, provided Java's overriding rules are followed. For example, this is a separate illustration, not code currently in the project:

```java
class ExampleCollege {
    public void describe() {
        System.out.println("College");
    }
}

class ExampleStudent extends ExampleCollege {
    @Override
    public void describe() {
        System.out.println("Student");
    }
}
```

If a reference of type `ExampleCollege` refers to an `ExampleStudent` object, calling `describe()` invokes the student's override. This runtime selection of an overridden method is one form of **polymorphism**. It differs from **overloading**, where methods have the same name but different parameter lists.

Java does not allow a class to extend multiple classes, such as `class C extends A, B`. A class can implement multiple interfaces, which provide multiple types and may provide default methods. Interfaces are not needed for this exercise.

## 7. Compile and run

From the project root, check that the source files are saved, then run:

```bash
javac -encoding UTF-8 -d out src/org/college/College.java src/org/college/Dept.java src/org/college/Hostel.java src/org/college/Student.java
java -cp out org.college.Student
```

The first command compiles the source files into the `out` directory. `-encoding UTF-8` specifies the source encoding, which matters for the accented character in `Student.java`. The second command sets `out` as the classpath and starts `Student` using its fully qualified class name, `org.college.Student`.

The expected output from the current source is:

```text
College Name: Ushindi
College Code: COL001
College Rank: 10(demostration value)
Department NAme: Computer Science
Hostel Name: TestSol Residence
Student Name: Steven Ainé
Student Department: Software Engineering
Student ID: STU001
```

The capitalization and spelling above reflect the strings currently in the code. The department and student department are also different fixed demonstration values; the program does not validate their consistency.

To avoid accidentally running old class files after a failed compilation, combine the commands in a shell that supports `&&`:

```bash
javac -encoding UTF-8 -d out src/org/college/College.java src/org/college/Dept.java src/org/college/Hostel.java src/org/college/Student.java && java -cp out org.college.Student
```

The repository's `.gitignore` excludes `out/` and compiled `.class` files.

## 8. Practice exercises

Try these in a copy of the project, or restore any temporary source changes afterwards.

1. **Trace the chain:** Identify where each of the eight methods is declared. Which five are inherited by `Student`?
2. **Remove a link:** Temporarily change `Student extends Hostel` to `Student`, then compile. Which calls fail? Restore `extends Hostel` and compile again. The classes are still in the same package, but package membership alone does not give a `Student` object the methods of unrelated classes.
3. **Explore private access:** Temporarily change `College.collegeName()` from `public` to `private`, then compile. Explain the error at the call in `Student.main`. Restore `public` and verify the program runs again.
4. **Try overriding:** In a separate copy, override `collegeName()` in `Student` and print a different message. Run the program and compare the result. Add `@Override` so the compiler checks that the method overrides an inherited method.
5. **Discuss design:** Sketch a model in which a student has a department and may be assigned to a hostel. Explain why object references would better represent those relationships.

### Questions for review

1. What are the direct and indirect superclasses of `Student`?
2. Why can a `Student` object call `collegeName()`?
3. Why does `main` create an object before calling these instance methods?
4. Does `new Student()` create four separate objects?
5. What is the difference between package-private and `protected` access?
6. Why is the current inheritance chain questionable outside a teaching exercise?

### Suggested answers

1. `Hostel` is the direct superclass; `Dept` and `College` are indirect superclasses. `Object` is also an ancestor.
2. `collegeName()` is public and inherited through the superclass chain.
3. `main` is static, while the methods it calls require an instance.
4. No. It creates one `Student` object, initialized through the superclass constructor chain.
5. Package-private members are accessible within their package. Protected members are also accessible in their package and can be accessed from outside-package subclasses under additional restrictions.
6. The relationships do not describe true “is-a” relationships: a student is not a hostel, and a hostel is not a department.

## 9. Troubleshooting

| Symptom | What to check |
|---|---|
| `javac: command not found` | Install a JDK and make sure its `bin` directory is on your `PATH`. |
| `cannot find symbol: class Dept` | Check `Dept` capitalization and compile all four source files together. |
| `cannot find symbol: method hostelName()` | The method in this project is `HostelName()`, with an uppercase `H`. |
| Ancestor methods cannot be found | Check the chain: `Dept extends College`, `Hostel extends Dept`, and `Student extends Hostel`. |
| `cyclic inheritance` | Make sure no class eventually extends itself. |
| Private-access error | A private superclass method is not inherited; check the method's access modifier. |
| `non-static method ... cannot be referenced from a static context` | Call the instance method on an object, such as `student.collegeName()`. |
| `Could not find or load main class` | Compile from the project root and run `java -cp out org.college.Student`. |
| Output does not reflect an edit | Confirm compilation succeeded before running; use `&&` where supported. |
| Accented text displays incorrectly | Compile with `-encoding UTF-8` and check your terminal's character encoding. |

## Lesson checklist

- [ ] I can explain the superclass chain and trace an inherited method.
- [ ] I can compile and run the project and account for all eight output lines.
- [ ] I can distinguish public, protected, package-private, and private member access.
- [ ] I understand why the chain demonstrates inheritance but is not a good real-world domain model.
- [ ] I restored any experimental changes and confirmed the program still runs.

---

**Done by Ushindi B. Victoire**, Software Q.A Engineer | Requirement Specialist | @ub-victoire @ub-victor
