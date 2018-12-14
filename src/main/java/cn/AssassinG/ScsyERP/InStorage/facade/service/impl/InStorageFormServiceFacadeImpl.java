package cn.AssassinG.ScsyERP.InStorage.facade.service.impl;

import cn.AssassinG.ScsyERP.InStorage.core.biz.InStorageFormBiz;
import cn.AssassinG.ScsyERP.InStorage.facade.entity.InStorageForm;
import cn.AssassinG.ScsyERP.InStorage.facade.service.InStorageFormServiceFacade;
import cn.AssassinG.ScsyERP.common.core.biz.FormBiz;
import cn.AssassinG.ScsyERP.common.core.service.impl.FormServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("InStorageFormServiceFacade")
public class InStorageFormServiceFacadeImpl extends FormServiceImpl<InStorageForm> implements InStorageFormServiceFacade {
    @Autowired
    private InStorageFormBiz inStorageFormBiz;

    @Override
    protected FormBiz<InStorageForm> getFormBiz() {
        return this.inStorageFormBiz;
    }

    @Override
    public void addDriveWorkers(Long entityId, String jsonArrayStr) {
        this.inStorageFormBiz.addDriveWorkers(entityId, jsonArrayStr);
    }

    @Override
    public void removeDriveWorker(Long entityId, Long driveWorkerId) {
        this.inStorageFormBiz.removeDriveWorker(entityId, driveWorkerId);
    }

    @Override
    public void addLiftWorkers(Long entityId, String jsonArrayStr) {
        this.inStorageFormBiz.addLiftWorkers(entityId, jsonArrayStr);
    }

    @Override
    public void removeLiftWorker(Long entityId, Long liftWorkerId) {
        this.inStorageFormBiz.removeLiftWorker(entityId, liftWorkerId);
    }

    @Override
    public void addProduct(Long entityId, Long productId) {
        this.inStorageFormBiz.addProduct(entityId, productId);
    }

    @Override
    public void removeProduct(Long entityId, Long productId) {
        this.inStorageFormBiz.removeProduct(entityId, productId);
    }

    @Override
    public void updateProductLocation(Long entityId, String Location) {
        this.inStorageFormBiz.updateProductLocation(entityId, Location);
    }
}
