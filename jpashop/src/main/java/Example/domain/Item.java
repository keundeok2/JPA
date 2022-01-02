package Example.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//@Entity
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    @Column(name = "NAME")
    private String name;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "STOCKQUANTITY")
    private Long stockQuantity;
}
