package com.cbs.oukasystem.repository.base;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.base.DictEntity;
import com.cbs.oukasystem.vo.out.base.DictTreeVO;

@Repository
public interface DictRepository extends JpaRepository<DictEntity, String>, JpaSpecificationExecutor<DictEntity> {

    @Query(nativeQuery = true, value = "select DISTINCT type_name as typeName,type_code as typeCode "
            + " from base_dict order by type_code;")
    List<DictTreeVO> queryTreeByType();
}
