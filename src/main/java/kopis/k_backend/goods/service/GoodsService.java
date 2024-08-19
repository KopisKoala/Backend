package kopis.k_backend.goods.service;

import jakarta.transaction.Transactional;
import kopis.k_backend.goods.domain.Goods;
import kopis.k_backend.goods.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoodsService {
    private final GoodsRepository goodsRepository;

    @Transactional
    public List<Goods> findAll() {
        return goodsRepository.findAll();
    }
}
