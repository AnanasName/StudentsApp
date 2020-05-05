package com.example.studentsapp;

import model.Student;

public class StudentItem {
    Student student;

    public StudentItem(Student student) {
        this.student = student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
