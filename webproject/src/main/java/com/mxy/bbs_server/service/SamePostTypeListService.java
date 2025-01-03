package com.mxy.bbs_server.service;

import com.mxy.bbs_server.entity.Post;
import com.mxy.bbs_server.entity.PostData;
import com.mxy.bbs_server.entity.PostType;
import com.mxy.bbs_server.mapper.PostMapper;
import com.mxy.bbs_server.response.samePostTypeList.samePostTypeListResponse;
import com.mxy.bbs_server.response.userinfo.LikeOrNotResponse;
import com.mxy.bbs_server.utility.Utility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SamePostTypeListService {
    private final PostMapper postMapper;

    public SamePostTypeListService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }


    public samePostTypeListResponse getPostListByPostType(String type){
        List<PostData> postDataList= postMapper.queryByPostType(type);
        List<Post> postList=new ArrayList<Post>();
        for (final PostData postData : postDataList) {
            postList.add(new Post(postData.getId(),postData.getDate(),postData.getOwner(),
                    postData.getTitle(),postData.getContent(),Utility.fromJson(postData.getImages(), ArrayList.class),
                    postData.getLikeNum(),Utility.fromJson(postData.getReviews(), ArrayList.class),postData.getPostType()));
        }
        return new samePostTypeListResponse(true,postList);
    }
}
