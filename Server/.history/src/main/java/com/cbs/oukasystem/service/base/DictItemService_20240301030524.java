package com.cbs.oukasystem.service.base;

import java.util.List;
import java.util.Map;
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
import com.cbs.oukasystem.entity.base.DictItemEntity;
import com.cbs.oukasystem.mapstruct.base.DictItemVOEntityMapStruct;
import com.cbs.oukasystem.repository.base.DictItemRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.base.IUDictItemVO;
import com.cbs.oukasystem.vo.in.base.QueryDictItemVO;
import com.cbs.oukasystem.vo.out.base.DictItemVO;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

@Service
@Transactional
public class DictItemService {

    String KEY = "辞典ITEM";

    @Autowired
    private DictItemRepository repository;

    /*
     * 辞典ITEMを得る
     */

    public DictItemVO getById(String id) {
        return DictItemVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public DictItemEntity getEntity(String id) {
        Optional<DictItemEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    public DictItemVO getByCode(String dictCode, String itemCode) {
        return repository.findByDictCodeAndItemCode(dictCode, itemCode);
    }

    /*
     * 全ての辞典ITEM
     */

    public List<DictItemVO> getAll() {
        return DictItemVOEntityMapStruct.INSTANCE.toVOs(findAll());
    }

    public List<DictItemEntity> findAll() {
        return repository.findAll();
    }

    public List<DictItemVO> query(QueryDictItemVO qVo) {
        Specification<DictItemEntity> specification = Search(qVo);
        return DictItemVOEntityMapStruct.INSTANCE.toVOs(repository.findAll(specification));
    }

    /*
     * 辞典ITEMのリスト
     */

    public ListVO<DictItemVO> getPages(QueryDictItemVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "itemCode");
        Specification<DictItemEntity> specification = Search(qVo);
        Page<DictItemEntity> pages = repository.findAll(specification, pageable);
        List<DictItemVO> list = DictItemVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<DictItemVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 辞典ITEM検索
     */
    public Specification<DictItemEntity> Search(QueryDictItemVO qVo) {
        return new Specification<DictItemEntity>() {

            public Predicate toPredicate(Root<DictItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preDictItemName = cb.like(root.get("itemName"), "%" + qVo.getKeyword() + "%");
                    Predicate preDictItemCode = cb.like(root.get("itemCode"), "%" + qVo.getKeyword() + "%");
                    Predicate preDictItemDescription = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preDictItemName, preDictItemCode, preDictItemDescription));
                }
                if (null != qVo.getDictCode() && !qVo.getDictCode().isEmpty()) {
                    predicate.add(cb.equal(root.get("dictCode"), qVo.getDictCode()));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public DictItemVO addOrEdit(IUDictItemVO iuVo) {
        return DictItemVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public DictItemEntity insertOrUpdate(IUDictItemVO iuVo) {
        List<DictItemEntity> list = repository
                .findAll(isExist(iuVo, iuVo.getId()));
        if (!list.isEmpty()) {
            throw new BaseException(EnumDataCheck.EXISTED);
        }
        DictItemEntity entity = null;
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            // 更新
            entity = getEntity(iuVo.getId());
            entity.setItemName(iuVo.getItemName());
            entity.setItemCode(iuVo.getItemCode());
            entity.setRemark(iuVo.getRemark());
        } else {
            // 追加
            entity = DictItemVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        }
        if (null == iuVo.getItemCode() || iuVo.getItemCode().isEmpty()) {
            entity.setItemCode(iuVo.getItemName());
        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    public Boolean updateAllItemByDictCode(String newCode, String oldCode) {
        repository.updateAllItemByDictCode(newCode, oldCode);
        return true;
    }

    /*
     * 辞典ITEM存在検証
     * email and name
     */
    public Specification<DictItemEntity> isExist(IUDictItemVO iuVo, String id) {
        return new Specification<DictItemEntity>() {

            public Predicate toPredicate(Root<DictItemEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preDictItemName = cb.like(root.get("itemName"), "%" + iuVo.getItemName() + "%");
                Predicate preDictItemCode = cb.like(root.get("itemCode"), "%" + iuVo.getItemCode() + "%");
                Predicate preDictCode = cb.like(root.get("dictCode"), "%" + iuVo.getDictCode() + "%");
                predicate.add(cb.and(preDictItemName, preDictItemCode, preDictCode));
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
