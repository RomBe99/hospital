package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.debug.DebugMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component("MapperFactory")
public class MapperFactory {
    private final Map<Class<? extends Mapper>, Function<SqlSession, ? extends Mapper>> mappers = new HashMap<>();

    public MapperFactory() {
        mappers.put(UserMapper.class, s -> s.getMapper(UserMapper.class));
        mappers.put(AdministratorMapper.class, s -> s.getMapper(AdministratorMapper.class));
        mappers.put(CommonMapper.class, s -> s.getMapper(CommonMapper.class));
        mappers.put(DoctorMapper.class, s -> s.getMapper(DoctorMapper.class));
        mappers.put(MedicalCommissionMapper.class, s -> s.getMapper(MedicalCommissionMapper.class));
        mappers.put(PatientMapper.class, s -> s.getMapper(PatientMapper.class));
        mappers.put(ScheduleMapper.class, s -> s.getMapper(ScheduleMapper.class));
        mappers.put(DebugMapper.class, s -> s.getMapper(DebugMapper.class));
    }

    @SuppressWarnings("unchecked")
    public <T extends Mapper> T getMapper(SqlSession session, Class<T> mapperClass) {
        return (T) mappers.get(mapperClass).apply(session);
    }
}