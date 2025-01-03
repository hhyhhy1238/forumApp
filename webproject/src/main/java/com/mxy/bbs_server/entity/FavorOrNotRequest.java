package com.mxy.bbs_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FavorOrNotRequest {
    private String username;
    private String targetusername;
}
