package io.github.guoyixing.collusion.controller;

import io.github.guoyixing.collusion.pojo.po.Student;
import io.github.guoyixing.collusion.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 敲代码的旺财
 * @date 7/7/2022 下午10:24
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public Student save(@RequestBody Student student) {
        return studentService.save(student);
    }

    @DeleteMapping("/{id}")
    public void del(@PathVariable("id") Long id) {
        studentService.del(id);
    }

}
