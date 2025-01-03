package com.mxy.bbs_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class NumFavor {
    @Getter
    private String postId;
    private Integer numFavor;
}
