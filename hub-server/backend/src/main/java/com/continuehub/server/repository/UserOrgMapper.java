package com.continuehub.server.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.continuehub.server.entity.UserOrgEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserOrgMapper extends BaseMapper<UserOrgEntity> {
}
