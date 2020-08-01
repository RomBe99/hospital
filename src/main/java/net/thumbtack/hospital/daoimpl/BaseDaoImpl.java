package net.thumbtack.hospital.daoimpl;

import net.thumbtack.hospital.mapper.CommonMapper;
import net.thumbtack.hospital.util.mybatis.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component("BaseDaoImpl")
public class BaseDaoImpl {
    protected SqlSession getSession() {
        return MyBatisUtils.getSession();
    }

    protected CommonMapper getCommonMapper(SqlSession session) {
        return session.getMapper(CommonMapper.class);
    }
}