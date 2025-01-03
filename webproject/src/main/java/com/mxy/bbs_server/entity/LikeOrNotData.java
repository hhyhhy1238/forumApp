package com.mxy.bbs_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class LikeOrNotData {
    String username;
    String postId;

}
