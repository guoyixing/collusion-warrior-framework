package io.github.guoyixing.collusion.service;

import io.github.guoyixing.collusion.pojo.po.Hobby;
import io.github.guoyixing.collusion.pojo.po.Student;
import io.github.guoyixing.collusion.repository.db.HobbyRepository;
import io.github.guoyixing.collusion.repository.db.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author 敲代码的旺财
 * @date 7/7/2022 下午10:23
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 添加一个学生
     * @param student 学生信息
     * @return 学生
     */
    @Override
    public Student save(Student student) {
        student.setCreateTime(new Date());
        return studentRepository.save(student);
    }

    /**
     * 删除一个学生
     * @param id 学生id
     * @return 学生
     */
    @Override
    public void del(Long id) {
        Student student = studentRepository.findById(id).get();
        studentRepository.delete(student);
    }
}
