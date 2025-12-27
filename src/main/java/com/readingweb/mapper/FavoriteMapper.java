package com.readingweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.readingweb.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏Mapper接口
 * 
 * @author ReadingWeb Team
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}

