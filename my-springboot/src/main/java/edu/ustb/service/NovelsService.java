package edu.ustb.service;

import edu.ustb.entity.Chapters;
import edu.ustb.entity.Novels;

import java.util.List;


public interface NovelsService
{


    public Novels create(String novelName, String writer, String summary, String genre);

    public Chapters add(int nid, int chaptersNum, String text);

    public List<Novels> searchNovels(String genre);

    public Chapters viewChapters(String novelName, int chaptersNum);

    public int checkNovelNameExist(String update);

    public int checkGenresNameExist(String update);

    public int checkChaptersNumExist(int update, int nid);
}
