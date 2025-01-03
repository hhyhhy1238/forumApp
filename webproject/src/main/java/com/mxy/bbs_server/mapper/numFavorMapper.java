package com.mxy.bbs_server.mapper;

import com.mxy.bbs_server.entity.LikeOrNotData;
import com.mxy.bbs_server.entity.NumFavor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface numFavorMapper {
    @Select("select * from NumFavor where postId = #{postId}")
    NumFavor query(String postId);

    @Update("update NumFavor set numFavor=#{numFavor} where postId = #{postId}")
    void update(NumFavor numFavor);

    @Insert("insert into NumFavor(postId, numFavor) VALUES (#{postId},0)")
    void add(String postId);
}
