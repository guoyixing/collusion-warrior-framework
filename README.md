# 双写同调战士
SpringBootJpa的一个插件，可以在jpa调用save、delete方法的时候，自动操作elasticsearch同步数据，对代码几乎没有入侵

## 使用说明
### 1.配置es连接
```java
@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("你的elasticsearch地址")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

}
```

### 2.jpa的repository接口集成EsSyncRepository
例如
```java
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> , EsSyncRepository<Student,Long> {
    
}
```

### 3.添加jpa数据库领域对象的事件通知
后续希望能自动生成，有对这方面技术了解的能联系我一下吗<br/>
例如
```java
@Getter
@Setter
@ToString
@Entity
@Table(name = "student")
@EsEntity(StudentEsDto.class)
public class Student {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   /* 省略一些业务字段 */

   // 使用集合类注册事件列表
   @DomainEvents
   Collection<Object> domainEvents(){
      List<Object> events= new ArrayList<>();
      events.add(this);
      return events;
   }

   //所有事件发布完成后调用，一般用来清空事件列表
   @AfterDomainEventPublication
   void callbackMethod() {
      domainEvents().clear();
   }

}
```


### 4.（可选）自定义es的领域对象，默认会生成一个数据库领域对象完全一样的es领域对象
1. 在数据库领域对象上添加`@EsEntity`注解，并指明es的领域对象
    ```java
    @Getter
    @Setter
    @ToString
    @Entity
    @Table(name = "student")
    @EsEntity(StudentEsDto.class)
    public class Student {
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
        /* 省略一些业务字段 */
    
        // 使用集合类注册事件列表
        @DomainEvents
        Collection<Object> domainEvents(){
            List<Object> events= new ArrayList<>();
            events.add(this);
            return events;
        }
    
        //所有事件发布完成后调用，一般用来清空事件列表
        @AfterDomainEventPublication
        void callbackMethod() {
            domainEvents().clear();
        }
    
    }
    ```
2. 创建es的领域对象
    ```java
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
    ```
## 兼容性
我自己测试过的

| **Spring Data Elasticsearch** | **Elasticsearch** | **Spring Framework** | **Spring Boot** |
| ----------------------------- | ----------------- | -------------------- | --------------- |
| 4.4.x                         | 7.17.3            | 5.3.x                | 2.7.x           |

如果不是这个版本的es可以查看spring官方的兼容性<br/>
https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#preface.versions

## 注意事项
1. 自动生成的elasticsearch索引，索引名是jpa数据库领域对象的类名转下划线命名法
2. 支持自动创建索引，但并不是很完善，生产环境建议还是手动创建索引
