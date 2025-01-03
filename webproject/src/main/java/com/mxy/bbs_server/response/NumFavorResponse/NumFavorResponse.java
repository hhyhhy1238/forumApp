package com.mxy.bbs_server.response.NumFavorResponse;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class NumFavorResponse {
    private Boolean success;
    private Integer numFavor;
}
