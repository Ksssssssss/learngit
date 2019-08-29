package com.hoolai.report.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hoolai.report.entiy.Retention;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: Ksssss(chelin)
 * @create: 2019-08-24 16:41
 */
@Mapper
public interface RetentionMappper extends BaseMapper<Retention> {
    @Select("select gameid, DATE_SUB(ds,INTERVAL dr DAY) ds,dr,retention,snid from retention ${ew.customSqlSegment}")
    List<Retention> getAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}
