package com.xd.service.impl;

import com.xd.dao.StudentDao;
import com.xd.domain.Student;
import com.xd.service.StudentService;
import com.xd.util.MyBatisUtils;

import java.util.List;

/**
 * 业务层的实现类就是目标类
 */
public class StudentServiceImpl implements StudentService {

    // dao层操作绝对安全，一般写在成员变量上
    // 多态，具体的目标类不确定，只知道该目标类继承了StudentDao接口
    private StudentDao studentDao = MyBatisUtils.getSqlSession().getMapper(StudentDao.class);

    @Override
    public Student getStudentById(int id) {
        Student student = studentDao.getStudentById(id);
        return student;
    }

    @Override
    public void insertStudent(Student student) {
        studentDao.insertStudent(student);
    }

    @Override
    public List<Student> getStudents() {
        List<Student> students = studentDao.getStudents();
        return students;
    }
}
