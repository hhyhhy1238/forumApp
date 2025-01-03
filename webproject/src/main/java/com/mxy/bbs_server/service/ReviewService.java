package com.mxy.bbs_server.service;

import com.mxy.bbs_server.entity.*;
import com.mxy.bbs_server.mapper.PostMapper;
import com.mxy.bbs_server.mapper.ReviewMapper;
import com.mxy.bbs_server.response.review.ReviewResponse;
import com.mxy.bbs_server.response.review.ReviewResponseFailedReason;
import com.mxy.bbs_server.utility.Const;
import com.mxy.bbs_server.utility.Utility;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mxy.bbs_server.response.post.PostResponseFailedReason.POST_DOES_NOT_EXIST;
import static com.mxy.bbs_server.response.review.ReviewResponseFailedReason.REVIEW_DOES_NOT_EXISTS;

@Service
public class ReviewService {
    private final ReviewMapper reviewMapper;

    private final PostMapper postMapper;

    public ReviewService(ReviewMapper reviewMapper, PostMapper postMapper) {
        this.reviewMapper = reviewMapper;
        this.postMapper = postMapper;
    }

    public ReviewResponse add(ReviewRequest reviewRequest) throws IOException {
        System.out.println(reviewRequest);
        final var reviewToQuery = new ReviewData(reviewRequest.getId(), null, null, null, null,null, null);
        while (reviewMapper.query(reviewToQuery) != null) {
            Random rand = new Random(System.currentTimeMillis());
            String s=String.valueOf(rand.nextInt());
             reviewRequest.setId(s);
             reviewToQuery.setId(s);
            //return new ReviewResponse(false, ReviewResponseFailedReason.REVIEW_ALREADY_EXISTS, null);
        }
        final var images = Utility.saveReviewImages(reviewRequest.getImages(), reviewRequest.getId());
        reviewMapper.add(new ReviewData(reviewRequest.getId(), reviewRequest.getTargetPost(), Utility.getDate(Const.DATE_FORMAT), reviewRequest.getUsername(), reviewRequest.getContent(), Utility.toJson(images), 0));
        final var previousPost = postMapper.query(new PostData(reviewRequest.getTargetPost(), null, null, null, null, null, null, null,null));
        System.out.println(reviewRequest.getTargetPost());
        if(previousPost==null){;
            return new ReviewResponse(false,null,null);
        }
        //更新针对该post的评论
        List<String> reviewsLst = Utility.fromJson(previousPost.getReviews(), List.class);
        if(reviewsLst==null){
            reviewsLst=new ArrayList<>();
        }
        reviewsLst.add(reviewRequest.getId());
        previousPost.setReviews(Utility.toJson(reviewsLst));
        postMapper.update(previousPost);
        final var reviewData = reviewMapper.query(reviewToQuery);
        return new ReviewResponse(true, null, new Review(reviewData.getId(),
                reviewData.getTargetPost(),
                reviewData.getDate(),
                reviewData.getUsername(),
                reviewData.getContent(),
                Utility.fromJson(reviewData.getImages(), ArrayList.class),
                reviewData.getLikeNum()
        ));
    }

    public ReviewResponse query(ReviewRequest reviewRequest) {
        final var reviewData = reviewMapper.query(new ReviewData(reviewRequest.getId(), null, null, null, null, null, null));
        if (reviewData == null) {
            return new ReviewResponse(false, REVIEW_DOES_NOT_EXISTS, null);
        }
        return new ReviewResponse(true, null,
                new Review(reviewData.getId(),
                        reviewData.getTargetPost(),
                        reviewData.getDate(),
                        reviewData.getUsername(),
                        reviewData.getContent(),
                        Utility.fromJson(reviewData.getImages(), ArrayList.class),
                        reviewData.getLikeNum()
                )
        );
    }

    public ReviewResponse addWithoutImage(ReviewWithoutImageRequest reviewWithoutImageRequest) {
        System.out.println(reviewWithoutImageRequest);
        final var reviewToQuery = new ReviewData(reviewWithoutImageRequest.getId(), null, null, null, null,null, null);
        while (reviewMapper.query(reviewToQuery) != null) {
            Random rand = new Random(System.currentTimeMillis());
            String s=String.valueOf(rand.nextInt());
            reviewWithoutImageRequest.setId(s);
            reviewToQuery.setId(s);
            //return new ReviewResponse(false, ReviewResponseFailedReason.REVIEW_ALREADY_EXISTS, null);
        }

        reviewMapper.add(new ReviewData(reviewWithoutImageRequest.getId(), reviewWithoutImageRequest.getTargetPost(), Utility.getDate(Const.DATE_FORMAT),reviewWithoutImageRequest.getUsername(),reviewWithoutImageRequest.getContent(), null, 0));
        final var previousPost = postMapper.query(new PostData(reviewWithoutImageRequest.getTargetPost(), null, null, null, null, null, null, null,null));
        System.out.println(reviewWithoutImageRequest.getTargetPost());
        if(previousPost==null){;
            return new ReviewResponse(false,null,null);
        }
        //更新针对该post的评论
        List<String> reviewsLst = Utility.fromJson(previousPost.getReviews(), List.class);
        if(reviewsLst==null){
            reviewsLst=new ArrayList<>();
        }
        reviewsLst.add(reviewWithoutImageRequest.getId());
        previousPost.setReviews(Utility.toJson(reviewsLst));
        postMapper.update(previousPost);
        final var reviewData = reviewMapper.query(reviewToQuery);
        return new ReviewResponse(true, null, new Review(reviewData.getId(),
                reviewData.getTargetPost(),
                reviewData.getDate(),
                reviewData.getUsername(),
                reviewData.getContent(),
                Utility.fromJson(reviewData.getImages(), ArrayList.class),
                reviewData.getLikeNum()
        ));

    }
}
