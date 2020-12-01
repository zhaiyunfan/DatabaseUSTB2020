package edu.ustb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity //这个实体类会映射为一张表
@Data //自动生成getter/setter toString hashCode equals
@AllArgsConstructor //自动生成带所有参数的构造方法
@NoArgsConstructor //自动生成默认不带参数的构造方法
public class Chapters
{
    @Id //表示是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //使用默认的主键生成策略 auto_increment
    @TableId(value = "cid",type = IdType.AUTO)
    private int cid;    //章节编号

    private int nid;    //小说编号
    private int chaptersNum;    //章节号
    private String editDate; //修改日期

    @Column(length = 20000)
    private String text;    //正文
}
