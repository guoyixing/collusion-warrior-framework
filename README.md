# 双写同步战士
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

### 3.（可选）自定义es的领域对象，默认会生成一个数据库领域对象完全一样的es领域对象


## 注意事项
1. elasticsearch的索引名是jpa数据库领域对象的类名转下划线命名法
   1. 支持自动创建索引，但并不是很完善，生产环境建议还是手动创建索引
      1. 目前jpa数据库领域对象，还需要自己写领域事件通知，后续会自动生成<br/>
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