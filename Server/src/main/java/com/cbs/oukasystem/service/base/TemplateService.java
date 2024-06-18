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
import com.cbs.oukasystem.entity.base.TemplateEntity;
import com.cbs.oukasystem.mapstruct.base.TemplateVOEntityMapStruct;
import com.cbs.oukasystem.repository.base.TemplateRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.base.IUTemplateVO;
import com.cbs.oukasystem.vo.in.base.QueryTemplateVO;
import com.cbs.oukasystem.vo.out.base.TemplateVO;

@Service
@Transactional
public class TemplateService {

    String KEY = "レンプレート";

    @Autowired
    private TemplateRepository repository;

    /*
     * レンプレートを得る
     */

    public TemplateVO getById(String id) {
        return TemplateVOEntityMapStruct.INSTANCE.toTemplateVO(getEntity(id));
    }

    public TemplateEntity getEntity(String id) {
        Optional<TemplateEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全てのレンプレート
     */

    public List<TemplateVO> getAll() {
        return TemplateVOEntityMapStruct.INSTANCE.toTemplateVOs(findAll());
    }

    public List<TemplateEntity> findAll() {
        return repository.findAll();
    }

    public List<TemplateEntity> findAll(Specification<TemplateEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * レンプレートのリスト
     */

    public ListVO<TemplateVO> getPages(QueryTemplateVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "dictCode");
        Specification<TemplateEntity> specification = Search(qVo);
        Page<TemplateEntity> pages = repository.findAll(specification, pageable);
        List<TemplateVO> list = TemplateVOEntityMapStruct.INSTANCE.toTemplateVOs(pages.toList());
        ListVO<TemplateVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * レンプレート検索
     */
    public Specification<TemplateEntity> Search(QueryTemplateVO qVo) {
        return new Specification<TemplateEntity>() {

            public Predicate toPredicate(Root<TemplateEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preTemplateName = cb.like(root.get("label"), "%" + qVo.getKeyword() + "%");
                    Predicate preTemplateDescription = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preTemplateName, preTemplateDescription));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public TemplateVO addOrEdit(IUTemplateVO iuVo) {
        return TemplateVOEntityMapStruct.INSTANCE.toTemplateVO(insertOrUpdate(iuVo));
    }

    public TemplateEntity insertOrUpdate(IUTemplateVO iuVo) {
        List<TemplateEntity> list = repository
                .findAll(isExist(iuVo.getLabel(), iuVo.getId()));
        if (!list.isEmpty()) {
            throw new BaseException(EnumDataCheck.EXISTED);
        }
        TemplateEntity entity = null;
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            // 更新
            entity = getEntity(iuVo.getId());
            entity.setUrl(iuVo.getUrl());
            entity.setLabel(iuVo.getLabel());
            entity.setType(iuVo.getType());
            entity.setRemark(iuVo.getRemark());
        } else {
            // 追加
            entity = TemplateVOEntityMapStruct.INSTANCE.toTemplateEntity(iuVo);
        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    /*
     * レンプレート存在検証
     * email and name
     */
    public Specification<TemplateEntity> isExist(String label, String id) {
        return new Specification<TemplateEntity>() {

            public Predicate toPredicate(Root<TemplateEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preTemplateName = cb.like(root.get("label"), "%" + label + "%");
                predicate.add(cb.or(preTemplateName));
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
