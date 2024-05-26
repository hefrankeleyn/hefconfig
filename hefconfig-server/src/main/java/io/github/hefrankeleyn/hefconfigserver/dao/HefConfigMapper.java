package io.github.hefrankeleyn.hefconfigserver.dao;

import io.github.hefrankeleyn.hefconfigserver.beans.Configs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Date 2024/5/26
 * @Author lifei
 */
@Repository
@Mapper
public interface HefConfigMapper {

    List<Configs> findConfigsList(@Param("capp") String capp, @Param("cenv") String cenv, @Param("cnamespace") String cnamespace);

    Configs findConfigs(@Param("capp") String capp, @Param("cenv") String cenv, @Param("cnamespace") String cnamespace, @Param("ckey") String ckey);

    void insertConfigs(Configs configs);

    void updateConfigs(Configs configs);

    void updateConfigsVersion(@Param("capp") String capp, @Param("cenv") String cenv, @Param("cnamespace") String cnamespace, @Param("cversion") Long cversion);

    Long findConfigsVersion(@Param("capp") String capp, @Param("cenv") String cenv, @Param("cnamespace") String cnamespace);

    void insertConfigsVersion(@Param("capp") String capp, @Param("cenv") String cenv, @Param("cnamespace") String cnamespace, @Param("cversion") Long cversion);
}
