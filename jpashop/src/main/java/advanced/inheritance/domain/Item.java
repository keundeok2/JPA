package advanced.inheritance.domain;

import javax.persistence.*;

//@Entity
// DB의 JOIN 전략 사용
// 기본 전략은 부모 엔터티와 자식 엔터티의 필드들을 하나의 테이블에 모두 넣는 전략이다.
//@Inheritance(strategy = InheritanceType.JOINED)

// @DiscriminatorColumn이 없어도 DTYPE이 필수적으로 생성된다. 없으면 어떤 데이터인지 구분 못하니까
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

// DTYPE 컬럼 생성한다. 디폴트 값은 자식 엔터티의 이름. JOIN 전략에서는 DTYPE이 없어도 되지만 있는 것이 좋다.
@DiscriminatorColumn
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
