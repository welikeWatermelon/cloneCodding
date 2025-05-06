package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 이거 쓰는 이유
// 해당 클래스(혹은 메서드)의 트랜잭션을 "읽기 전용"으로 설정한다는 뜻
// dirty checking (변경 감지) ❌
// insert/update SQL 생성 ❌
// 성능은 좋아짐, 하지만 저장/수정 불가
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    // 이거 쓰는 이유
    // 위에 기본값으로 readOnly = true 니까 쓰기를 하기 위함
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
