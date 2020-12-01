package edu.ustb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //这个实体类会映射为一张表
@Data //自动生成getter/setter toString hashCode equals
@AllArgsConstructor //自动生成带所有参数的构造方法
@NoArgsConstructor //自动生成默认不带参数的构造方法
public class NovelGenres
{
    @Id //表示是主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) //使用默认的主键生成策略 auto_increment
    @TableId(value = "nvid", type = IdType.AUTO)
    private int ngid;   //中间id
    private int nid;    //小说id
    private int gid;    //类型id
}
