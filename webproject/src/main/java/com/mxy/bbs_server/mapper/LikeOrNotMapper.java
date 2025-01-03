package com.mxy.bbs_server.mapper;

import com.mxy.bbs_server.entity.FriendshipData;
import com.mxy.bbs_server.entity.LikeOrNotData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LikeOrNotMapper {
    @Select("select * from LikeOrNot where username = #{username}")
    LikeOrNotData query(String username);

    @Update("update LikeOrNot set postId=#{postId} where username = #{username}")
    void update(LikeOrNotData likeOrNotData);

    @Insert("insert into LikeOrNot(username, postId) VALUES (#{username},NULL)")
    void add(String username);
}
