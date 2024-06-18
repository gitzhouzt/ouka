package com.cbs.oukasystem.service.sys;

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
import com.cbs.oukasystem.entity.sys.RoleEntity;
import com.cbs.oukasystem.mapstruct.sys.RoleVOEntityMapStruct;
import com.cbs.oukasystem.repository.sys.RoleRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.sys.IURoleVO;
import com.cbs.oukasystem.vo.in.sys.QueryRoleVO;
import com.cbs.oukasystem.vo.out.sys.RoleVO;

@Service
@Transactional
public class RoleService {

    String KEY = "役職";

    @Autowired
    private RoleRepository repository;

    /*
     * 役職を得る
     */

    public RoleVO getById(String id) {
        return RoleVOEntityMapStruct.INSTANCE.toRoleVO(getEntity(id));
    }

    public RoleEntity getEntity(String id) {
        Optional<RoleEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id : " + id);
        }
        return optional.get();
    }

    /*
     * 全ての役職
     */

    public List<RoleVO> getAll() {
        return RoleVOEntityMapStruct.INSTANCE.toRoleVOs(findAll());
    }

    public List<RoleEntity> findAll() {
        return repository.findAll();
    }

    public List<RoleEntity> findAll(Specification<RoleEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * 役職のリスト
     */

    public ListVO<RoleVO> getPages(QueryRoleVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "roleCode");
        Specification<RoleEntity> specification = Search(qVo);
        Page<RoleEntity> pages = repository.findAll(specification, pageable);
        List<RoleVO> list = RoleVOEntityMapStruct.INSTANCE.toRoleVOs(pages.toList());
        ListVO<RoleVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 役職検索
     */
    public Specification<RoleEntity> Search(QueryRoleVO qVo) {
        return new Specification<RoleEntity>() {

            public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preRoleName = cb.like(root.get("roleName"), "%" + qVo.getKeyword() + "%");
                    Predicate preRoleCode = cb.like(root.get("roleCode"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preRoleName, preRoleCode));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public RoleVO addOrEdit(IURoleVO iuVo) {
        return RoleVOEntityMapStruct.INSTANCE.toRoleVO(insertOrUpdate(iuVo));
    }

    public RoleEntity insertOrUpdate(IURoleVO iuVo) {
        List<RoleEntity> list = repository
                .findAll(isExist(iuVo.getRoleName(), iuVo.getRoleCode(), iuVo.getId()));
        if (!list.isEmpty()) {
            throw new BaseException(EnumDataCheck.EXISTED);
        }
        RoleEntity entity = null;
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            // 更新
            entity = getEntity(iuVo.getId());
            entity.setRoleCode(iuVo.getRoleCode());
            entity.setRoleName(iuVo.getRoleName());
        } else {
            // 追加
            entity = RoleVOEntityMapStruct.INSTANCE.toRoleEntity(iuVo);
        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    /*
     * 役職存在検証
     * email and name
     */
    public Specification<RoleEntity> isExist(String cityName, String cityCode, String id) {
        return new Specification<RoleEntity>() {

            public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preRoleName = cb.like(root.get("roleName"), "%" + cityName + "%");
                Predicate preRoleCode = cb.like(root.get("roleCode"), "%" + cityCode + "%");
                predicate.add(cb.or(preRoleName, preRoleCode));
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
