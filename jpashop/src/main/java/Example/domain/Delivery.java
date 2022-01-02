package Example.domain;

import javax.persistence.*;

//@Entity
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Orders order;

    @Column(name = "CITY")
    private String City;

    @Column(name = "STREET")
    private String street;

    @Column(name = "ZIPCODE")
    private String zipcode;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private DeliveryStatus status;
}
