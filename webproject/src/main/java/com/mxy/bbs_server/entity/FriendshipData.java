package com.mxy.bbs_server.entity;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
public class FriendshipData {
    private String username;
    private String myConcerned;
    private String myFans;
    private Integer numConcerned;
    private Integer numFans;
}

