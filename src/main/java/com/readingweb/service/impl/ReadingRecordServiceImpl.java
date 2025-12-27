package com.readingweb.service.impl;

import com.readingweb.dto.ReadingRecordDTO;
import com.readingweb.entity.ReadingRecord;
import com.readingweb.mapper.ReadingRecordMapper;
import com.readingweb.service.ReadingRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 阅读记录服务实现类
 * 
 * @author ReadingWeb Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReadingRecordServiceImpl implements ReadingRecordService {
    
    private final ReadingRecordMapper readingRecordMapper;
    
    @Override
    @Transactional
    public void saveOrUpdateRecord(Long userId, ReadingRecordDTO recordDTO) {
        // 查询是否已存在记录
        ReadingRecord existingRecord = readingRecordMapper.selectOne(
            new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, recordDTO.getBookId())
        );
        
        if (existingRecord != null) {
            // 更新记录
            existingRecord.setChapterId(recordDTO.getChapterId());
            existingRecord.setProgress(recordDTO.getProgress() != null ? recordDTO.getProgress() : 0);
            existingRecord.setLastReadTime(LocalDateTime.now());
            existingRecord.setUpdateTime(LocalDateTime.now());
            readingRecordMapper.updateById(existingRecord);
        } else {
            // 创建新记录
            ReadingRecord record = new ReadingRecord();
            record.setUserId(userId);
            record.setBookId(recordDTO.getBookId());
            record.setChapterId(recordDTO.getChapterId());
            record.setProgress(recordDTO.getProgress() != null ? recordDTO.getProgress() : 0);
            record.setLastReadTime(LocalDateTime.now());
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());
            readingRecordMapper.insert(record);
        }
    }
    
    @Override
    public ReadingRecord getRecord(Long userId, Long bookId) {
        return readingRecordMapper.selectOne(
            new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, bookId)
        );
    }
    
    @Override
    public List<ReadingRecord> getUserRecords(Long userId) {
        return readingRecordMapper.selectList(
            new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .orderByDesc(ReadingRecord::getLastReadTime)
        );
    }
    
    @Override
    @Transactional
    public void deleteRecord(Long userId, Long bookId) {
        readingRecordMapper.delete(
            new LambdaQueryWrapper<ReadingRecord>()
                .eq(ReadingRecord::getUserId, userId)
                .eq(ReadingRecord::getBookId, bookId)
        );
    }
}

