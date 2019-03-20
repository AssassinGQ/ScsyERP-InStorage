package cn.AssassinG.ScsyERP.InStorage.core.biz.impl;

import cn.AssassinG.ScsyERP.BasicInfo.facade.entity.Product;
import cn.AssassinG.ScsyERP.BasicInfo.facade.enums.ProductStatus;
import cn.AssassinG.ScsyERP.BasicInfo.facade.service.ProductServiceFacade;
import cn.AssassinG.ScsyERP.InStorage.core.biz.InStorageFormBiz;
import cn.AssassinG.ScsyERP.InStorage.core.dao.InStorageFormDao;
import cn.AssassinG.ScsyERP.InStorage.facade.entity.InStorageForm;
import cn.AssassinG.ScsyERP.InStorage.facade.enums.InStorageFormStatus;
import cn.AssassinG.ScsyERP.InStorage.facade.exceptions.InStorageFormBizException;
import cn.AssassinG.ScsyERP.common.core.biz.impl.FormBizImpl;
import cn.AssassinG.ScsyERP.common.core.dao.BaseDao;
import cn.AssassinG.ScsyERP.common.enums.AccountStatus;
import cn.AssassinG.ScsyERP.common.utils.ValidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component("InStorageFormBiz")
public class InStorageFormBizImpl extends FormBizImpl<InStorageForm> implements InStorageFormBiz {
    @Autowired
    private InStorageFormDao inStorageFormDao;
    protected BaseDao<InStorageForm> getDao() {
        return this.inStorageFormDao;
    }

    @Override
    public Long create(InStorageForm inStorageForm) {
        if(inStorageForm.getInStorageNumber() == null){
            inStorageForm.setInStorageNumber(String.valueOf(System.currentTimeMillis()));
        }
        if(inStorageForm.getAccountStatus() == null) {
            inStorageForm.setAccountStatus(cn.AssassinG.ScsyERP.common.enums.AccountStatus.WRZ);
        }
        inStorageForm.setInStorageStatus(InStorageFormStatus.Working);
        inStorageForm.setProducts(new HashSet<>());
        inStorageForm.setInStorageTime(new Date());
        ValidUtils.ValidationWithExp(inStorageForm);
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("IfDeleted", false);
        queryMap.put("Warehouse", inStorageForm.getWarehouse());
        queryMap.put("IfCompleted", false);
        List<InStorageForm> inStorageForms = inStorageFormDao.listBy(queryMap);
        if(inStorageForms.size() > 1){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_UNKNOWN_ERROR, "仓库（主键：%d）存在多个活跃的入库单", inStorageForm.getWarehouse());
        }else if(inStorageForms.size() == 1){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_UNKNOWN_ERROR, "当前仓库已经存在一个活跃的入库单，仓库主键：%d", inStorageForms.get(0).getWarehouse());
        }else{
            Long id = getDao().insert(inStorageForm);
            if(!inStorageForm.getInStorageNumber().startsWith("rkd")){
                inStorageForm.setInStorageNumber("rkd" + id);
                getDao().update(inStorageForm);
            }
            return id;
        }
    }

//    public void update(InStorageForm inStorageForm) {
//        ValidUtils.ValidationWithExp(inStorageForm);
//        InStorageForm inStorageForm_check = this.getById(inStorageForm.getId());
//        if(inStorageForm_check == null || inStorageForm_check.getIfDeleted()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "该入库单已被删除，entityId: %d", inStorageForm.getId());
//        }
//        if(inStorageForm_check.getIfCompleted()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "该入库单已经完成，不能更新");
//        }
//        getDao().update(inStorageForm);
//    }

    /**
     * @param entityId
     * @param paramMap 入库单基本信息字段(warehouse,truck,pickWorker,lister,accountStatus,totalAmount,totalVolume,totalWeight)
     */
    @Transactional
    public void updateByMap(Long entityId, Map<String, String> paramMap) {
        if(entityId == null){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "入库单基本信息主键不能为空");
        }
        InStorageForm inStorageForm = this.getById(entityId);
        if(inStorageForm == null || inStorageForm.getIfDeleted()){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的入库单基本信息，entityId: %d", entityId);
        }
        try{
            Long warehouse = paramMap.get("warehouse") == null ? null : Long.valueOf(paramMap.get("warehouse"));
            Long truck = paramMap.get("truck") == null ? null : Long.valueOf(paramMap.get("truck"));
            Long pickWorker = paramMap.get("pickWorker") == null ? null : Long.valueOf(paramMap.get("pickWorker"));
            Long lister = paramMap.get("lister") == null ? null : Long.valueOf(paramMap.get("lister"));
            String accountStatusstr = paramMap.get("accountStatus");
            Integer totalAmount = paramMap.get("totalAmount") == null ? null : Integer.valueOf(paramMap.get("totalAmount"));
            Double totalVolume = paramMap.get("totalVolume") == null ? null : Double.valueOf(paramMap.get("totalVolume"));
            Double totalWeight = paramMap.get("totalWeight") == null ? null : Double.valueOf(paramMap.get("totalWeight"));
            boolean flag = false;
            if(warehouse != null) {
                inStorageForm.setWarehouse(warehouse);
                flag = true;
            }
            if(truck != null) {
                inStorageForm.setWarehouse(truck);
                flag = true;
            }
            if(pickWorker != null) {
                inStorageForm.setWarehouse(pickWorker);
                flag = true;
            }
            if(lister != null) {
                inStorageForm.setWarehouse(lister);
                flag = true;
            }
            if(accountStatusstr != null) {
                if(accountStatusstr.equals("未入账"))
                    inStorageForm.setAccountStatus(AccountStatus.WRZ);
                else{
                    inStorageForm.setAccountStatus(AccountStatus.YRZ);
                }
                flag = true;
            }
            if(totalAmount != null) {
                inStorageForm.setTotalAmount(totalAmount);
                flag = true;
            }
            if(totalVolume != null) {
                inStorageForm.setTotalVolume(totalVolume);
                flag = true;
            }
            if(totalWeight != null) {
                inStorageForm.setTotalWeight(totalWeight);
                flag = true;
            }
            if (flag) {
                this.update(inStorageForm);
            }
        }catch(NumberFormatException e){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "参数格式错误："+e.getMessage());
        }
    }

//    @Autowired
//    private DriveWorkerServiceFacade driveWorkerServiceFacade;
//
//    @Transactional
//    public void addDriveWorkers(Long entityId, String jsonArrayStr) {
//        if(entityId == null){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "入库单基本信息主键不能为空");
//        }
//        if(jsonArrayStr == null || jsonArrayStr.isEmpty()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "行车工基本信息主键不能为空");
//        }
//        JSONArray jsonArray;
//        try{
//            jsonArray = JSONArray.parseArray(jsonArrayStr);
//        }catch(Exception e){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "行车工基本信息主键格式不正确");
//        }
//        InStorageForm inStorageForm = this.getById(entityId);
//        if(inStorageForm == null || inStorageForm.getIfDeleted()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的入库单基本信息，entityId: %d", entityId);
//        }
//        boolean flag = true;
//        for(int i = 0; i < jsonArray.size(); i++){
//            DriveWorker driveWorker_tmp = driveWorkerServiceFacade.getById(jsonArray.getLong(i));
//            if(driveWorker_tmp == null || driveWorker_tmp.getIfDeleted()){
//                flag = false;
//            }else{
//                inStorageForm.getDriveWorkers().add(driveWorker_tmp.getId());
//            }
//        }
//        this.update(inStorageForm);
//        if(!flag){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_UNKNOWN_ERROR, "一部分行车工主键没有对应的记录导致这部分主键没有添加到", entityId);
//        }
//    }
//
//    @Transactional
//    public void removeDriveWorker(Long entityId, Long driveWorkerId) {
//        if(entityId == null){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "入库单基本信息主键不能为空");
//        }
//        if(driveWorkerId == null){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "行车工基本信息主键不能为空");
//        }
//        InStorageForm inStorageForm = this.getById(entityId);
//        if(inStorageForm == null || inStorageForm.getIfDeleted()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的入库单基本信息，entityId: %d", entityId);
//        }
//        DriveWorker driveWorker = driveWorkerServiceFacade.getById(driveWorkerId);
//        if(driveWorker == null || driveWorker.getIfDeleted()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的行车工基本信息，entityId: %d", entityId);
//        }
//        inStorageForm.getDriveWorkers().remove(driveWorker.getId());
//        this.update(inStorageForm);
//    }
//
//    @Autowired
//    private LiftWorkerServiceFacade liftWorkerServiceFacade;
//
//    @Transactional
//    public void addLiftWorkers(Long entityId, String jsonArrayStr) {
//        if(entityId == null){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "入库单基本信息主键不能为空");
//        }
//        if(jsonArrayStr == null || jsonArrayStr.isEmpty()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "起重工基本信息主键不能为空");
//        }
//        JSONArray jsonArray;
//        try{
//            jsonArray = JSONArray.parseArray(jsonArrayStr);
//        }catch(Exception e){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "起重工基本信息主键格式不正确");
//        }
//        InStorageForm inStorageForm = this.getById(entityId);
//        if(inStorageForm == null || inStorageForm.getIfDeleted()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的入库单基本信息，entityId: %d", entityId);
//        }
//        boolean flag = true;
//        for(int i = 0; i < jsonArray.size(); i++){
//            LiftWorker liftWorker_tmp = liftWorkerServiceFacade.getById(jsonArray.getLong(i));
//            if(liftWorker_tmp == null || liftWorker_tmp.getIfDeleted()){
//                flag = false;
//            }else{
//                inStorageForm.getLiftWorkers().add(liftWorker_tmp.getId());
//            }
//        }
//        this.update(inStorageForm);
//        if(!flag){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_UNKNOWN_ERROR, "一部分起重工主键没有对应的记录导致这部分主键没有添加到", entityId);
//        }
//    }
//
//    @Transactional
//    public void removeLiftWorker(Long entityId, Long liftWorkerId) {
//        if(entityId == null){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "入库单基本信息主键不能为空");
//        }
//        if(liftWorkerId == null){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "起重工基本信息主键不能为空");
//        }
//        InStorageForm inStorageForm = this.getById(entityId);
//        if(inStorageForm == null || inStorageForm.getIfDeleted()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的入库单基本信息，entityId: %d", entityId);
//        }
//        LiftWorker liftWorker = liftWorkerServiceFacade.getById(liftWorkerId);
//        if(liftWorker == null || liftWorker.getIfDeleted()){
//            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的起重工基本信息，entityId: %d", entityId);
//        }
//        inStorageForm.getLiftWorkers().remove(liftWorker.getId());
//        this.update(inStorageForm);
//    }

    @Autowired
    private ProductServiceFacade productServiceFacade;

    @Transactional
    public void addProduct(Long entityId, Long productId) {
        if(entityId == null){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "入库单基本信息主键不能为空");
        }
        if(productId == null){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "货物基本信息主键不能为空");
        }
        InStorageForm inStorageForm = this.getById(entityId);
        if(inStorageForm == null || inStorageForm.getIfDeleted()){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的入库单基本信息，entityId: %d", entityId);
        }
        Product product = productServiceFacade.getById(productId);
        if(product == null || product.getIfDeleted()){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的货物基本信息，entityId: %d", entityId);
        }
        if(product.getStatus().getValue() != ProductStatus.DRK.getValue()){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "该货物已经入库，entityId: %d", product.getId());
        }
        product.setInStorageForm(inStorageForm.getId());
        product.setStatus(ProductStatus.YRK);
        product.setWarehouse(inStorageForm.getWarehouse());
        inStorageForm.getProducts().add(product.getId());
        inStorageForm.setWorkingProduct(product.getId());
        this.update(inStorageForm);
        productServiceFacade.update(product);
    }

    @Transactional
    public void removeProduct(Long entityId, Long productId) {
        if(entityId == null){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "入库单基本信息主键不能为空");
        }
        if(productId == null){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "货物基本信息主键不能为空");
        }
        InStorageForm inStorageForm = this.getById(entityId);
        if(inStorageForm == null || inStorageForm.getIfDeleted()){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的入库单基本信息，entityId: %d", entityId);
        }
        Product product = productServiceFacade.getById(productId);
        if(product == null){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的货物基本信息，entityId: %d", entityId);
        }
        if(product.getStatus().getValue() == ProductStatus.DRK.getValue()){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "该货物尚未入库，entityId: %d", product.getId());
        }
        if(product.getStatus().getValue() == ProductStatus.YCK.getValue()){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "该货物已经出库，entityId: %d", product.getId());
        }
        product.setStatus(ProductStatus.DRK);
        product.setInStorageForm(null);
        inStorageForm.getProducts().remove(product.getId());
        if(inStorageForm.getWorkingProduct() != null && inStorageForm.getWorkingProduct().longValue() == product.getId()){
            inStorageForm.setWorkingProduct(null);//todo 如果这个为空，货物定位上传了该怎么办。可以实现setWorkingProduct接口
        }
        this.update(inStorageForm);
        productServiceFacade.update(product);
    }

    @Override
    @Transactional
    public void updateProductLocation(Long Warehouse, String Location) {
        if(Warehouse == null){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "仓库基本信息主键不能为空");
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("IfDeleted", false);
        queryMap.put("Warehouse", Warehouse);
        queryMap.put("IfCompleted", false);
        List<InStorageForm> inStorageForms = inStorageFormDao.listBy(queryMap);
        if(inStorageForms.size() > 1){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_UNKNOWN_ERROR, "仓库（主键：%d）存在多个活跃的入库单", Warehouse);
        }else if(inStorageForms.size() == 0){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "当前仓库不存在活跃的入库单");
        }else{
            InStorageForm inStorageForm = inStorageForms.get(0);
            if(inStorageForm == null || inStorageForm.getIfDeleted()){
                throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "当前仓库不存在活跃的入库单");
            }
            if(inStorageForm.getWorkingProduct() == null){
                throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_CANNOTOPERATE, "请先指定正在入库的货物，入库单主键: %d", inStorageForm.getId());
            }
            if(inStorageForm.getWorkingProduct() != null){
                Product product = productServiceFacade.getById(inStorageForm.getWorkingProduct());
                if(product == null || product.getIfDeleted()){
                    throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_CANNOTOPERATE, "正在入库的货物已被删除");
                }
                if(product.getStatus().getValue() != ProductStatus.YRK.getValue()){
                    throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_CANNOTOPERATE, "货物当前不能上传定位信息");
                }
                product.setWarehouseLocation(Location);
                productServiceFacade.update(product);
            }
        }
    }

    @Override
    public void setWorkingProduct(Long entityId, Long productId) {
        //todo
    }

    @Override
    public void complete(Long entityId) {
        if(entityId == null){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_PARAMS_ILLEGAL, "入库单基本信息主键不能为空");
        }
        InStorageForm inStorageForm = this.getById(entityId);
        if(inStorageForm == null || inStorageForm.getIfDeleted()){
            throw new InStorageFormBizException(InStorageFormBizException.INSTORAGEFORMBIZ_NOSUIT_RESULT, "没有符合条件的入库单基本信息，entityId: %d", entityId);
        }
        Set<Long> products = inStorageForm.getProducts();
        Iterator<Long> iterator = products.iterator();
        int totalAmount = 0;
        double totalVolume = 0, totalWeight = 0;
        while(iterator.hasNext()){
            Product product = productServiceFacade.getById(iterator.next());
            if(product != null || !product.getIfDeleted()){
                totalAmount++;
                totalVolume += product.getVolume();
                totalWeight += product.getWeight();
            }else{
                iterator.remove();
            }
        }
        inStorageForm.setProducts(products);
        inStorageForm.setTotalAmount(totalAmount);
        inStorageForm.setTotalVolume(totalVolume);
        inStorageForm.setTotalWeight(totalWeight);
        inStorageForm.setIfCompleted(true);
        inStorageForm.setInStorageStatus(InStorageFormStatus.Done);
        this.update(inStorageForm);
    }
}
