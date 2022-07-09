package io.github.guoyixing.collusion.repository.db;

import io.github.guoyixing.collusion.pojo.po.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 敲代码的旺财
 * @date 7/7/2022 下午10:55
 */
@Repository
public interface HobbyRepository extends JpaRepository<Hobby, Long> {
}
