package TestDao;

import cn.AssassinG.ScsyERP.InStorage.core.dao.InStorageFormDao;
import cn.AssassinG.ScsyERP.InStorage.facade.entity.InStorageForm;
import cn.AssassinG.ScsyERP.InStorage.facade.enums.InStorageFormStatus;
import cn.AssassinG.ScsyERP.common.page.PageBean;
import cn.AssassinG.ScsyERP.common.page.PageParam;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-context.xml"})
public class TestInStorageForm {
    private static Logger logger = Logger.getLogger(TestInStorageForm.class);
    @Autowired
    private InStorageFormDao inStorageFormDao;

    @Test
    public void testGetById() {
        Long inStorageForm_id = 1L;
        InStorageForm inStorageForm = inStorageFormDao.getById(inStorageForm_id);
        if(inStorageForm == null || inStorageForm.getId() == null || inStorageForm.getId().longValue() != inStorageForm_id.longValue()){
            throw  new RuntimeException("getById failed");
        }else {
            logger.info("GetById success");
        }
    }

    @Test
    public void testInsert() {
        InStorageForm inStorageForm = new InStorageForm();
        inStorageForm.setCorporation(1L);
        inStorageForm.setProject(1L);
        inStorageForm.setInStorageStatus(InStorageFormStatus.Working);
        inStorageForm.setInStorageNumber("rkd123456");
        inStorageForm.setInStorageTime(new Date());
        inStorageForm.setWarehouse(1L);
        inStorageForm.setTruck(2L);
        inStorageForm.setPickWorker(1L);
        inStorageForm.setLister(1L);
        inStorageFormDao.insert(inStorageForm);
        Long id = inStorageForm.getId();
        if(id == null){
            throw new RuntimeException("insert nothing");
        }else {
            logger.info("Inserted : " + inStorageFormDao.getById(id));
        }
    }

    @Test
    public void testBatchInsert() {
        InStorageForm inStorageForm = new InStorageForm();
        inStorageForm.setCorporation(1L);
        inStorageForm.setProject(1L);
        inStorageForm.setInStorageStatus(InStorageFormStatus.Working);
        inStorageForm.setInStorageNumber("rkd123456");
        inStorageForm.setInStorageTime(new Date());
        inStorageForm.setWarehouse(1L);
        inStorageForm.setTruck(2L);
        inStorageForm.setPickWorker(1L);
        inStorageForm.setLister(1L);
        InStorageForm inStorageForm2 = new InStorageForm();
        inStorageForm2.setCorporation(1L);
        inStorageForm2.setProject(1L);
        inStorageForm2.setInStorageStatus(InStorageFormStatus.Working);
        inStorageForm2.setInStorageNumber("rkd123456");
        inStorageForm2.setInStorageTime(new Date());
        inStorageForm2.setWarehouse(1L);
        inStorageForm2.setTruck(2L);
        inStorageForm2.setPickWorker(1L);
        inStorageForm2.setLister(1L);
        List<InStorageForm> inStorageForms = new ArrayList<InStorageForm>();
        inStorageForms.add(inStorageForm);
        inStorageForms.add(inStorageForm2);
        int result = inStorageFormDao.insert(inStorageForms);
        if(result != inStorageForms.size()){
            throw new RuntimeException("BatchInsert failed " + (inStorageForms.size()-result) + "s, succeed " + result + "s");
        }else if(inStorageForm.getId() == null || inStorageForm2.getId() == null){
            throw new RuntimeException("BatchInsert failed");
        }else{
            logger.info("BatchInserted succeed");
        }
    }

    @Test
    public void testUpdate() {
        InStorageForm inStorageForm = inStorageFormDao.getById(2);
        logger.info("Before Update: "+inStorageForm);
        long tmp = inStorageForm.getWarehouse()+1;
        inStorageForm.setWarehouse(tmp);
        inStorageFormDao.update(inStorageForm);
        InStorageForm inStorageForm_check = inStorageFormDao.getById(2);
        if(inStorageForm_check.getWarehouse() != tmp){
            throw new RuntimeException("Update failed");
        }else{
            logger.info("Updated succeed");
        }
    }

    @Test
    public void testBatchUpdate() {
        InStorageForm inStorageForm2 = inStorageFormDao.getById(2);
        InStorageForm inStorageForm4 = inStorageFormDao.getById(4);
        long tmp1 = inStorageForm2.getWarehouse() + 1;
        long tmp2 = inStorageForm4.getWarehouse() + 1;
        inStorageForm2.setWarehouse(tmp1);
        inStorageForm4.setWarehouse(tmp2);
        List<InStorageForm> inStorageForms = new ArrayList<InStorageForm>();
        inStorageForms.add(inStorageForm2);
        inStorageForms.add(inStorageForm4);
        inStorageFormDao.update(inStorageForms);
        InStorageForm inStorageForm2_check = inStorageFormDao.getById(2);
        InStorageForm inStorageForm4_check = inStorageFormDao.getById(4);
        if(inStorageForm2_check.getWarehouse() != tmp1){
            throw new RuntimeException("InStorageForm2 update failed");
        }
        if(inStorageForm4_check.getWarehouse() != tmp2){
            throw new RuntimeException("InStorageForm4 update failed");
        }
        if(inStorageForm2_check.getWarehouse() != tmp1 && inStorageForm4_check.getWarehouse() != tmp2){
            logger.info("BatchUpdated succeed");
        }
    }

    @Test
    public void testDeleteById() {
        Long delete_id = 2L;
        inStorageFormDao.delete(delete_id);
        InStorageForm inStorageForm_check = inStorageFormDao.getById(delete_id);
        if(!inStorageForm_check.getIfDeleted()){
            throw new RuntimeException("Delete failed");
        }else {
            logger.info("Delete succeed");
        }
    }

    @Test
    public void testDelete() {
        Long delete_id = 4L;
        InStorageForm inStorageForm = inStorageFormDao.getById(delete_id);
        inStorageFormDao.delete(inStorageForm);
        InStorageForm inStorageForm_check = inStorageFormDao.getById(delete_id);
        if(!inStorageForm_check.getIfDeleted()){
            throw new RuntimeException("Delete failed");
        }else {
            logger.info("Delete succeed");
        }
    }

    @Test
    public void testListAll() {
        List<InStorageForm> inStorageForms = inStorageFormDao.listAll();
        for (int i = 0; i < inStorageForms.size(); i++)
            logger.info("Item" + i + ":" + inStorageForms.get(i));
    }

    @Test
    public void testGetBy() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ifDeleted", true);
        paramMap.put("Id", 1L);
        InStorageForm inStorageForm = inStorageFormDao.getBy(paramMap);
        if(inStorageForm.getId().longValue() != 1L){
            throw new RuntimeException("GetBy failed");
        }else{
            logger.info("GetBy succeed");
        }
    }

    @Test
    public void testListBy() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("IsDeleted", false);
        paramMap.put("InStorageFormName", "admi");
        List<InStorageForm> inStorageForms = inStorageFormDao.listBy(paramMap);
        for (int i = 0; i < inStorageForms.size(); i++)
            logger.info("Item" + i + ":" + inStorageForms.get(i));
    }

    @Test
    public void testListPage() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ifDeleted", false);
        PageParam pageParam = new PageParam(2, 2);
        PageBean<InStorageForm> pageBean = inStorageFormDao.listPage(pageParam, paramMap);
        logger.info(pageBean);
        List<InStorageForm> inStorageForms = pageBean.getRecordList();
//        if(inStorageForms.size() != 2){
//            throw new RuntimeException("ListPage failed");
//        }
        for (int i = 0; i < inStorageForms.size(); i++)
            logger.info("Item" + i + ":" + inStorageForms.get(i));
    }
}
