package com.mxy.bbs_server.controller;

import com.mxy.bbs_server.response.samePostTypeList.samePostTypeListResponse;
import com.mxy.bbs_server.service.SamePostTypeListService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/samePostTypeList")
public class SamePostTypeListController {
    private final SamePostTypeListService service;

    public SamePostTypeListController(SamePostTypeListService service) {
        this.service = service;
    }
    @GetMapping("/query/{type}")
    public samePostTypeListResponse getSamePostTypeList(@PathVariable("type") String type){
        return service.getPostListByPostType(type);
    }
}
