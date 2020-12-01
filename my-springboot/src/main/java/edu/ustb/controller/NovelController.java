package edu.ustb.controller;

import edu.ustb.entity.Chapters;
import edu.ustb.entity.Novels;
import edu.ustb.service.NovelsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("novel")
public class NovelController
{
    @Resource
    private NovelsService novelsService;

    @PostMapping("create")
    @ResponseBody
    public Map<String, Object> create(String novelName, String writer, String summary, String genre)
    {
        Map<String, Object> result = new HashMap<String, Object>();

        if (novelsService.checkNovelNameExist(novelName) != 0)
        {
            result.put("code", 400);
            result.put("novels", null);
            result.put("msg", "该小说名已存在");
            return result;
        }

        Novels novels = novelsService.create(novelName, writer, summary, genre);

        if (novels != null)
        {
            result.put("code", 200);
            result.put("novels", novels);
            result.put("msg", "小说创建过程成功");
        } else
        {
            result.put("code", 400);
            result.put("novels", null);
            result.put("msg", "小说创建过程失败");
        }
        System.out.println(result);
        return result;
    }

    @PostMapping("add")
    @ResponseBody
    public Map<String, Object> add(String novelName, int chaptersNum, String text)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        int novelId = novelsService.checkNovelNameExist(novelName);
        if (novelId == 0)
        {
            result.put("code", 400);
            result.put("chapters", null);
            result.put("msg", "小说名不存在");
            return result;
        }

        if (novelsService.checkChaptersNumExist(chaptersNum, novelId) != 0)
        {
            result.put("code", 400);
            result.put("chapters", null);
            result.put("msg", "章节已存在");
            return result;
        }

        if (chaptersNum < 1)
        {
            result.put("code", 400);
            result.put("chapters", null);
            result.put("msg", "章节号不能小于1");
            return result;
        }

        Chapters chapters = novelsService.add(novelId, chaptersNum, text);

        if (chapters != null)
        {
            result.put("code", 200);
            result.put("chapters", chapters);
            result.put("msg", "章节创建过程成功");
        } else
        {
            result.put("code", 400);
            result.put("chapters", null);
            result.put("msg", "章节创建过程失败");
        }
        System.out.println(result);
        return result;
    }

    @GetMapping("search")
    @ResponseBody
    public Map<String, Object> search(String genre)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Novels> novelsList = novelsService.searchNovels(genre);
        if (novelsList != null)
        {
            result.put("code", 200);
            result.put("novelsList", novelsList);
            result.put("msg", "查找" + genre + "类型小说成功");
        } else
        {
            result.put("code", 400);
            result.put("novelsList", null);
            result.put("msg", "查找" + genre + "类型小说失败");
        }
        return result;
    }

    @GetMapping("chapter")
    @ResponseBody
    public Map<String, Object> view(String novelName, int chaptersNum)
    {
        System.out.println(chaptersNum);
        Map<String, Object> result = new HashMap<String, Object>();
        Chapters chapters = novelsService.viewChapters(novelName,chaptersNum);
        if (chapters != null)
        {
            result.put("code", 200);
            result.put("chaptersList", chapters);
            result.put("msg", "查找" + novelName + "小说"+chaptersNum+"章节成功");
        } else
        {
            result.put("code", 400);
            result.put("novelsList", null);
            result.put("msg", "查找" + novelName + "小说"+chaptersNum+"章节失败");
        }
        return result;
    }
}
