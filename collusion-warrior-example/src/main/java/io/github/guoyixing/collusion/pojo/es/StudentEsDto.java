package io.github.guoyixing.collusion.pojo.es;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * @author 敲代码的旺财
 * @date 2022/6/29 16:02
 */
@Getter
@Setter
@ToString
@Document(indexName = "spring-data-test-student")
public class StudentEsDto {

    @Id
    private Long id;

    private String name;

    @Field(name = "english_name")
    private String englishName;

    private Integer age;

    private String sex;

    @Field(name = "create_time", type = FieldType.Date)
    private Date createTime;
}
