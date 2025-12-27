package com.readingweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.readingweb.entity.Chapter;
import org.apache.ibatis.annotations.Mapper;

/**
 * 章节Mapper接口
 * 
 * @author ReadingWeb Team
 */
@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {
}

