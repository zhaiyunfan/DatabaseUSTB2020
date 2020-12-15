# 开源小说共享平台

## 项目概述

### 综述

依赖于```Vue+SpringBoot+docker```本项目需实现一个开源小说共享平台网站，作者可以将小说及其信息上传至网站，且在后续操作中可以添加章节，读者可以自由选择想要阅读的小说的章节，并可以通过小说类型进行查询。

### 需求分析

1. 需要设计用于存储小说内容和标题、作者、简介、类型等信息的数据库；

2. 需要根据实际使用需求设计web界面，以实现小说基本信息的展示和小说以及章节的添加、阅读等功能的可视化；

3. 实现小说标题、作者、简介、类型添加至数据库的功能，并能在web界面进行显示；

4. 实现根据类型查找指定类型小说的功能，并能将查找结果显示在web界面；

5. 能够在首页展示的每部小说中添加章节和阅读章节；

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

## 数据库设计

数据库中共有四张表

1. Novels表：存放除章节正文外的所有的小说信息，主键nid
2. Genres表：存放小说类型信息，主键gid
3. NovelGenres表：中间表，通过nid和gid的外键依赖建立每部小说与其类型之间的对应关系，主键ngid
4. Chapter表：存放一个章节的正文，通过nid的外键依赖来确定该章正文的归属，主键cid

![](/home/zhaiyunfan/图片/微信图片_20201215201120.png)

各主键均采用自增策略

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

```javascript
    dialogFormAdd(formdong) {
      console.log(this.formData)
      this.$refs[formdong].validate((valid) => {
        if (valid) {
          let params = new URLSearchParams();
          params.append('novelName',this.formData.novelName)
          params.append('writer',this.formData.writer)
          params.append('summary',this.formData.summary)
          params.append('genre',this.formData.genre)
          this.$axios.post('novel/create', params).then(res => {
            this.$message({
              type: "success",
              message: "添加书籍成功"
            })
            this.dialogAdd.show = false;
            this.$emit('update');
            console.log(res)
          })
          this.formDate = ""
        } else {
          console.log('填写字段不完整');
          return false;
        }
      })
    }
```



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

```javascript
    getUserInfo() {
      let url = 'novel/search?genre=' + keyUser
      this.$axios.get(url).then(res => {
        console.log(res)
        this.tableData = res.data.novelsList
      })
    },
```



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

### 添加章节

在首页展示的每部小说都可以使用对应的*添加章节*和*阅读章节*按钮进行章节层面的操作

![](/home/zhaiyunfan/图片/2020-12-15 12-26-59 的屏幕截图.png)

首先以添加章节为例，点击添加章节按钮，会唤出添加章节组件

![](/home/zhaiyunfan/图片/2020-12-15 12-45-10 的屏幕截图.png)

前端页面会记录每一部小说的总章数，待插入章节序号会默认在总章数基础上顺序自增，和正文属性一样，都是必填项

填写完成并点击确定后，前端会将小说名```novelName```、待插入章节序号```chaptersNum```和章节正文```text```封装好，调用```novel/add/```接口发送post请求

```javascript
    dialogFormEdit(formEdit) {
      this.$refs[formEdit].validate((valid) => {
        if (valid) {
          let params = new URLSearchParams();
          params.append('novelName',this.form.novelName)
          params.append('chaptersNum',this.form.chaptersNum)
          params.append('text',this.form.mainText)
          this.$axios.post(`novel/add`,params).then(res => {
            this.$message({
              type:"success",
              message:"插入章节成功"
            })
            console.log(res)
            this.dialogEdit.show = false;
            this.$emit('updateEdit')
          })
        } else {
          console.log('error submit!!');
          return false;
        }
```

后端监听到这个请求后进入章节添加流程，在插入之前，需要预先做一些参数合法性判断

```java
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
```

首先调用```novelsService.checkNovelNameExist(novelName)```函数发起SQL查询，判断接收到的novelName对应的小说是否已经存在，如果小说不存在，则发生错误，直接结束创建流程

> Novel和Chapter的对应关系有Chapters表中的nid字段来确定，每个Chapter会存储它所属的Novel的nid，外键约束

如果小说已存在，则根据```novelName```和```chapterNum```判断该小说的这一章节是否已经存在，调用```novelsService.checkChaptersNumExist(chaptersNum, novelId)```函数，发起SQL查询，执行SQL语句

```mysql
==>  Preparing: SELECT cid,nid,chapters_num,edit_date,text FROM chapters WHERE (nid = ? AND chapters_num = ?) 
==> Parameters: 1(Integer), 1(Integer)
<==      Total: 0
```

如果查询结果元组为空，该小说不存在对应章节，则最后判断下```chapterNum```是否大于等于1，如过是则入参合法，正式调用```novelsService.add(novelId, chaptersNum, text)```函数进行插入流程

```java
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
```

封装好Chapter对象后执行SQL语句

```mysql
==>  Preparing: INSERT INTO chapters ( nid, chapters_num, edit_date, text ) VALUES ( ?, ?, ?, ? ) 
==> Parameters: 1(Integer), 1(Integer), Tue Dec 15 12:48:38 CST 2020(String), 中国，1967年。(为保持报告整洁以下部分正文字段省略)(String)
<==    Updates: 1
```

进行插入，插入完成后，需要更新Novel对象的修改日期和总章节数，先按nid执行SQL查询，获取Novel对象

```mysql
<==    Columns: nid, novel_name, writer, edit_date, total_chapters, summary
<==        Row: 1, 三体, 刘慈欣, Mon Dec 14 19:58:58 CST 2020, 0, 获得星云奖的科幻小说
<==      Total: 1
```

修改Novel对象的部分属性，将修改日期更新为章节的插入日期，并将总章数自增

```java
        novels.setEditDate(chapters.getEditDate());
        novels.setTotalChapters(novels.getTotalChapters() + 1);
```

然后将更新的Novel对象重新update回数据库，执行update SQL语句

```mysql
==>  Preparing: UPDATE novels SET novel_name=?, writer=?, edit_date=?, total_chapters=?, summary=? WHERE nid=? 
==> Parameters: 三体(String), 刘慈欣(String), Tue Dec 15 12:48:38 CST 2020(String), 1(Integer), 获得星云奖的科幻小说(String), 1(Integer)
<==    Updates: 1
```

最后执行SQL查询，按Chapter的cid查询确认下是否插入成功

```mysql
==>  Preparing: SELECT cid,nid,chapters_num,edit_date,text FROM chapters WHERE cid=? 
==> Parameters: 1(Integer)
<==    Columns: cid, nid, chapters_num, edit_date, text
<==        Row: 1, 1, 1, Tue Dec 15 12:48:38 CST 2020, 中国，1967年。(报告中部分正文省略)
<==      Total: 1
```

并将查询结果封装返回给前端，前端提示插入成功，并自动调用```/novel/search```接口更新前端页面，重新从后端数据库获取数据，整个插入过程就结束了

![](/home/zhaiyunfan/图片/2020-12-15 13-25-11 的屏幕截图.png)

插入章节后，Novel的总章节数和修改日期都会更新

### 阅读章节

同样的，点击阅读章节可以唤出章节阅读页面

![](/home/zhaiyunfan/图片/2020-12-15 13-28-33 的屏幕截图.png)

此处可以输入大于零但小于等于该小说总章数的章节号，确定后同样会在URL中拼接```novelName```和```chaptersNun```，调用```/novel/chapter```接口发送get请求

```javascript
    handleRead(novelName, totalChapters) {
      this.$prompt('请输入要阅读的章节号', '正文', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        console.log(value);
        console.log(novelName)
        if (value > totalChapters) {
          this.$message({
            type: "fail",
            message: "超出总章数限制"
          })
          return false
        } else {
          console.log('novel/chapter?novelName='+{novelName}+'&chaptersNum='+{value})
          this.$axios.get('novel/chapter?novelName='+novelName+'&chaptersNum='+value).then(res => {
            this.dialogVisible = true
            console.log(res.data.chaptersList.text)
            this.dialogText = res.data.chaptersList.text
            this.$message({
              type: "success",
              message: "打开章节成功"
            })
            console.log(res)

          })

        }
      });
    },
```

后端监听到接口请求后，会直接调用```novelsService.viewChapters(novelName,chaptersNum)```函数

```java
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
```

```java
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
```

首先会发起查询，按```novel_name```字段来查找对应小说，执行SQL语句

```mysql
==>  Preparing: SELECT nid,novel_name,writer,edit_date,total_chapters,summary FROM novels WHERE (novel_name = ?) 
==> Parameters: 三体(String)
<==    Columns: nid, novel_name, writer, edit_date, total_chapters, summary
<==        Row: 1, 三体, 刘慈欣, Tue Dec 15 12:48:38 CST 2020, 1, 获得星云奖的科幻小说
<==      Total: 1
```

获取查询结果中的```nid```字段，与```chapters_num```字段一同进行SQL查询，执行SQL语句

```mysql
==>  Preparing: SELECT cid,nid,chapters_num,edit_date,text FROM chapters WHERE (nid = ? AND chapters_num = ?) 
==> Parameters: 1(Integer), 1(Integer)
<==    Columns: cid, nid, chapters_num, edit_date, text
<==        Row: 1, 1, 1, Tue Dec 15 12:48:38 CST 2020, 中国，1967年。(省略部分正文)
<==      Total: 1
```

并将得到的章节查询结果返回给前端进行显示

![](/home/zhaiyunfan/图片/2020-12-15 13-32-30 的屏幕截图.png)

![](/home/zhaiyunfan/图片/2020-12-15 13-40-31 的屏幕截图.png)

至此，小说的章节阅读功能就完成了

## Docker部署指南

直接使用```docker-compose```进行打包，只需要简单的对Vue\SpringBoot\Tomcat分别书写```docker-file```就可以直接进行打包

![](/home/zhaiyunfan/图片/微信图片_20201215200728.jpg)

![](/home/zhaiyunfan/图片/微信图片_20201215200749.jpg)

![](/home/zhaiyunfan/图片/微信图片_20201215200759.jpg)



## 项目反思

由于工程经验的缺失，对前端技术的不熟悉，起初实现了按类型查找的接口，但是并没能将这个接口在前端输入后正常调用，几番查找资料后最终才搞明白怎么获取输出并实时刷新，最终万幸没有留下遗憾

