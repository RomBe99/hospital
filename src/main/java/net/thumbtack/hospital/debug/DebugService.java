package net.thumbtack.hospital.debug;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("DebugService")
public class DebugService {
    private final DebugDao debugDao;

    @Autowired
    public DebugService(DebugDao debugDao) {
        this.debugDao = debugDao;
    }

    public void clear() {
        debugDao.clearUsers();
        debugDao.clearLoggedInUsers();
        debugDao.clearAdministrators();
        debugDao.clearDoctors();
        debugDao.clearPatients();
        debugDao.clearMedicalCommissions();
        debugDao.clearCommissionDoctors();
        debugDao.clearScheduleCells();
        debugDao.clearTimeCells();
    }
}