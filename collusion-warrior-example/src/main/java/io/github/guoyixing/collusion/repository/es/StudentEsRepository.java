package io.github.guoyixing.collusion.repository.es;

import io.github.guoyixing.collusion.pojo.es.StudentEsDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 敲代码的旺财
 * @date 2022/6/29 16:17
 */
@Repository
public interface StudentEsRepository extends ElasticsearchRepository<StudentEsDto, Long> {
    List<StudentEsDto> getByName(String name);

    List<StudentEsDto> getByNameLike(String name);
}
