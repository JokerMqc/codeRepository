package com.mqc.ssm.controller;

import com.mqc.ssm.annotation.RedisLock;
import com.mqc.ssm.dto.ProductDto;
import org.springframework.web.bind.annotation.*;

/**
 * @author maoqichuan
 * @date 2021年06月30日 20:41
 */
@RestController
@RequestMapping("/reids")
public class RedisLockController {

    @PostMapping("/tryLock")
    @RedisLock(lockName = "addProduct",key = "#productDto.shopId + ':' + #productDto.version")
    public void tryLock(@RequestBody ProductDto productDto) throws InterruptedException {
        Thread.sleep(5000000);
    }

    @PostMapping("/tryLockTest")
    @RedisLock(lockName = "addProduct",key = "#productDto.shopId + ':' + #productDto.version")
    public void tryLockTest(@RequestBody ProductDto productDto) throws InterruptedException {
        return;
    }
}
