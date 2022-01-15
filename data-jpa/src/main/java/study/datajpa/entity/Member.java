package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Member {

    // 엔터티는 기본 생성자가 존재해야함. protected 까지만 JPA가 접근할 수 있다. JPA 스펙에 명시되어있음
    protected Member() {
    }

    public Member(String username) {
        this.username = username;
    }

    @Id @GeneratedValue
    private Long id;
    private String username;
}
