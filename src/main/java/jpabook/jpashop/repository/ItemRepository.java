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
        if (item.getId() == null) {
            em.persist(item); // persist 이유 : 새로운 엔티티를 영속성 컨텍스트에 등록
            // 하지만 바로 저장하는게 아니라, 1차 캐시에 저장 후 마지막에 모두 올리는 것임
            // persist()는 저장 예약이고, commit 시점에 실제 저장됨
        } else {
            em.merge(item); // merge는 제공되는 기본코드임?
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
