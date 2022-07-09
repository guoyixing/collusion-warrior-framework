package io.github.guoyixing.collusion.pojo.es;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Getter
@Setter
@ToString
@Document(indexName = "spring-data-test-hobby")
public class HobbyEsDto {

    @Id
    private Long id;

    private String name;

}
