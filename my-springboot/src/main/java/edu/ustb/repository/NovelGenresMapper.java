package edu.ustb.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.ustb.entity.NovelGenres;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NovelGenresMapper extends BaseMapper<NovelGenres>
{
}