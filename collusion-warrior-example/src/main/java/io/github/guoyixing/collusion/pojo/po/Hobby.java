package io.github.guoyixing.collusion.pojo.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "hobby")
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


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
