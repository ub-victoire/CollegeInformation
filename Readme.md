# Day 2 — Inheritance and access control
## Task 1: CollegeInformation

## 1. Understand what has changed

In Day 1, you created separate objects to call methods from separate classes:

```java
var college = new College();
college.collegeName();
```

For this task, we will connect classes using `extends`. A `Student` object will be able to call methods inherited through its superclass chain, as well as its own methods.

The assignment requires:

| Class name as supplied | Methods as supplied |
|---|---|
| `College` | `collegeName()`, `collegeCode()`, `collegeRank()` |
| `Student` | `studentName()`, `studentDept()`, `studentId()` |
| `Hostel` | `HostelName()` |
| `dept` | `deptName()` |

That is **four classes and eight instance methods**.

### Naming decision

The supplied names `dept` and `HostelName()` conflict with standard Java conventions:

- A conventional class name would be `Dept` or `Department`.
- A conventional method name would be `hostelName()`.

For this submission, we will preserve **`dept`** and **`HostelName()`** exactly to match the stated requirements. Those spellings are legal Java, just nonstandard. Use `dept.java` for the public class `dept`.

If your instructor confirms that conventional renaming is preferred, rename the declarations, filenames where applicable, and all references together. Do not mix the two spellings.

## 2. Choose the multilevel chain

The task does not specify which of the three non-Student classes should be the direct parent of which. We will use this explicit assumption:

```text
College
   ↓
dept
   ↓
Hostel
   ↓
Student
```

Here, an arrow means **“is extended by.”** In code:

```java
public class dept extends College
public class Hostel extends dept
public class Student extends Hostel
```

`Student` is the final subclass. It inherits **five required methods** through its superclass chain and declares **three**, for **eight total required methods**.

The inherited methods are:

- From `College`: three college methods.
- From `dept`: one department method.
- From `Hostel`: one hostel method.

### Important design caveat

This chain is an **academic exercise**, not a good real-world domain model. A student is not a type of hostel, and a hostel is not a type of department. In production design, these relationships would normally be represented through object references or composition/association rather than this inheritance chain.

We use this chain only because the question explicitly asks for multilevel inheritance across these four classes. If the instructor supplies a required chain, follow that instead.

## 3. Correct the key lesson misconceptions

### Inheritance is not a heap-memory optimization

Objects are conceptually allocated on the JVM heap, although runtime optimizations may eliminate some allocations. Inheritance does **not** automatically reduce object memory.

A subclass object includes the instance state defined by its superclass hierarchy. In our example, the call:

```java
var student = new Student();
```

creates one `Student` instance. It does not create four separate class instances merely because the inheritance chain contains four classes. Superclass constructors participate in initialization, but that does not mean separate parent objects are created.

Method code is not copied into every instance. The important lesson is **reuse and specialization**, not “inheritance saves RAM.”

### A subclass extends one direct superclass

Java classes have a single direct superclass, except `Object`, which has none. A class can have several ancestors through a chain.

The more accurate description of multilevel inheritance is:

> A subclass extends another class that itself extends another class.

It is not several parents accessing a child.

### Multiple class inheritance versus interfaces

Java does not permit a class declaration such as `class C extends A, B`. It does allow a class to implement multiple interfaces. Interfaces provide multiple inheritance of type and can also supply default behavior; conflicting inherited defaults may require an explicit override.

Keep interfaces for a later lesson. This task needs only `extends` between classes.

## 4. Create the project

From your previous `NetworkInformation` project directory:

```bash
cd ..
pwd
mkdir -p CollegeInformation/src/org/college CollegeInformation/.vscode
cd CollegeInformation
code .
```

If your terminal is elsewhere, navigate to the directory where you keep the exercise repositories first.

Source structure:

```text
CollegeInformation/
├── .gitignore
├── README.md
├── .vscode/
│   └── settings.json
└── src/
    └── org/
        └── college/
            ├── College.java
            ├── dept.java
            ├── Hostel.java
            └── Student.java
```

All four classes belong to `org.college`, so they need no imports for one another.

### VS Code settings

Create `.vscode/settings.json`:

```json
{
    "java.project.sourcePaths": ["src"],
    "java.project.outputPath": "out"
}
```

### Ignore generated files

Create `.gitignore`:

```gitignore
*.class
out/
bin/
target/
*~
*.java.save
*.log
```

Keep using your installed JDK 25. No build tool or extra library is needed.

## 5. Implement the chain from top to bottom

All displayed names, codes, and ranks below are fictional demonstration data.

### College.java — the first class

Location: `src/org/college/College.java`

```java
package org.college;

public class College {

    public void collegeName() {
        System.out.println("College Name: Example College");
    }

    public void collegeCode() {
        System.out.println("College Code: COL001");
    }

    public void collegeRank() {
        System.out.println("College Rank: 10 (demonstration value)");
    }
}
```

Although we write no `extends` clause here, `College` implicitly extends `java.lang.Object`.

### dept.java — extends College

Location: `src/org/college/dept.java`

```java
package org.college;

public class dept extends College {

    public void deptName() {
        System.out.println("Department Name: Computer Science");
    }
}
```

`dept` inherits the three accessible college methods and adds `deptName()`. Do not copy the college methods into this file.

### Hostel.java — extends dept

Location: `src/org/college/Hostel.java`

```java
package org.college;

public class Hostel extends dept {

    public void HostelName() {
        System.out.println("Hostel Name: Example Residence");
    }
}
```

`Hostel` inherits the department and college methods, then adds `HostelName()`.

### Student.java — extends Hostel and contains main

Location: `src/org/college/Student.java`

```java
package org.college;

public class Student extends Hostel {

    public void studentName() {
        System.out.println("Student Name: Alex Morgan");
    }

    public void studentDept() {
        System.out.println("Student Department: Computer Science");
    }

    public void studentId() {
        System.out.println("Student ID: STU001");
    }

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
}
```

This creates **one explicit object** and makes **eight method calls** through it.

Do not create separate `College`, `dept`, and `Hostel` objects just to call their methods here. Separate objects would not demonstrate the requested inherited access through `Student`.

## 6. Trace a call through the chain

Consider:

```java
student.collegeName();
```

For this example, the method definition comes from `College`, reached through the inheritance hierarchy:

```text
Student -> Hostel -> dept -> College
```

There is no need to redeclare `collegeName()` in every subclass. There is also no overriding in this exercise: each required method is declared only once.

The reference variable `student` has inferred type `Student`. Its accessible methods include the inherited public instance methods and its own public instance methods.

## 7. Access control: the beginner's accurate table

The usual Java term is **access modifier**. Package-private access is selected by writing no access modifier.

For ordinary class members, ignoring module-system restrictions:

| Access | Declaring class | Other classes in same package | Subclass in another package | Unrelated class in another package |
|---|---|---|---|---|
| `public` | Yes | Yes | Yes | Yes |
| `protected` | Yes | Yes | Yes, with restrictions | No |
| No modifier: package-private | Yes | Yes | No | No |
| `private` | Yes | No | No | No |

For `private`, nested classes belonging to the same enclosing top-level class have special access; that is outside this exercise.

### Important details

- `default` is **not** the access keyword you write for a package-private method. You omit the modifier.
- Ordinary top-level classes can be `public` or package-private, not `private` or `protected`.
- A private superclass method is not inherited by a subclass.
- Outside the declaring package, protected instance-member access is available through subclass code but is restricted: a subclass cannot freely access that member on an arbitrary superclass instance.
- All four classes here share a package. Consequently, a package-private or protected version of these methods could still be accessible here; this setup alone does not demonstrate the cross-package protected rule.

For the submitted solution, we keep the required methods public to make the inheritance behavior clear.

### A tiny package-private example

```java
void collegeCode() {
    System.out.println("College Code: COL001");
}
```

There is no access modifier before `void`, so this method is package-private. Do not apply this variation unless you are deliberately experimenting; keep the main solution above consistent.

## 8. Compile and run

Save all four Java files. From the `CollegeInformation` project root:

```bash
javac --release 25 -d out src/org/college/*.java
java -cp out org.college.Student
```

Use the class name `org.college.Student` in the launch command so you test the compiled bytecode under `out`, not the source-file launch workflow.

Expected output:

```text
College Name: Example College
College Code: COL001
College Rank: 10 (demonstration value)
Department Name: Computer Science
Hostel Name: Example Residence
Student Name: Alex Morgan
Student Department: Computer Science
Student ID: STU001
```

For later edits, compile and run only on successful compilation:

```bash
javac --release 25 -d out src/org/college/*.java && java -cp out org.college.Student
```

## 9. Test your understanding with one controlled change

Before your final commit:

1. Temporarily change `Student extends Hostel` to just `Student`.
2. Recompile.
3. Observe that the calls to the five ancestor methods no longer resolve.
4. Restore `extends Hostel`.
5. Recompile and run successfully.

This shows that the inherited method access comes from the superclass chain, not merely from sharing a package.

Do not submit the intentionally broken experiment.

## 10. Add a README

Create `README.md`:

````markdown
# CollegeInformation

Day 2 — Multilevel inheritance and access control.

## Objective

Use one Student object to call the required methods declared in
College, dept, Hostel, and Student.

## Inheritance chain

College -> dept -> Hostel -> Student

Arrows mean "is extended by." This order is an assumption because
the task does not specify the intermediate superclass order.

This is an inheritance exercise, not a recommended real-world domain model.

## Naming

The names dept and HostelName() intentionally match the assignment.
Conventional alternatives would be Department and hostelName().

## Requirements

JDK 25. No external dependencies.

## Compile and run

From the project root:

```bash
javac --release 25 -d out src/org/college/*.java
java -cp out org.college.Student
```

## Concepts practiced

- Multilevel class inheritance using extends
- Direct and indirect superclasses
- Calling inherited instance methods through a subclass object
- Public, protected, package-private, and private access distinctions

All printed details are fictional demonstration data.
````

## 11. Commit and publish separately

Check repository boundaries first:

```bash
git rev-parse --show-toplevel
```

If it reports **not a git repository**, initialize:

```bash
git init -b main
```

If it prints this project's directory, skip initialization. If it prints a parent directory, resolve the layout before creating a nested repository.

Stage and review:

```bash
git add src/org/college/College.java src/org/college/dept.java src/org/college/Hostel.java src/org/college/Student.java .vscode/settings.json .gitignore README.md
git diff --cached --stat
git diff --cached
```

Then commit:

```bash
git commit -m "Complete CollegeInformation multilevel inheritance exercise"
```

Create an empty GitHub repository named `CollegeInformation`. If using the same account as your earlier projects, and no origin exists yet:

```bash
git remote add origin https://github.com/ub-victoire/CollegeInformation.git
git push -u origin main
git status
```

Use the actual repository URL if it differs. Verify the hosted files before submitting.

## 12. Troubleshooting

| Error or symptom | Check |
|---|---|
| `cannot find symbol: class dept` | Use lowercase `dept` consistently and filename `dept.java`. |
| `cannot find symbol: method hostelName()` | The assignment-preserving method is `HostelName()` with uppercase H. |
| Ancestor methods cannot be found | Check every link: `dept extends College`, `Hostel extends dept`, `Student extends Hostel`. |
| `cyclic inheritance` | A class must not eventually extend itself. Check that the chain is one-way. |
| Private-access error | A private superclass method cannot be invoked as an inherited method from the subclass. |
| `non-static method ... cannot be referenced from a static context` | Call through `student`, not directly from static main. |
| Output appears unchanged after a failed compile | Do not run stale output; use the compile-and-run command joined with `&&`. |

## 13. Submission checklist

- [ ] Four classes belong to `org.college`.
- [ ] Class and method spellings match the assignment-preserving solution.
- [ ] The inheritance chain has four levels ending at `Student`.
- [ ] Methods are declared in their required classes, not copied into `Student`.
- [ ] `Student.main` creates one Student object.
- [ ] All eight required methods are called through that object.
- [ ] Compilation succeeds and execution prints eight lines.
- [ ] README states the chain assumption and naming exceptions.
- [ ] Generated files and backups are not committed.
- [ ] Source and documentation are pushed to the repository.

## 14. Explain these before moving on

1. What is Student's direct superclass? What are its other ancestors?
2. How can Student call collegeName() without declaring it?
3. Does new Student() create a separate College object too?
4. Why is inheritance not automatically a memory-saving technique?
5. Why is this chain acceptable as an exercise but questionable as a real-world model?

**Next checkpoint:** share all four source files and the output from `java -cp out org.college.Student`. The output demonstrates behavior; the source is necessary to verify the inheritance structure.
