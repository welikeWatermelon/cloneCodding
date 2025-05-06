package jpabook.jpashop.domain;                      // (1)

import jakarta.persistence.*;                         // (2)
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;                           // (3)
import java.util.List;                                // (4)

@Entity                                               // (5) 이 클래스가 JPA의 엔티티임을 선언
@Getter @Setter                                       // (6) 	Lombok을 통해 Getter/Setter 자동 생성
public class Member {                                 // (7)

    @Id @GeneratedValue                               // (8) 	기본 키 id를 자동 생성 (PK), 이름은 DB에선 member_id로 지정
    @Column(name = "member_id")                       // (9)
    private Long id;                                  // (10)

    private String name;                              // (11)

    @Embedded                                         // (12) 내장 값 타입 Address를 포함시킴 (@Embeddable로 따로 정의됨)
    private Address address;                          // (13)

    @OneToMany(mappedBy = "member")                   // (14)
    private List<Order> orders = new ArrayList<>();   // (15)

}                                                     // (16)
