package com.readingweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.readingweb.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * 书籍Mapper接口
 * 
 * @author ReadingWeb Team
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}

