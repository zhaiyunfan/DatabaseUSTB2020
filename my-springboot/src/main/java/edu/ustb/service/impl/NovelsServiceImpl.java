package edu.ustb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.ustb.entity.Chapters;
import edu.ustb.entity.Genres;
import edu.ustb.entity.NovelGenres;
import edu.ustb.entity.Novels;
import edu.ustb.repository.ChaptersMapper;
import edu.ustb.repository.GenresMapper;
import edu.ustb.repository.NovelGenresMapper;
import edu.ustb.repository.NovelsMapper;
import edu.ustb.service.NovelsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class NovelsServiceImpl implements NovelsService
{
    @Resource
    private NovelsMapper novelsMapper;
    @Resource
    private GenresMapper genresMapper;
    @Resource
    private NovelGenresMapper novelGenresMapper;
    @Resource
    private ChaptersMapper chaptersMapper;

    @Override
    public Novels create(String novelName, String writer, String summary, String genre)
    {
        Novels novels = new Novels();

        novels.setNovelName(novelName);
        novels.setEditDate((new Date()).toString());
        novels.setWriter(writer);
        novels.setTotalChapters(0);
        novels.setSummary(summary);

        System.out.println(novels);
        int res = novelsMapper.insert(novels);
        System.out.println("novels插入成功");
        int genreExist = checkGenresNameExist(genre);
        if (genreExist == 0)
        {
            Genres genres = new Genres();
            genres.setGenre(genre);
            System.out.println(genre);
            int genresRes = genresMapper.insert(genres);
            System.out.println(genres);
            System.out.println("Genres插入结果:" + genresRes);
        }

        NovelGenres novelGenres = new NovelGenres();
        novelGenres.setNid(checkNovelNameExist(novelName));
        novelGenres.setGid(checkGenresNameExist(genre));
        int novelGenresRes = novelGenresMapper.insert(novelGenres);
        System.out.println(novelGenres);
        System.out.println("novelGenres插入结果:" + novelGenresRes);


        if (res > 0)
        {
            return novelsMapper.selectById(novels.getNid());
        }
        return null;
    }

    @Override
    public Chapters add(int nid, int chaptersNum, String text)
    {
        Chapters chapters = new Chapters();
        chapters.setNid(nid);
        chapters.setChaptersNum(chaptersNum);
        chapters.setEditDate((new Date()).toString());
        //处理下字符串
        //text = text.replace("\n\n","<br>");
        //不处理了，会和tomcat 的保留字段冲突
        chapters.setText(text);
        int res = chaptersMapper.insert(chapters);

        Novels novels = novelsMapper.selectById(nid);
        novels.setEditDate(chapters.getEditDate());
        novels.setTotalChapters(novels.getTotalChapters() + 1);

        novelsMapper.updateById(novels);
        System.out.println(novels);

        if (res > 0)
        {
            return chaptersMapper.selectById(chapters.getCid());
        }
        return null;
    }

    @Override
    public List<Novels> searchNovels(String genre)
    {
        QueryWrapper<Novels> novelsQueryWrapper = new QueryWrapper<Novels>();
        QueryWrapper<Genres> genresQueryWrapper = new QueryWrapper<Genres>();
        QueryWrapper<NovelGenres> novelGenresQueryWrapper = new QueryWrapper<NovelGenres>();

        if (!genre.equals("all"))
        {
            List<Novels> novelsList = novelsMapper.selectList(novelsQueryWrapper.eq("nid", -1));
            //取得类型对应的类型id
            genresQueryWrapper.eq("genre", genre);
            Genres genres = genresMapper.selectOne(genresQueryWrapper);
            int gid;
            if (genres == null)
            {
                return null;//没有这个类型，查找失败
            }
            gid = genres.getGid();

            //取得gid为指定值的所有novelGenres中间表对
            novelGenresQueryWrapper.eq("gid", gid);
            List<NovelGenres> novelGenresList = novelGenresMapper.selectList(novelGenresQueryWrapper);
            //遍历符合条件的中间表
            for (int i = 0; i < novelGenresList.size(); i++)
            {
                //novelsQueryWrapper.eq("nid",novelGenresList.get(i).getNid());
                novelsList.add(novelsMapper.selectById(novelGenresList.get(i).getNid()));
            }
            return novelsList;
        }
        return novelsMapper.selectList(novelsQueryWrapper);
    }

    @Override
    public Chapters viewChapters(String novelName, int chaptersNum)
    {
        QueryWrapper<Novels> novelsQueryWrapper = new QueryWrapper<Novels>();
        novelsQueryWrapper.eq("novel_name", novelName);
        Novels novels = novelsMapper.selectOne(novelsQueryWrapper);
        if (novels == null)
        {
            System.out.println("找不到小说" + novelName);
            return null;
        }
        int nid = novels.getNid();

        QueryWrapper<Chapters> chaptersQueryWrapper = new QueryWrapper<Chapters>();
        chaptersQueryWrapper.eq("nid", nid).eq("chapters_num", chaptersNum);
        Chapters chapters = chaptersMapper.selectOne(chaptersQueryWrapper);
        if (chapters == null)
        {
            System.out.println("找不到章节" + chaptersNum);
            return null;
        }
        return chapters;
    }


    @Override
    public int checkNovelNameExist(String update)
    {
        QueryWrapper<Novels> queryWrapper = new QueryWrapper<Novels>();
        queryWrapper.eq("novel_name", update);
        Novels novel = novelsMapper.selectOne(queryWrapper);
        if (novel != null)
        {
            return novel.getNid();
        } else
        {
            return 0;
        }
    }

    @Override
    public int checkGenresNameExist(String update)  //返回值为0时，不存在这样一个实例，否则返回id
    {
        QueryWrapper<Genres> queryWrapper = new QueryWrapper<Genres>();
        queryWrapper.eq("genre", update);
        Genres genres = genresMapper.selectOne(queryWrapper);
        if (genres != null)
        {
            return genres.getGid();
        } else
        {
            return 0;
        }
    }

    @Override
    public int checkChaptersNumExist(int update, int nid)
    {
        QueryWrapper<Chapters> queryWrapper = new QueryWrapper<Chapters>();
        queryWrapper.eq("nid", nid).eq("chapters_num", update);
        Chapters chapters = chaptersMapper.selectOne(queryWrapper);
        if (chapters != null)
        {
            return chapters.getCid();
        } else
        {
            return 0;
        }
    }
}
