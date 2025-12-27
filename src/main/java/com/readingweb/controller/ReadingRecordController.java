package com.readingweb.controller;

import com.readingweb.common.Result;
import com.readingweb.dto.ReadingRecordDTO;
import com.readingweb.entity.ReadingRecord;
import com.readingweb.service.ReadingRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 阅读记录控制器
 * 
 * @author ReadingWeb Team
 */
@RestController
@RequestMapping("/reading-record")
@RequiredArgsConstructor
public class ReadingRecordController {
    
    private final ReadingRecordService readingRecordService;
    
    /**
     * 保存或更新阅读记录
     */
    @PostMapping
    public Result<?> saveRecord(HttpServletRequest request, @Valid @RequestBody ReadingRecordDTO recordDTO) {
        Long userId = (Long) request.getAttribute("userId");
        readingRecordService.saveOrUpdateRecord(userId, recordDTO);
        return Result.success("保存成功");
    }
    
    /**
     * 获取用户的阅读记录
     */
    @GetMapping("/book/{bookId}")
    public Result<ReadingRecord> getRecord(HttpServletRequest request, @PathVariable Long bookId) {
        Long userId = (Long) request.getAttribute("userId");
        ReadingRecord record = readingRecordService.getRecord(userId, bookId);
        if (record == null) {
            return Result.error("暂无阅读记录");
        }
        return Result.success(record);
    }
    
    /**
     * 获取用户的所有阅读记录
     */
    @GetMapping("/list")
    public Result<List<ReadingRecord>> getUserRecords(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ReadingRecord> records = readingRecordService.getUserRecords(userId);
        return Result.success(records);
    }
    
    /**
     * 删除阅读记录
     */
    @DeleteMapping("/book/{bookId}")
    public Result<?> deleteRecord(HttpServletRequest request, @PathVariable Long bookId) {
        Long userId = (Long) request.getAttribute("userId");
        readingRecordService.deleteRecord(userId, bookId);
        return Result.success("删除成功");
    }
}

