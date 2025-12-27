package com.readingweb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.readingweb.entity.ReadingRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 阅读记录Mapper接口
 * 
 * @author ReadingWeb Team
 */
@Mapper
public interface ReadingRecordMapper extends BaseMapper<ReadingRecord> {
}

