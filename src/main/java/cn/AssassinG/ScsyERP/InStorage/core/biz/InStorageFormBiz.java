package cn.AssassinG.ScsyERP.InStorage.core.biz;

import cn.AssassinG.ScsyERP.InStorage.facade.entity.InStorageForm;
import cn.AssassinG.ScsyERP.common.core.biz.FormBiz;

public interface InStorageFormBiz extends FormBiz<InStorageForm> {
    void addDriveWorkers(Long entityId, String jsonArrayStr);
    void removeDriveWorker(Long entityId, Long driveWorkerId);
    void addLiftWorkers(Long entityId, String jsonArrayStr);
    void removeLiftWorker(Long entityId, Long liftWorkerId);
    void addProduct(Long entityId, Long productId);
    void removeProduct(Long entityId, Long productId);
    void updateProductLocation(Long Warehouse, String Location);
}
