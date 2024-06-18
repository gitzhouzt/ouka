package com.cbs.oukasystem.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cbs.oukasystem.entity.base.DictItemEntity;

@Repository
public interface DictItemRepository
                extends JpaRepository<DictItemEntity, String>, JpaSpecificationExecutor<DictItemEntity> {

        @Modifying
        @Query(value = "update base_dict_item "
                        + " set dict_code = :newCode "
                        + " where dict_code = :oldCode ", nativeQuery = true)
        void updateAllItemByDictCode(@Param("newCode") String newCode,
                        @Param("oldCode") String oldCode);

        DictItemEntity findByDictCodeAndItemCode(String dictCode, String itemCode);
}
