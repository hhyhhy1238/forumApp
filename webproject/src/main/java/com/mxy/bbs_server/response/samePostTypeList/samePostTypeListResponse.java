package com.mxy.bbs_server.response.samePostTypeList;

import com.mxy.bbs_server.entity.Post;
import com.mxy.bbs_server.entity.PostData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class samePostTypeListResponse {
    private  Boolean success;
    private List<Post> postList;
}
