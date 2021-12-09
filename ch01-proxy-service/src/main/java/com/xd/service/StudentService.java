package com.xd.service;

import com.xd.domain.Student;

import java.util.List;

public interface StudentService {

    Student getStudentById(int id);

    void insertStudent(Student student);

    List<Student> getStudents();
}
