package io.github.guoyixing.collusion.repository.db;

import io.github.guoyixing.collusion.core.EsSyncRepository;
import io.github.guoyixing.collusion.pojo.po.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 敲代码的旺财
 * @date 2022/7/7 17:36
 */
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> , EsSyncRepository<Student,Long> {
}
