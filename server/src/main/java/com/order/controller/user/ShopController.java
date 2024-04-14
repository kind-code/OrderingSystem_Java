package com.order.controller.user;

import com.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {
    @GetMapping("/status")
    public Result status(){
        Integer status = com.order.controller.admin.ShopController.status;
        log.info("当前状态为{}",status.toString());
        return Result.success(status);
    }
}
