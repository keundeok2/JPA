package valuetype.embedded.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//    private LocalDateTime startDate;
//    private LocalDateTime endDate;
    @Embedded
    private Period period;


//    private String city;
//    private String street;
//    private String zipcode;
    @Embedded
    private Address address;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY"))
    })
    private Address address2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
