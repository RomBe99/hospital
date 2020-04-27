package net.thumbtack.hospital;

import net.thumbtack.hospital.dao.*;
import net.thumbtack.hospital.daoimpl.*;
import net.thumbtack.hospital.model.Administrator;
import net.thumbtack.hospital.model.Doctor;
import net.thumbtack.hospital.model.Patient;
import net.thumbtack.hospital.utils.mybatis.MyBatisUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

public class BaseTest {
    protected AdminDao adminDao = new AdminDaoImpl();
    protected CommonDao commonDao = new CommonDaoImpl();
    protected DoctorDao doctorDao = new DoctorDaoImpl();
    protected PatientDao patientDao = new PatientDaoImpl();

    private static boolean setUpIsDone = false;

    @BeforeClass
    public static void setUp() {
        if (!setUpIsDone) {
            boolean initSqlSessionFactory = MyBatisUtils.initSqlSessionFactory();

            if (!initSqlSessionFactory) {
                throw new RuntimeException("Can't create connection, stop");
            }

            setUpIsDone = true;
        }
    }

    @Before
    public void clearDatabase() {
        commonDao.clear();
    }

    protected Administrator insertAdministrator(String login, String password,
                                                String firstName, String lastName, String patronymic,
                                                String position) {
        Administrator administrator = new Administrator(login, password, firstName, lastName, patronymic, position);
        adminDao.insertAdministrator(administrator);

        Assert.assertNotEquals(0, administrator.getId());

        return administrator;
    }

    protected Doctor insertDoctor(String login, String password,
                                  String firstName, String lastName, String patronymic,
                                  String cabinetName, String doctorSpecialtyName) {
        Doctor doctor = new Doctor(login, password, firstName, lastName, patronymic, cabinetName, doctorSpecialtyName);
        doctorDao.insertDoctor(doctor);

        Assert.assertNotEquals(0, doctor.getId());

        return doctor;
    }

    protected Patient insertPatient(String login, String password,
                                    String firstName, String lastName, String patronymic,
                                    String email, String address, String phone) {
        Patient patient = new Patient(login, password, firstName, lastName, patronymic, email, address, phone);
        patientDao.insertPatient(patient);

        Assert.assertNotEquals(0, patient.getId());

        return patient;
    }

    protected int getDoctorSpecialityIdByName(String name) {
        int id = commonDao.getDoctorSpecialityIdByName(name);

        Assert.assertNotEquals(0, id);
        return id;
    }

    protected int getCabinetIdByName(String name) {
        int id = commonDao.getCabinetIdByName(name);

        Assert.assertNotEquals(0, id);
        return id;
    }
}