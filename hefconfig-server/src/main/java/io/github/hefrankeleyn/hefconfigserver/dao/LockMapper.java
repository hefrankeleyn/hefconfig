package io.github.hefrankeleyn.hefconfigserver.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Date 2024/6/5
 * @Author lifei
 */
@Repository
@Mapper
public interface LockMapper {

    String selectForUpdate();
}
