# DatabaseUSTB2020

## 后端接口API表

### 创建小说

类型:	post

URL:	/novel/create

参数:

```javascript
let params = new URLSearchParams();
          params.append('novelName',this.formData.novelName)
          params.append('writer',this.formData.writer)
          params.append('summary',this.formData.summary)
          params.append('genre',this.formData.genre)
```

### 添加章节

类型:	post

URL:	/novel/add

参数:

```javascript
let params = new URLSearchParams();
          params.append('novelName',this.form.novelName)
          params.append('chaptersNum',this.form.chaptersNum)
          params.append('text',this.form.mainText)
```

### 查找小说

类型:	get

URL:	/novel/search?genre=genreName

参数:

```javascript
genreName	类型名称
```

### 阅读章节

类型:	get

URL:	'novel/chapter?novelName='+novelName+'&chaptersNum='+value

参数:

```javascript
novelName	小说名称
chaptersNum	章节序号
```





## 安装指南

> 这里对手动安装构建运行项目的过程进行概述，如果使用docker镜像拉取项目，那么可以直接跳到最后部分

- 推荐使用IDEA进行后端SpringBoot项目的构建，用IDEA打开```my-springboot```文件夹下的后端项目

首先配置好你的JDK和Maven，并在IDEA里引入好。JDK版本最好是JDK11，否则需要修改pom.xml文件中的JAVA版本。IDEA自带3.6.3版本的Maven，可以直接使用。需要注意的是，IDEA中，除了setting里需要配置JDK版本、Project Structure里需要配置Module JDK版本之外，还需要在Build设置中将项目构建托管给Maven，这些操作属于基础配置，在搜索引擎中都可以找到配置方法

- 对于前端项目，需要安装好node.js和npm。如何进行npm换源此处不再赘述

```bash
npm install -g @vue/cli	# 全局安装vue
cd 到你的git拉取好的文件夹下的my-vue文件夹
npm install	# 根据package.json文件的内容在当前文件夹下安装项目依赖
```

这样，前后端的基础运行环境就搭建好了

## 构建指南

### 数据库初始化

第一次启动本项目前，需要在MySQL服务器中预先创建好名为```novel_db```的database，并在```application.yml```配置文件中的```spring.datasource.username```与```spring.datasource.password```字段中填写为```jdbc```登录MySQL准备的用户名和密码。这些准备工作为```jdbc```提供了访问的凭证。

### 后端项目启动

构建好SpringBoot项目并第一次开始运行时，后端程序会使用```jdbc```驱动连接并登录3306端口上的MySQL服务器，自动检查```novel_db```数据库下是否已存在名为

1. Novels
2. Genres
3. NovelGenres
4. Chapters

的表，如果没有，自动执行SQL语句

```mysql
create table chapters (cid integer not null auto_increment, chapters_num integer not null, edit_date varchar(255), nid integer not null, text varchar(16000), primary key (cid)) engine=InnoDB
create table genres (gid integer not null auto_increment, genre varchar(255), primary key (gid)) engine=InnoDB
create table novel_genres (ngid integer not null auto_increment, gid integer not null, nid integer not null, primary key (ngid)) engine=InnoDB
create table novels (nid integer not null auto_increment, edit_date varchar(255), novel_name varchar(255), summary varchar(140), total_chapters integer not null, writer varchar(255), primary key (nid)) engine=InnoDB
```

进行表创建，这里由于不同平台对于```varchar```类型变量的长度限制不同，所以判断了当前项目构建平台是```Windows```还是```Linux```

> MySQL的服务器URL以及端口可在application.yml的```spring.datasource.url```字段中修改，但是同样需要在前端项目中axios请求地址
>
> SpringBoot后端接口的端口和上下文可以在```server.port```和```server.servlet.context-path```字段中修改

如果运行一切正常，后端项目和数据库就已经准备好使用了，可以用*PostMan*测试接口可用性。

### 前端项目启动

在my-vue目录下执行命令

```bash
npm run serve
```

npm会自动编译整个Vue项目，并在```http://localhost:8080/ ```下映射前端项目，可以直接用浏览器访问前端页面使用这个项目

## 使用指南

### 添加小说

点击```添加小说```按钮可以唤出添加小说组件

![](/home/zhaiyunfan/图片/2020-12-14 19-55-21 的屏幕截图.png)

此处，标题字段和小说类型字段是必填项，不能为空

![](/home/zhaiyunfan/图片/2020-12-14 19-58-18 的屏幕截图.png)

填写完毕后点击确定，前端会将输入的四个字段的小说信息自动封装好，并作为参数向后端接口URL```novel/create```发送post请求

此处对应的后端代码如下

```java
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
```



后端接收请求后，首先调用```novelsService.checkNovelNameExist(novelName)```函数发起一个查询

```java
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
```



> 函数返回值为小说的nid，由于主键自增策略，nid不会为0也不会为null

```mysql
==>  Preparing: SELECT gid,genre FROM genres WHERE (genre = ?) 
==> Parameters: sciencebook(String)
<==      Total: 0
```

来确认是否已有同名小说存在，如果查询返回了非空元组，函数返回值非0，有同名小说存在，则直接返回状态码400，并告知前端创建失败；如果返回的查询结果元组为空，函数返回值为0，即没有同名小说存在，则可以正常创建，正式调用```novelsService.create(novelName, writer, summary, genre)```函数进入小说创建流程；

```java
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
```



在create函数中，首先创建并set要插入的Novels对象,初始化修改日期，然后执行SQL语句

```mysql
==>  Preparing: INSERT INTO novels ( novel_name, writer, edit_date, total_chapters, summary ) VALUES ( ?, ?, ?, ?, ? ) 
==> Parameters: 三体(String), 刘慈欣(String), Mon Dec 14 19:58:58 CST 2020(String), 0(Integer), 获得星云奖的科幻小说(String)
<==    Updates: 1
```

将小说本身先插入Novels表，如果插入成功则继续向下进行。

接下来处理小说的类型信息，考虑到新插入的小说可能为一个全新类型，所以这里先调用```checkGenresNameExist(genre)```函数根据小说类型进行查询，查看Genres表中是否已经存在这一类型

```java
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
```

于是执行SQL语句

```mysql
==>  Preparing: SELECT gid,genre FROM genres WHERE (genre = ?) 
==> Parameters: sciencebook(String)
<==      Total: 0
```

如果查询返回的元组为空，函数返回值为0，表中不存在这个类型，则将此类型名构建为Genre表项插入到Genres表中，执行SQL语句

```mysql
==>  Preparing: INSERT INTO genres ( genre ) VALUES ( ? ) 
==> Parameters: sciencebook(String)
<==    Updates: 1
```

将新类型插入到Genres表中

接下来处理Novels和Genres对应关系的NovelGenres中间表，由于中间表只存储Genre的gid和Novel的nid的对应关系，所以每次插入新小说时必定会更新插入这个表

先查询获取小说的nid和类型的gid，确保前两项插入成功，执行SQL语句

```mysql
==>  Preparing: SELECT nid,novel_name,writer,edit_date,total_chapters,summary FROM novels WHERE (novel_name = ?) 
==> Parameters: 三体(String)
<==    Columns: nid, novel_name, writer, edit_date, total_chapters, summary
<==        Row: 1, 三体, 刘慈欣, Mon Dec 14 19:58:58 CST 2020, 0, 获得星云奖的科幻小说
<==      Total: 1
```

```mysql
==>  Preparing: SELECT gid,genre FROM genres WHERE (genre = ?) 
==> Parameters: sciencebook(String)
<==    Columns: gid, genre
<==        Row: 1, sciencebook
<==      Total: 1
```

根据获取的nid和gid直接构建NovelGenre表项并执行SQL语句

```mysql
==>  Preparing: INSERT INTO novel_genres ( nid, gid ) VALUES ( ?, ? ) 
==> Parameters: 1(Integer), 1(Integer)
<==    Updates: 1
```

将中间表项插入

至此，整个小说插入过程就完成了，插入成功，后端会返回状态码200，前端页面收到成功消息后会自动重新查询小说列表，执行查询，更新的小说列表会被自动加载到前端

![](/home/zhaiyunfan/图片/2020-12-14 20-48-28 的屏幕截图.png)

### 查找指定类型小说

每次刷新或操作前端页面时，都会依据页面左上角输入的查询指定类型小说字段，自动向后端接口发送一个get请求，此get请求的URL为```novel/search?genre=```+```小说类型字段```，通过URL拼接，可以实现对不同类型小说的列表查询。

```java
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
```

后端controller监听到这个请求，通过```jdbc```访问数据库并返回小说ID、小说名、作者、编辑日期、总章节数和内容总结信息

```java
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
```



1. 如果get请求URL中小说类型字段为```all```，则不进行传参，直接在数据库中查找全部类型小说，直接执行SQL语句：

```mysql
SELECT nid,novel_name,writer,edit_date,total_chapters,summary FROM novels
```

 	2. 如果get请求URL中小说类型字段不为```all```，则首先按genre字段的值在Genres表中查找是否存在这一类型，执行SQL语句

```mysql
==>  Preparing: SELECT gid,genre FROM genres WHERE (genre = ?) 
==> Parameters: sciencebook(String)
<==    Columns: gid, genre
<==        Row: 1, sciencebook
<==      Total: 1
```

如果该类型存在，则依据类型的gid继续在中间表NovelGenres中查找与该类型对应的所有小说，执行SQL语句

```mysql
==>  Preparing: SELECT ngid,nid,gid FROM novel_genres WHERE (gid = ?) 
==> Parameters: 1(Integer)
<==    Columns: ngid, nid, gid
<==        Row: 1, 1, 1
<==      Total: 1
```

最后，根据获取到的中间表元组列表，循环遍历整个列表，根据列表中的每个nid在Novels表中进行查找，执行SQL语句

> 这里只是一个例子，实际查找中会使用循环，故可以查找出所有的该类型小说

```mysql
==>  Preparing: SELECT nid,novel_name,writer,edit_date,total_chapters,summary FROM novels WHERE nid=? 
==> Parameters: 1(Integer)
<==    Columns: nid, novel_name, writer, edit_date, total_chapters, summary
<==        Row: 1, 三体, 刘慈欣, Mon Dec 14 19:58:58 CST 2020, 0, 获得星云奖的科幻小说
<==      Total: 1
```

查询完毕后，后端将返回的小说列表返回给前端，并显示在页面上，这样就完成了小说的按类型搜索及展示功能。

 ## 添加章节

