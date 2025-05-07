package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {

        // @GeneratedValue는 JPA가 DB에 INSERT(저장) 할 때 ID를 생성해주는 것이며,
        // new Item() 했을 때는 ID가 null입니다!
        if (item.getId() == null) {
            em.persist(item); // persist 이유 : 새로운 엔티티를 영속성 컨텍스트에 등록
            // 하지만 바로 저장하는게 아니라, 1차 캐시에 저장 후 마지막에 모두 올리는 것임
            // persist()는 저장 예약이고, commit 시점에 실제 저장됨
        } else {
            em.merge(item); // merge는 제공되는 기본코드임?
            // 변경 감지와 병합에 대한 내용이 필요
            // 병합보단 변경감지를 해야함
            // 만약 값을 빼놓고 null이 입력된다면 모두 null로 바꿔버림
            // 그래서 하나하나 관리할 수 있는 변경감지를 해야함
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList(); // 사용되는 문법에 대해 자세히 설명 부탁
    }
}
