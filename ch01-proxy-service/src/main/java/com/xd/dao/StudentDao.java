package com.xd.dao;

import com.xd.domain.Student;

import java.util.List;

public interface StudentDao {

    Student getStudentById(int id);

    void insertStudent(Student student);

    List<Student> getStudents();
}
