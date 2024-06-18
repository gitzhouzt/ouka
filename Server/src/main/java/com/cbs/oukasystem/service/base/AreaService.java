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
import com.cbs.oukasystem.entity.base.AreaEntity;
import com.cbs.oukasystem.mapstruct.base.AreaVOEntityMapStruct;
import com.cbs.oukasystem.repository.base.AreaRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.base.IUAreaVO;
import com.cbs.oukasystem.vo.in.base.QueryAreaVO;
import com.cbs.oukasystem.vo.out.base.AreaVO;

@Service
@Transactional
public class AreaService {

    String KEY = "地域";

    @Autowired
    private AreaRepository repository;

    /*
     * 地域を得る
     */

    public AreaVO getById(String id) {
        return AreaVOEntityMapStruct.INSTANCE.toAreaVO(getEntity(id));
    }

    public AreaEntity getEntity(String id) {
        Optional<AreaEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id : " + id);
        }
        return optional.get();
    }

    /*
     * 全ての地域
     */

    public List<AreaVO> getAll() {
        return AreaVOEntityMapStruct.INSTANCE.toAreaVOs(findAll());
    }

    public List<AreaEntity> findAll() {
        return repository.findAll();
    }

    public List<AreaEntity> findAll(Specification<AreaEntity> spec) {
        return repository.findAll(spec);
    }

    public List<AreaEntity> findByAreaCodes(List<String> areaCodes) {
        return repository.findByAreaCodes(areaCodes);
    }

    public ListVO<AreaVO> queryAllArea(QueryAreaVO qVo) {
        List<AreaEntity> pages = repository.queryAllArea(qVo.getKeyword(), (qVo.getPage() - 1) * qVo.getPageSize(),
                qVo.getPageSize());
        List<AreaVO> list = AreaVOEntityMapStruct.INSTANCE.toAreaVOs(pages);
        ListVO<AreaVO> listDto = new ListVO<>();
        long itemCnt = repository.countAllArea(qVo.getKeyword());
        int pageCnt = (int) itemCnt / qVo.getPageSize() + (itemCnt % qVo.getPageSize() == 0 ? 0 : 1);
        listDto.setItemCount(itemCnt);
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pageCnt);
        listDto.setList(list);
        return listDto;
    }

    /*
     * 地域のリスト
     */

    public ListVO<AreaVO> getPages(QueryAreaVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.ASC, "areaId");
        Specification<AreaEntity> specification = Search(qVo);
        Page<AreaEntity> pages = repository.findAll(specification, pageable);
        List<AreaVO> list = AreaVOEntityMapStruct.INSTANCE.toAreaVOs(pages.toList());
        ListVO<AreaVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 地域検索
     */
    public Specification<AreaEntity> Search(QueryAreaVO qVo) {
        return new Specification<AreaEntity>() {

            public Predicate toPredicate(Root<AreaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preAreaId = cb.like(root.get("areaId"), "%" + qVo.getKeyword() + "%");
                    Predicate preAreaName = cb.like(root.get("areaName"), "%" + qVo.getKeyword() + "%");
                    Predicate preAreaCode = cb.like(root.get("areaCode"), "%" + qVo.getKeyword() + "%");
                    Predicate preCityName = cb.like(root.get("cityName"), "%" + qVo.getKeyword() + "%");
                    Predicate preCityCode = cb.like(root.get("cityCode"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preAreaId, preAreaName, preAreaCode, preCityCode, preCityName));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public AreaVO addOrEdit(IUAreaVO iuVo) {
        return AreaVOEntityMapStruct.INSTANCE.toAreaVO(insertOrUpdate(iuVo));
    }

    public AreaEntity insertOrUpdate(IUAreaVO iuVo) {
        List<AreaEntity> list = repository
                .findAll(isExist(iuVo.getCityName(), iuVo.getCityCode(), iuVo.getId()));
        if (!list.isEmpty()) {
            throw new BaseException(EnumDataCheck.EXISTED);
        }
        AreaEntity entity = null;
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            // 更新
            entity = getEntity(iuVo.getId());
            entity.setAreaCode(iuVo.getAreaCode());
            entity.setAreaName(iuVo.getAreaName());
            entity.setCityCode(iuVo.getCityCode());
            entity.setCityName(iuVo.getCityName());
            entity.setAreaId(iuVo.getAreaCode() + iuVo.getCityCode());
        } else {
            // 追加
            entity = AreaVOEntityMapStruct.INSTANCE.toAreaEntity(iuVo);
            entity.setAreaId(iuVo.getAreaCode() + iuVo.getCityCode());
        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    /*
     * 地域存在検証
     * email and name
     */
    public Specification<AreaEntity> isExist(String cityName, String cityCode, String id) {
        return new Specification<AreaEntity>() {

            public Predicate toPredicate(Root<AreaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preCityName = cb.like(root.get("cityName"), "%" + cityName + "%");
                Predicate preCityCode = cb.like(root.get("cityCode"), "%" + cityCode + "%");
                predicate.add(cb.or(preCityName, preCityCode));
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
