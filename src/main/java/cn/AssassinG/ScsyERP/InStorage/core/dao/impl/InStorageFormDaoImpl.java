package cn.AssassinG.ScsyERP.InStorage.core.dao.impl;

import cn.AssassinG.ScsyERP.InStorage.core.dao.InStorageFormDao;
import cn.AssassinG.ScsyERP.InStorage.facade.entity.InStorageForm;
import cn.AssassinG.ScsyERP.common.core.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository(value="InStorageFormDao")
public class InStorageFormDaoImpl extends BaseDaoImpl<InStorageForm> implements InStorageFormDao {
}
