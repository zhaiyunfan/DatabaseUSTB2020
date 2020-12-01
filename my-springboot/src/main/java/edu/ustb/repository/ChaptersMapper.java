package edu.ustb.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.ustb.entity.Chapters;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ChaptersMapper extends BaseMapper<Chapters>
{

}
