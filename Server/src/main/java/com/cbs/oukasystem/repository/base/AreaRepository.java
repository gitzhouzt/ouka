package com.cbs.oukasystem.repository.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.base.AreaEntity;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, String>, JpaSpecificationExecutor<AreaEntity> {

        @Query(value = "select a from AreaEntity "
                        + " a where a.areaCode in (:areaCodes) "
                        + " order by a.areaCode asc ", nativeQuery = false)
        List<AreaEntity> findByAreaCodes(@Param("areaCodes") List<String> areaCodes);

        @Query(nativeQuery = true, value = "select * from sys_area a "
                        + " where if( (:keyword is not null or :keyword != ''), a.area_code like %:keyword% or a.area_name like %:keyword% ,1=1) group by a.area_code ORDER BY a.area_code limit :page,:pageSize ")
        List<AreaEntity> queryAllArea(@Param("keyword") String keyword, @Param("page") int page,
                        @Param("pageSize") int pageSize);

        @Query(nativeQuery = true, value = "select count(DISTINCT(area_code)) from sys_Area a where if( (:keyword is not null or :keyword != '') , a.area_code like %:keyword% or a.area_name like %:keyword% ,1=1)")
        long countAllArea(@Param("keyword") String keyword);

}