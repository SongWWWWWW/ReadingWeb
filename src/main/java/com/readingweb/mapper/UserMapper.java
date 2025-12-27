package com.readingweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.readingweb.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 
 * @author ReadingWeb Team
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

