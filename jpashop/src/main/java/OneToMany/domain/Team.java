package OneToMany.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany
    @JoinColumn(name = "TEAM_ID") // Member 테이블의 TEAM_ID를 관리한다. (연관관계의 주인)
    private List<Member> members = new ArrayList<>();

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
