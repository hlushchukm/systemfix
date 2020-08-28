package com.alsritter.services;

import com.alsritter.mappers.StudentMapper;
import com.alsritter.pojo.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author alsritter
 * @version 1.0
 **/
@Service
@Slf4j
public class StudentService {
    @Resource
    public StudentMapper studentMapper;

    public Student getStudent(){
        return studentMapper.getStudent();
    }
}