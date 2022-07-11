package io.github.guoyixing.collusion.controller;

import io.github.guoyixing.collusion.pojo.po.Hobby;
import io.github.guoyixing.collusion.service.HobbyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 敲代码的旺财
 * @date 7/7/2022 下午10:24
 */
@Slf4j
@RestController
@RequestMapping("/hobby")
public class HobbyController {

    @Autowired
    private HobbyService hobbyService;

    @PostMapping
    public Hobby save(@RequestBody Hobby hobby) {
        return hobbyService.save(hobby);
    }

    @DeleteMapping("/{id}")
    public void del(@PathVariable("id") Long id) {
        hobbyService.del(id);
    }

}
