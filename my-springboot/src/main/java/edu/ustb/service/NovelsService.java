package edu.ustb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.ustb.entity.Novels;

public class NovelsService
{
    public static Novels create(String novelName, String writer, String summary)
    {

        return new Novels();
    }

    public int checkUpdateValue(String update)
    {
        QueryWrapper<Novels> wrapper = new QueryWrapper<Novels>();
        return 0;

    }
}
