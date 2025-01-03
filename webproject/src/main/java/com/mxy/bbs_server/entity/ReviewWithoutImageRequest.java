package com.mxy.bbs_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewWithoutImageRequest {
    private String id;
    private String targetPost;
    private String username;
    private String content;
}
