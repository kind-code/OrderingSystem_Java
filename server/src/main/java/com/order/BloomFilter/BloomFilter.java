package com.order.BloomFilter;

import com.order.mapper.DishMapper;
import com.order.mapper.SetmealMapper;
import com.order.utils.BloomFilterUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class BloomFilter {

    @Autowired
    private BloomFilterUtil bloomFilterUtil;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    private
    // 预期插入数量
    static long expectedInsertions = 200L;
    // 误判率
    static double falseProbability = 0.01;

    public static RBloomFilter<Integer> bloomFilter = null;
    @PostConstruct  // 项目启动的时候执行该方法
    public void initbloomFilter() {
        bloomFilter = bloomFilterUtil.create("idWhiteList", expectedInsertions, falseProbability);
        //将菜品id 和 套餐 id 存入布隆过滤器中
        List<Integer> dishList = dishMapper.getId();
        List<Integer> setmealList = setmealMapper.getId();
        for (Integer i : dishList) {
            bloomFilter.add(i);
        }
        for (Integer i : setmealList) {
            bloomFilter.add(i);
        }
    }
}
