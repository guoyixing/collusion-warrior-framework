package io.github.guoyixing.collusion.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class StudentAndHobbyBo {

    private Long id;

    private String name;

    private String englishName;

    private Integer age;

    private String sex;

    private Date createTime;

    private List<String> hobby;

}
