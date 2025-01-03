package com.mxy.bbs_server.service;

import com.mxy.bbs_server.entity.*;
import com.mxy.bbs_server.mapper.*;
import com.mxy.bbs_server.response.action.ActionResponse;
import com.mxy.bbs_server.utility.Utility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActionService {

    private final PostMapper postMapper;

    private final ReviewMapper reviewMapper;

    private final UserInfoMapper userInfoMapper;
    private final LikeOrNotMapper likeOrNotMapper;
    private final numFavorMapper  numfavorMapper;

    public ActionService(PostMapper postMapper, ReviewMapper reviewMapper, UserInfoMapper userInfoMapper, LikeOrNotMapper likeOrNotMapper, numFavorMapper numfavorMapper) {
        this.postMapper = postMapper;
        this.reviewMapper = reviewMapper;
        this.userInfoMapper = userInfoMapper;
        this.likeOrNotMapper = likeOrNotMapper;
        this.numfavorMapper = numfavorMapper;
    }

    public ActionResponse like(ActionRequest actionRequest) {
        System.out.println(actionRequest.toString());
        if (actionRequest.getIsTargetPost()) {
            return likePost(actionRequest);
        }
        return likeReview(actionRequest.getReviewId());
    }

    public ActionResponse favor(ActionRequest actionRequest, Boolean isCancel) {
        System.out.println(actionRequest);
        final var targetPost = actionRequest.getPostId();
        final var who = actionRequest.getUsername();
        final var userInfo = userInfoMapper.query(
                new UserInfoData(who, "", "", "", "", "")
        );
        final ArrayList<String> myCollections = Utility.fromJson(userInfo.getMyCollections(), ArrayList.class);
        if (!isCancel) {
            myCollections.add(targetPost);
            userInfoMapper.update(new UserInfoData(
                    who,
                    userInfo.getNickname(),
                    userInfo.getPersonalSign(),
                    userInfo.getAvatarUrl(),
                    userInfo.getMyPosts(),
                    Utility.toJson(myCollections)
            ));

            final var Oldnum=numfavorMapper.query(actionRequest.getPostId()).getNumFavor();
            numfavorMapper.update(new NumFavor(actionRequest.getPostId(),Oldnum+1));
        } else {
            final var newCollections = new ArrayList<String>();
            for (var postId: myCollections) {
                if (postId!=null&&!postId.equals(targetPost)) {
                    newCollections.add(postId);
                }
            }
            userInfoMapper.update(new UserInfoData(
                    who,
                    userInfo.getNickname(),
                    userInfo.getPersonalSign(),
                    userInfo.getAvatarUrl(),
                    userInfo.getMyPosts(),
                    Utility.toJson(newCollections)
            ));
            final var Oldnum1=numfavorMapper.query(actionRequest.getPostId()).getNumFavor();
            numfavorMapper.update(new NumFavor(actionRequest.getPostId(),Oldnum1-1));
        }
        return new ActionResponse(true);
    }

    private ActionResponse likePost(ActionRequest actionRequest) {
        final var previousPost = postMapper.query(new PostData(actionRequest.getPostId(), null, null, null, null, null, null, null,null));
        if(previousPost==null){
            System.out.println("hello");
        }else {
            previousPost.setLikeNum(previousPost.getLikeNum() + 1);
        }
        postMapper.update(previousPost);

        final var likeOrNotData=likeOrNotMapper.query(actionRequest.getUsername());
        ArrayList<String> myLikes=new ArrayList<>();
        if(likeOrNotData!=null&&Utility.fromJson(likeOrNotData.getPostId(), ArrayList.class)!=null){
            myLikes = Utility.fromJson(likeOrNotData.getPostId(), ArrayList.class);
        }

        myLikes.add(actionRequest.getPostId());

        likeOrNotMapper.update(new LikeOrNotData(actionRequest.getUsername(),
                Utility.toJson(myLikes)));

        return new ActionResponse(true);
    }

    private ActionResponse likeReview(String reviewId) {
        final var previousReview = reviewMapper.query(new ReviewData(reviewId, null, null, null, null, null, null));
        previousReview.setLikeNum(previousReview.getLikeNum() + 1);
        reviewMapper.update(previousReview);
        return new ActionResponse(true);
    }
}
