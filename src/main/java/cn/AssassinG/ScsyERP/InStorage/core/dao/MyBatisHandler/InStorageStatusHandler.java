package cn.AssassinG.ScsyERP.InStorage.core.dao.MyBatisHandler;

import cn.AssassinG.ScsyERP.InStorage.facade.enums.InStorageFormStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InStorageStatusHandler extends BaseTypeHandler<InStorageFormStatus> {

//    private Class<AdminDeptType> type;
//    private AdminDeptType[] enums;
//
//    public AdminDeptTypeHandler(Class<AdminDeptType> type) {
//        if(type == null)
//            throw new IllegalArgumentException("type参数不能为空");
//        this.type = type;
//        this.enums = type.getEnumConstants();
//        if(this.enums == null){
//            throw new IllegalArgumentException(type.getSimpleName() + "不是一个枚举类型");
//        }
//    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, InStorageFormStatus inStorageFormStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, inStorageFormStatus.getName());
    }

    @Override
    public InStorageFormStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String str = resultSet.getString(s);
        if(resultSet.wasNull())
            return null;
        else{
            return InStorageFormStatus.getEnum(str);
        }
    }

    @Override
    public InStorageFormStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String str = resultSet.getString(i);
        if(resultSet.wasNull())
            return null;
        else{
            return InStorageFormStatus.getEnum(str);
        }
    }

    @Override
    public InStorageFormStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String str = callableStatement.getString(i);
        if(callableStatement.wasNull())
            return null;
        else{
            return InStorageFormStatus.getEnum(str);
        }
    }
}
