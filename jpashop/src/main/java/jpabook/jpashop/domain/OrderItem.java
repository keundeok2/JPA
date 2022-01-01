package jpabook.jpashop.domain;

import javax.persistence.*;

//@Entity
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "ORDER_ITEM_ID")
    private Long id;

//    @Column(name = "ORDER_ID")
//    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

//    @Column(name = "ITEM_ID")
//    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getOrderId() {
//        return orderId;
//    }

//    public void setOrderId(Long orderId) {
//        this.orderId = orderId;
//    }

//    public Long getItemId() {
//        return itemId;
//    }

//    public void setItemId(Long itemId) {
//        this.itemId = itemId;
//    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
