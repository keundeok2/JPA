package Example.domain;

import javax.persistence.*;

@Entity
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Column(name = "ORDERPRICE")
    private Long price;

    @Column(name = "COUNT")
    private Long count;
}
