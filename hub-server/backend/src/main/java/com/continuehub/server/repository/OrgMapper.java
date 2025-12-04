package com.continuehub.server.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.continuehub.server.entity.OrgEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrgMapper extends BaseMapper<OrgEntity> {
}
