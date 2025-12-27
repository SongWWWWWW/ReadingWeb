package com.readingweb.service;

import com.readingweb.dto.ReadingRecordDTO;
import com.readingweb.entity.ReadingRecord;

import java.util.List;

/**
 * 阅读记录服务接口
 * 
 * @author ReadingWeb Team
 */
public interface ReadingRecordService {
    
    /**
     * 保存或更新阅读记录
     */
    void saveOrUpdateRecord(Long userId, ReadingRecordDTO recordDTO);
    
    /**
     * 获取用户的阅读记录
     */
    ReadingRecord getRecord(Long userId, Long bookId);
    
    /**
     * 获取用户的所有阅读记录
     */
    List<ReadingRecord> getUserRecords(Long userId);
    
    /**
     * 删除阅读记录
     */
    void deleteRecord(Long userId, Long bookId);
}

