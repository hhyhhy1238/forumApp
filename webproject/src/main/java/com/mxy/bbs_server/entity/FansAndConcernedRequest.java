package com.mxy.bbs_server.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class FansAndConcernedRequest {

    private String username;
}
