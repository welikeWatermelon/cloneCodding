package jpabook.jpashop.domain;                          // (1)

import lombok.Getter;
import lombok.Setter;                                     // (2)

import jakarta.persistence.*;                             // (3)
import java.time.LocalDateTime;                           // (4)
import java.util.ArrayList;                               // (5)
import java.util.List;                                    // (6)

@Entity                                                   // (7)
@Table(name = "orders")                                   // (8)
@Getter @Setter                                           // (9)
public class Order {                                      // (10)

    /**
     *  기본키
     */
    @Id @GeneratedValue                                   // (11) 주문 PK, DB엔 order_id
    @Column(name = "order_id")                            // (12)
    private Long id;                                      // (13)

    /**
     *  Member와 매핑
     */
    @ManyToOne(fetch = FetchType.LAZY)                    // (14) Order → Member 다대일 관계
    // fetch는 연관된 Member 객체를 언제 DB에서 가져올지 설정하는 옵션.
    //    🐢 FetchType.LAZY (지연 로딩)
    //    필요할 때까지 Member 데이터를 DB에서 안 불러옵니다.
    //
    //    Order를 조회할 때, Member는 **프록시 객체(껍데기)**만 들고 있습니다.
    //
    //    실제로 order.getMember().getName()처럼 접근하는 순간 DB 조회가 발생합니다.
    @JoinColumn(name = "member_id")                       // (15) 주인임!
    private Member member;                                // (16)
    // "Order 엔티티에서 Member 엔티티와 다대일(@ManyToOne) 매핑을 하는데,
    // Order 테이블에 생성될 외래키 컬럼 이름은 'member_id'로 지정하겠다.
    // 그리고 이 연관관계의 주인은 바로 이 필드(member)이다."



    /**
     *  orederItem과 매핑
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // (17) Order → OrderItem 일대다 관계
    // mappedBy = "order"
    // 이쪽(Order)은 연관관계의 주인이 아님
    // OrderItem 엔티티 내부에 있는 order 필드가 외래키를 실제로 갖고 있는 주인이라는 뜻

    // cascade = CascadeType.ALL
    private List<OrderItem> orderItems = new ArrayList<>();   // (18) 하나의 주문은 여러 상품을 가질 수 있음



    /**
     *  Delivery와 매핑
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // (19) Order → Delivery 일대일 관계
    @JoinColumn(name = "delivery_id")                         // (20) 배송 정보와 연결됨
    private Delivery delivery;                                // (21)



    private LocalDateTime orderDate;                          // (22) 주문 일시


    @Enumerated(EnumType.STRING)                              // (23) 열거형으로 주문 상태 (ORDER, CANCEL)
    private OrderStatus status;                               // (24)
    // "OrderStatus라는 enum(열거형)을
    // DB 테이블의 컬럼에 저장할 때,
    // 그 값을 문자열(String) 형태로 저장하겠다." 라는 뜻입니다.




    // 연관관계 편의 메서드 (Member <-> Order)                 // (25)
    // 연관관계 편의 메서드들로서 객체 양방향 설정 쉽게 도와줌
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    // Order 입장에서는 Member member → 주문한 사람 (ManyToOne)
    //Member 입장에서는 List<Order> orders → 내가 한 모든 주문 (OneToMany)

    // setMember()는 Java 객체 간의 관계를 동기화시키는 편의 메서드
    // 자바 객체(member, order)의 양방향 관계를 양쪽에 직접 넣어주는 코드





    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 == //
    public static Order createOrder(Member member, Delivery delivery,
                                    OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // == 비즈니스 로직 == //
    /** 주문 취소 */
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // == 조회 로직 == //
    /** 전체 주문 가격 조회 */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
