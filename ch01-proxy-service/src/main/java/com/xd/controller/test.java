package com.xd.controller;

import com.xd.domain.Student;
import com.xd.service.StudentService;
import com.xd.service.impl.StudentServiceImpl;
import com.xd.util.ServiceFactory;

import java.util.List;

// controller层，顶层控制器不干活，都是调业务层对象来干活
public class test {
    public static void main(String[] args) {
        // 创建业务层对象，通
        /*StudentService ss = new StudentServiceImpl();*/
        StudentService ss = (StudentService) ServiceFactory.getService(new StudentServiceImpl());

        /*Student s = new Student();
        s.setId(201);
        s.setName("万里花");
        s.setEmail("wanlihua@qq.com");
        s.setAge(20);
        ss.insertStudent(s);*/

        /*Student student = service.getStudentById(101);
        System.out.println(student);*/

        List<Student> students = ss.getStudents();
        for(Student s : students){
            System.out.println(s);
        }

    }
}
