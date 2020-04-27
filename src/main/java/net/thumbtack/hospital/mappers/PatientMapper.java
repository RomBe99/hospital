package net.thumbtack.hospital.mappers;

import net.thumbtack.hospital.model.Patient;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

public interface PatientMapper {
    @Insert("INSERT INTO patient VALUES (#{id}, #{email}, #{address}, #{phone});")
    void insertPatient(Patient patient);

    @Update("UPDATE patient SET email = #{email}, address = #{address}, phone = #{phone} WHERE userId = #{id};")
    void updatePatient(Patient patient);

    @Delete("DELETE FROM patient WHERE userId = #{id};")
    void removePatient(int id);

    @Delete("DELETE FROM patient;")
    void deleteAll();
}