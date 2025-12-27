package com.readingweb.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.readingweb.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 分类Mapper接口
 * 
 * @author ReadingWeb Team
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    
    /**
     * 查询所有启用的分类
     */
    default List<Category> selectAllEnabled() {
        return selectList(
            new LambdaQueryWrapper<Category>()
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSortOrder)
        );
    }
}

