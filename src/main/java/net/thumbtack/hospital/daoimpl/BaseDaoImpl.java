package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.mapper.*;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
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

    protected CommonMapper getCommonMapper(SqlSession session) {
        return session.getMapper(CommonMapper.class);
    }

    protected DoctorMapper getDoctorMapper(SqlSession session) {
        return session.getMapper(DoctorMapper.class);
    }
}