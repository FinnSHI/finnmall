package com.finn.gulimall.seckill.scheduled;

import com.finn.gulimall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/*
 * @description: 秒杀商品定时上架
 * @author: Finn
 * @create: 2022/08/08 11:16
 */
@Slf4j
@Service
public class SeckillSkuSchedules {

    @Autowired
    private SeckillService seckillService;

    /*
    * 每天晚上3点：上架最近三天需要秒杀的商品
    * */
    @Scheduled(cron = "0 0 3 * * ?")
    public void uploadSeckillSkuLatest3Days() {
        seckillService.uploadSeckillSkuLatest3Days();
    }
}
