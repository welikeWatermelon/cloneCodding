package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
// 이 클래스는 JPA가 관리하는 테이블 대상이 된다

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// 이 추상 클래스를 상속받는 모든 자식 클래스의 정보를 하나의 테이블에 저장

@DiscriminatorColumn(name = "dtype")
// 어떤 자식인지 구분하기 위해 dtype이라는 컬럼을 테이블에 추가

@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    //////////////////////////////////////////////////////////////////////////////
    // == 비즈니스 로직 == //
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }


    // 굳이 이 파일에서 이 코드를 안썼음
//    @OneToMany(mappedBy = "item")
//    private List<OrderItem> orderItems = new ArrayList<>();
}
