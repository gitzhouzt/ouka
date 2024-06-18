package com.cbs.oukasystem.service.base;

import java.util.List;
import java.util.Optional;
import java.io.FileInputStream;
import java.io.InputStream;
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
import org.springframework.web.multipart.MultipartFile;

import com.cbs.oukasystem.common.CommonUtils;
import com.cbs.oukasystem.common.CsvUtils;
import com.cbs.oukasystem.common.FileUploadUtil;
import com.cbs.oukasystem.common.LoginUtils;
import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumUploadCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.base.DictEntity;
import com.cbs.oukasystem.entity.base.DictItemEntity;
import com.cbs.oukasystem.mapstruct.base.DictVOEntityMapStruct;
import com.cbs.oukasystem.repository.base.DictRepository;
import com.cbs.oukasystem.vo.ListVO;
import com.cbs.oukasystem.vo.in.base.IUDictVO;
import com.cbs.oukasystem.vo.in.base.QueryDictVO;
import com.cbs.oukasystem.vo.in.user.IUUserRestVO;
import com.cbs.oukasystem.vo.out.base.DictVO;

@Service
@Transactional
public class DictService {

    String KEY = "辞典";

    @Autowired
    private DictRepository repository;

    @Autowired
    private DictItemService itemService;

    /*
     * 辞典を得る
     */

    public DictVO getById(String id) {
        return DictVOEntityMapStruct.INSTANCE.toVO(getEntity(id));
    }

    public DictEntity getEntity(String id) {
        Optional<DictEntity> optional = repository.findById(id);
        if (false == optional.isPresent()) {
            throw new BaseException(EnumDataCheck.EMPTY, KEY + "Id:" + id);
        }
        return optional.get();
    }

    /*
     * 全ての辞典
     */

    public List<DictVO> getAll() {
        return DictVOEntityMapStruct.INSTANCE.toVOs(findAll());
    }

    public List<DictEntity> findAll() {
        return repository.findAll();
    }

    public List<DictEntity> findAll(Specification<DictEntity> spec) {
        return repository.findAll(spec);
    }

    /*
     * 辞典のリスト
     */

    public ListVO<DictVO> getPages(QueryDictVO qVo) {
        Pageable pageable = PageRequest.of(qVo.getPage() - 1, qVo.getPageSize(),
                Sort.Direction.DESC, "dictCode");
        Specification<DictEntity> specification = Search(qVo);
        Page<DictEntity> pages = repository.findAll(specification, pageable);
        List<DictVO> list = DictVOEntityMapStruct.INSTANCE.toVOs(pages.toList());
        ListVO<DictVO> listDto = new ListVO<>();
        listDto.setItemCount(pages.getTotalElements());
        listDto.setPage(qVo.getPage());
        listDto.setPageSize(qVo.getPageSize());
        listDto.setPageCount(pages.getTotalPages());
        listDto.setList(list);
        return listDto;
    }

    /*
     * 辞典検索
     */
    public Specification<DictEntity> Search(QueryDictVO qVo) {
        return new Specification<DictEntity>() {

            public Predicate toPredicate(Root<DictEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                if (null != qVo.getKeyword() && !qVo.getKeyword().isEmpty()) {
                    Predicate preDictName = cb.like(root.get("dictName"), "%" + qVo.getKeyword() + "%");
                    Predicate preDictCode = cb.like(root.get("dictCode"), "%" + qVo.getKeyword() + "%");
                    Predicate preDictDescription = cb.like(root.get("remark"), "%" + qVo.getKeyword() + "%");
                    predicate.add(cb.or(preDictName, preDictCode, preDictDescription));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * 追加 or 編集
     */

    public DictVO addOrEdit(IUDictVO iuVo) {
        return DictVOEntityMapStruct.INSTANCE.toVO(insertOrUpdate(iuVo));
    }

    public DictEntity insertOrUpdate(IUDictVO iuVo) {
        List<DictEntity> list = repository
                .findAll(isExist(iuVo, iuVo.getId()));
        if (!list.isEmpty()) {
            throw new BaseException(EnumDataCheck.EXISTED);
        }
        DictEntity entity = null;
        if (null != iuVo.getId() && !iuVo.getId().isEmpty()) {
            // 更新
            entity = getEntity(iuVo.getId());
            String newCode = iuVo.getDictCode();
            String oldCode = entity.getDictCode();
            entity.setDictName(iuVo.getDictName());
            entity.setDictCode(newCode);
            entity.setRemark(iuVo.getRemark());

            // 子项全部更新
            itemService.updateAllItemByDictCode(newCode, oldCode);
        } else {
            // 追加
            entity = DictVOEntityMapStruct.INSTANCE.toEntity(iuVo);
        }
        entity = repository.save(entity);
        if (null == entity) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return entity;
    }

    /*
     * 辞典存在検証
     * email and name
     */
    public Specification<DictEntity> isExist(IUDictVO iuVo, String id) {
        return new Specification<DictEntity>() {

            public Predicate toPredicate(Root<DictEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preDictName = cb.like(root.get("dictName"), "%" + iuVo.getDictName() + "%");
                Predicate preDictCode = cb.like(root.get("dictCode"), "%" + iuVo.getDictCode() + "%");
                predicate.add(cb.or(preDictName, preDictCode));
                if (null != id && !id.isEmpty()) {
                    predicate.add(cb.notEqual(root.get("id"), id));
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }

    /*
     * BATCH
     */
    public Boolean batch(String key, MultipartFile... files) {
        try {
            String suffix = FileUploadUtil.getSuffixName(files);
            String path = FileUploadUtil.upload(key, LoginUtils.getLoginUserId(), files);

            List<List<String>> rows = null;
            InputStream inputStream = new FileInputStream(FileUploadUtil.uploadPath + path);
            if (suffix.equals(".xls") || suffix.equals(".csv")) {
                rows = CsvUtils.readCsvFile(inputStream);
            } else {
                new BaseException(EnumUploadCheck.IMAGE_FORMAT);
            }
            int i = 0;
            if (null == rows) {
                return true;
            }
            List<DictItemEntity> dictItemEntities = new ArrayList<>();
            for (List<String> cols : rows) {
                if (i == 0) {
                    continue;
                }
                String dictId = cols.get(0).replace(" ", "");
                if (null != dictId && !dictId.isEmpty()) {
                    String dictCode = cols.get(1).replace(" ", "");
                    String itemName = cols.get(2).replace(" ", "");
                    String itemCode = cols.get(3).replace(" ", "");
                    String remark = cols.get(4).replace(" ", "");

                    DictItemEntity entity = new DictItemEntity();
                    entity.setDictCode(dictCode);
                    entity.setDictId(dictId);
                    entity.setItemCode(itemCode);
                    entity.setItemName(itemName);
                    entity.setRemark(remark);
                }
                i++;
            }
        } catch (Exception e) {
            throw new BaseException(EnumUploadCheck.ERROR, e.getMessage());
        }
        return true;
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
