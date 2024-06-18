package com.cbs.oukasystem.service.base;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.base.ConfigEntity;
import com.cbs.oukasystem.mapstruct.base.ConfigVOEntityMapStruct;
import com.cbs.oukasystem.repository.base.ConfigRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.base.IUConfigVO;
import com.cbs.oukasystem.vo.in.base.QueryConfigVO;
import com.cbs.oukasystem.vo.out.base.ConfigVO;

@Service
@Transactional
public class ConfigService {

    String KEY = "基礎設定";

    @Autowired
    private ConfigRepository repository;

    /*
     * 基礎設定を得る
     */

    public ConfigVO getById(String id) {
        return ConfigVOEntityMapStruct.INSTANCE.toConfigVO(getEntity(id));
    }

    public ConfigEntity getEntity(String id) {
        Optional<ConfigEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全ての基礎設定
     */

    public List<ConfigVO> getAll() {
        return ConfigVOEntityMapStruct.INSTANCE.toConfigVOs(findAll());
    }

    public List<ConfigEntity> findAll() {
        return repository.findAll();
    }

    public List<ConfigEntity> findAll(Specification<ConfigEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * 基礎設定のリスト
     */

    public ListVO<ConfigVO> getPages(QueryConfigVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "dictCode");
        Specification<ConfigEntity> specification = Search(qVo);
        Page<ConfigEntity> pages = repository.findAll(specification, pageable);
        List<ConfigVO> list = ConfigVOEntityMapStruct.INSTANCE.toConfigVOs(pages.toList());
        ListVO<ConfigVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 基礎設定検索
     */
    public Specification<ConfigEntity> Search(QueryConfigVO qVo) {
        return new Specification<ConfigEntity>() {

            public Predicate toPredicate(Root<ConfigEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preConfigName = cb.like(root.get("label"), "%" + qVo.getKeyword() + "%");
                    Predicate preConfigCode = cb.like(root.get("value"), "%" + qVo.getKeyword() + "%");
                    Predicate preConfigDescription = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preConfigName, preConfigCode, preConfigDescription));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public ConfigVO addOrEdit(IUConfigVO iuVo) {
        return ConfigVOEntityMapStruct.INSTANCE.toConfigVO(insertOrUpdate(iuVo));
    }

    public ConfigEntity insertOrUpdate(IUConfigVO iuVo) {
        List<ConfigEntity> list = repository
                .findAll(isExist(iuVo.getLabel(), iuVo.getValue(), iuVo.getId()));
        if (!list.isEmpty()) {
            throw new BaseException(EnumDataCheck.EXISTED);
        }
        ConfigEntity entity = null;
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            // 更新
            entity = getEntity(iuVo.getId());
            entity.setValue(iuVo.getValue());
            entity.setLabel(iuVo.getLabel());
            entity.setRemark(iuVo.getRemark());
        } else {
            // 追加
            entity = ConfigVOEntityMapStruct.INSTANCE.toConfigEntity(iuVo);
        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    /*
     * 基礎設定存在検証
     * email and name
     */
    public Specification<ConfigEntity> isExist(String dictName, String dictCode, String id) {
        return new Specification<ConfigEntity>() {

            public Predicate toPredicate(Root<ConfigEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preConfigName = cb.like(root.get("label"), "%" + dictName + "%");
                Predicate preConfigCode = cb.like(root.get("value"), "%" + dictCode + "%");
                predicate.add(cb.or(preConfigName, preConfigCode));
                if (null != id && !id.isEmpty()) {
                    predicate.add(cb.notEqual(root.get("id"), id));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 物理削除
     */

    public Boolean deletePhysics(String id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR, KEY + ":" + e.getMessage());
        }
        return true;
    }
}
