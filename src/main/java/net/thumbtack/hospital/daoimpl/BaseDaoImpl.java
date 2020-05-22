package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class BaseDaoImpl {
    public SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    public CommonMapper getCommonMapper(SqlSession session) {
        return session.getMapper(CommonMapper.class);
    }
}