package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.mappers.AdminMapper;
import net.thumbtack.hospital.mappers.PatientMapper;
import net.thumbtack.hospital.mappers.UserMapper;
import net.thumbtack.hospital.utils.mybatis.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class BaseDaoImpl {
    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected AdminMapper getAdminMapper(SqlSession session) {
        return session.getMapper(AdminMapper.class);
    }

    protected PatientMapper getPatientMapper(SqlSession session) {
        return session.getMapper(PatientMapper.class);
    }

    protected UserMapper getUserMapper(SqlSession session) {
        return session.getMapper(UserMapper.class);
    }
}