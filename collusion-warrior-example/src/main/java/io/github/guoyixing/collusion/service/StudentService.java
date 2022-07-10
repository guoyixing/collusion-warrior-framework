package io.github.guoyixing.collusion.service;

import io.github.guoyixing.collusion.pojo.po.Student;

/**
 * @author 敲代码的旺财
 * @date 7/7/2022 下午10:07
 */
public interface StudentService {
    /**
     * 添加一个学生
     * @param student 学生信息
     * @return 学生
     */
    Student save(Student student);

    /**
     * 删除一个学生
     * @param id 学生id
     */
    void del(Long id);
}
