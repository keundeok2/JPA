package ManyToManyRefactor.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    private String username;

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();
}
