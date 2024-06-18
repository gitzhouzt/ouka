package com.cbs.oukasystem.service.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cbs.oukasystem.common.MessageEnum.EnumDataCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumDeleteCheck;
import com.cbs.oukasystem.common.MessageEnum.EnumIOUCheck;
import com.cbs.oukasystem.config.BaseException;
import com.cbs.oukasystem.entity.sys.MenuEntity;
import com.cbs.oukasystem.mapstruct.sys.MenuVOEntityMapStruct;
import com.cbs.oukasystem.repository.sys.MenuRepository;
import com.cbs.oukasystem.vo.ResultVO;
import com.cbs.oukasystem.vo.in.sys.IUMenuVO;
import com.cbs.oukasystem.vo.out.sys.MenuVO;

import org.springframework.data.jpa.domain.Specification;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuRepository repository;

    /*
     * idによってメニューを得る
     */

    public ResultVO<MenuVO> getById(String id) {
        MenuVO outMenuVO = MenuVOEntityMapStruct.INSTANCE.toMenuVO(getEntity(id));
        return ResultVO.success(outMenuVO);
    }

    /*
     * 全てのメニュー
     */

    public ResultVO<List<MenuVO>> getAll() {
        List<MenuEntity> menus = repository.findAll();
        List<MenuVO> list = MenuVOEntityMapStruct.INSTANCE.toMenuVOs(menus);
        return ResultVO.success(list);
    }

    /*
     * 追加 or 編集
     */

    public ResultVO<Boolean> insertOrUpdate(IUMenuVO iuMenuVO) {
        MenuEntity newMenu = MenuVOEntityMapStruct.INSTANCE.toMenuEntity(iuMenuVO);
        List<MenuEntity> list = repository
                .findAll(isExist(newMenu.getName()));
        if (!list.isEmpty()) {
            throw new BaseException(EnumDataCheck.MENU_EXISTED);
        }
        if (null == repository.save(newMenu)) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return ResultVO.success(true);
    }

    /*
     * 削除
     */

    public ResultVO<Boolean> delete(String id) {
        try {
            repository.delete(getEntity(id));
        } catch (Exception e) {
            throw new BaseException(EnumDeleteCheck.ERROR);
        }
        return ResultVO.success(true);
    }

    /*
     * 有効審査 true 有効 false 無効
     */

    public ResultVO<Boolean> audit(String id) {
        MenuEntity user = getEntity(id);
        user.setIsAudit(!user.getIsAudit());
        if (null == repository.save(user)) {
            throw new BaseException(EnumIOUCheck.ERROR);
        }
        return ResultVO.success(true);
    }

    /*
     * メニューを得る
     */
    public MenuEntity getEntity(String id) {
        Optional<MenuEntity> uOptional = repository.findById(id);
        if (false == uOptional.isPresent()) {
            throw new BaseException(EnumDataCheck.NOT_EXIST);
        }
        return uOptional.get();
    }

    /*
     * メニュー存在検証
     * email and name
     */
    public Specification<MenuEntity> isExist(String name) {
        return new Specification<MenuEntity>() {

            public Predicate toPredicate(Root<MenuEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<>();
                Predicate preMenuName = cb.equal(root.get("name"), name);
                predicate.add(cb.or(preMenuName));
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        };
    }
}
