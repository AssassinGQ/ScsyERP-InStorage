package TestService;

import cn.AssassinG.ScsyERP.InStorage.core.biz.InStorageFormBiz;
import cn.AssassinG.ScsyERP.InStorage.core.dao.InStorageFormDao;
import cn.AssassinG.ScsyERP.InStorage.facade.entity.InStorageForm;
import cn.AssassinG.ScsyERP.common.enums.AccountStatus;
import cn.AssassinG.ScsyERP.InStorage.facade.enums.InStorageFormStatus;
import cn.AssassinG.ScsyERP.InStorage.facade.service.InStorageFormServiceFacade;
import cn.AssassinG.ScsyERP.common.page.PageBean;
import cn.AssassinG.ScsyERP.common.page.PageParam;
import cn.AssassinG.ScsyERP.common.utils.StringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-context.xml"})
public class InStorageFormServiceFacadeImplTest {
    private static Logger logger = Logger.getLogger(InStorageFormServiceFacadeImplTest.class);
    @Autowired
    private InStorageFormServiceFacade inStorageFormService;
    @Autowired
    private InStorageFormBiz inStorageFormBiz;
    @Autowired
    private InStorageFormDao inStorageFormDao;

    private Long inStorageFormId;

    @Before
    public void setUp() throws Exception {
        InStorageForm inStorageForm = new InStorageForm();
        inStorageForm.setCorporation(1L);
        inStorageForm.setProject(1L);
        inStorageForm.setInStorageStatus(InStorageFormStatus.Working);
        inStorageForm.setInStorageNumber(StringUtils.getRandomStr(8));
        inStorageForm.setInStorageTime(new Date());
        inStorageForm.setWarehouse(1L);
        inStorageForm.setTruck(1L);
        inStorageForm.setPickWorker(1L);
        inStorageForm.setLister(1L);
        inStorageForm.setAccountStatus(AccountStatus.WRZ);
        inStorageFormId = inStorageFormBiz.create(inStorageForm);
    }

    @After
    public void tearDown() throws Exception {
        inStorageFormDao.delete(inStorageFormId);
    }

    @Test
    public void create() {
        InStorageForm inStorageForm = new InStorageForm();
        inStorageForm.setCorporation(1L);
        inStorageForm.setProject(1L);
        inStorageForm.setInStorageStatus(InStorageFormStatus.Working);
        inStorageForm.setInStorageNumber(StringUtils.getRandomStr(8));
        inStorageForm.setInStorageTime(new Date());
        inStorageForm.setWarehouse(3L);
        inStorageForm.setTruck(1L);
        inStorageForm.setPickWorker(1L);
        inStorageForm.setLister(1L);
        Long id = inStorageFormService.create(inStorageForm);
        if(id == null){
            throw new RuntimeException("Create nothing");
        }else {
            logger.info("Created : " + inStorageFormDao.getById(id));
        }
    }

    @Test
    public void update() {
        InStorageForm inStorageForm = inStorageFormDao.getById(inStorageFormId);
        Long new_truck = inStorageForm.getTruck() + 1;
        logger.info("Before Update: "+inStorageForm);
        inStorageForm.setTruck(new_truck);
        inStorageFormService.update(inStorageForm);
        InStorageForm inStorageForm_check = inStorageFormDao.getById(inStorageFormId);
        if(inStorageForm_check.getTruck().longValue() != new_truck.longValue()){
            throw new RuntimeException("Update failed");
        }else{
            logger.info("Updated succeed");
        }
    }

    @Test
    public void deleteById() {
        inStorageFormService.deleteById(inStorageFormId);
        InStorageForm inStorageForm_check = inStorageFormDao.getById(inStorageFormId);
        if(!inStorageForm_check.getIfDeleted()){
            throw new RuntimeException("Delete failed");
        }else {
            logger.info("Delete succeed");
        }
    }

    @Test
    public void delete() {
        InStorageForm inStorageForm_test = inStorageFormDao.getById(inStorageFormId);
        inStorageFormService.delete(inStorageForm_test);
        InStorageForm inStorageForm_check = inStorageFormDao.getById(inStorageFormId);
        if(!inStorageForm_check.getIfDeleted()){
            throw new RuntimeException("Delete failed");
        }else {
            logger.info("Delete succeed");
        }
    }

    @Test
    public void getById() {
        InStorageForm inStorageForm = inStorageFormService.getById(inStorageFormId);
        if(inStorageForm == null || inStorageForm.getIfDeleted()){
            throw new RuntimeException("getById null");
        }else{
            if(inStorageForm.getId().longValue() != inStorageFormId.longValue()){
                throw new RuntimeException("getById failed");
            }else{
                logger.info("getById succeed");
            }
        }
    }

    @Test
    public void getBy() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("IfDeleted", false);
        paramMap.put("Id", inStorageFormId);
        InStorageForm inStorageForm = inStorageFormService.getBy(paramMap);
        if(inStorageForm.getId() != inStorageFormId){
            throw new RuntimeException("getBy failed");
        }else{
            logger.info("getBy succeed");
        }
    }

    @Test
    public void listBy() {
        InStorageForm inStorageForm = inStorageFormDao.getById(inStorageFormId);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("IfDeleted", false);
        paramMap.put("InStorageNumber", inStorageForm.getInStorageNumber());
        List<InStorageForm> inStorageForms = inStorageFormService.listBy(paramMap);
        for (int i = 0; i < inStorageForms.size(); i++)
            logger.info("Item" + i + ":" + inStorageForms.get(i));
    }

    @Test
    public void listPage() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("IfDeleted", false);
        PageParam pageParam = new PageParam(2, 2);
        PageBean<InStorageForm> pageBean = inStorageFormService.listPage(pageParam, paramMap);
        logger.info(pageBean);
        List<InStorageForm> inStorageForms = pageBean.getRecordList();
        for (int i = 0; i < inStorageForms.size(); i++)
            logger.info("Item" + i + ":" + inStorageForms.get(i));
    }

//    @Test
//    public void createWithUser() {
//        InStorageForm inStorageForm = new InStorageForm();
//        inStorageForm.setCorporation(1L);
//        inStorageForm.setDept(InStorageFormDeptType.FinancialInStorageForm);
//        User user = new User();
//        user.setUserName(StringUtils.getRandomStr(8));
//        user.setPassWord("d123456");
//        user.setPhone("188" + StringUtils.getRandomStr(8, StringUtils.StrType.NUMBER));
//        user.setCorporation(1L);
//        inStorageFormService.createWithUser(inStorageForm, user);
//        if(user.getUserType() != UserType.InStorageForm || user.getUserInfo().longValue() != inStorageForm.getId().longValue()){
//            throw new RuntimeException("createWithUser failed");
//        }else {
//            logger.info("createWithUser succeed : InStorageFormID:" + inStorageForm.getId() + ", userID:" + user.getId());
//        }
//    }

    @Test
    public void updateByMap() {
        AccountStatus accountStatus;
        InStorageForm inStorageForm_check = inStorageFormDao.getById(inStorageFormId);
        if(inStorageForm_check.getAccountStatus().getValue().intValue() == AccountStatus.WRZ.getValue().intValue()){
            accountStatus = AccountStatus.YRZ;
        }else{
            accountStatus = AccountStatus.WRZ;
        }
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("accountStatus", accountStatus.getName());
        inStorageFormService.updateByMap(inStorageFormId, paramMap);
        inStorageForm_check = inStorageFormDao.getById(inStorageFormId);
        if(inStorageForm_check.getAccountStatus().getValue().intValue() != accountStatus.getValue().intValue()){
            throw new RuntimeException("updateByMap failed");
        }else{
            logger.info("updateByMap succeed");
        }
    }

//    @Test
//    public void deleteByUserId() {
//        inStorageFormService.deleteByUserId(userId);
//        User user_check = userDao.getById(userId);
//        if(user_check == null || !user_check.getIfDeleted() || user_check.getUserType().getValue().intValue() != UserType.InStorageForm.getValue().intValue()){
//            throw new RuntimeException("deleteByUserId failed");
//        }
//        InStorageForm inStorageForm_check = inStorageFormDao.getById(user_check.getUserInfo());
//        if(inStorageForm_check == null || !inStorageForm_check.getIfDeleted()){
//            throw new RuntimeException("deleteByUserId failed");
//        }
//        logger.info("deleteByUserId succeed");
//    }
}