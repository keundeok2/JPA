package Example.domain;

import javax.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STREET")
    private String street;

    @Column(name = "ZIPCODE")
    private String zipcode;

}
