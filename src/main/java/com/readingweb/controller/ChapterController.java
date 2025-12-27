package com.readingweb.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.readingweb.common.Result;
import com.readingweb.dto.ChapterDTO;
import com.readingweb.dto.ChapterVO;
import com.readingweb.service.ChapterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 章节控制器
 * 
 * @author ReadingWeb Team
 */
@RestController
@RequestMapping("/chapter")
@RequiredArgsConstructor
public class ChapterController {
    
    private final ChapterService chapterService;
    
    /**
     * 创建章节
     */
    @PostMapping
    public Result<ChapterVO> createChapter(@Valid @RequestBody ChapterDTO chapterDTO) {
        ChapterVO chapterVO = chapterService.createChapter(chapterDTO);
        return Result.success("创建成功", chapterVO);
    }
    
    /**
     * 更新章节
     */
    @PutMapping("/{id}")
    public Result<ChapterVO> updateChapter(@PathVariable Long id, @Valid @RequestBody ChapterDTO chapterDTO) {
        ChapterVO chapterVO = chapterService.updateChapter(id, chapterDTO);
        return Result.success("更新成功", chapterVO);
    }
    
    /**
     * 根据ID查询章节（包含内容）
     */
    @GetMapping("/{id}")
    public Result<ChapterVO> getChapterById(@PathVariable Long id) {
        ChapterVO chapterVO = chapterService.getChapterById(id);
        if (chapterVO == null) {
            return Result.error("章节不存在");
        }
        return Result.success(chapterVO);
    }
    
    /**
     * 根据书籍ID查询章节列表（不包含内容）
     */
    @GetMapping("/book/{bookId}")
    public Result<Page<ChapterVO>> getChaptersByBookId(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "20") Integer size) {
        Page<ChapterVO> page = chapterService.getChaptersByBookId(bookId, current, size);
        return Result.success(page);
    }
    
    /**
     * 删除章节
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return Result.success("删除成功");
    }
}

