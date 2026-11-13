package org.college;

public class Student extends Hostel{

public void studentName(){
    System.out.println("Student Name: Steven Ainé");
    }

    public void studentDept(){
        System.out.println("Student Department: Software Engineering");
    }

    public void studentId(){
        System.out.println("Student ID: STU001");
    }

    public static void main(String[] args){
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
