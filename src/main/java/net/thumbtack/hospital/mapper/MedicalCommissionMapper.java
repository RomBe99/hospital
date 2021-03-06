package net.thumbtack.hospital.mapper;

import net.thumbtack.hospital.model.ticket.TicketToMedicalCommission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MedicalCommissionMapper extends Mapper {
    @Insert("INSERT INTO medical_commission VALUES (#{id}, #{title}, #{date}, #{time}, #{patientId}, #{duration}, (SELECT id FROM cabinet WHERE name = #{room}));")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void createMedicalCommission(TicketToMedicalCommission ticketToMedicalCommission);

    @Insert({"<script>",
            "INSERT INTO commission_doctor VALUES",
            "<foreach item='doctorId' collection='doctorIds' separator=','>",
            "(#{commissionTicketId}, #{doctorId})",
            "</foreach>;",
            "</script>"})
    @Options(useGeneratedKeys = true)
    void insertDoctorsInMedicalCommission(@Param("commissionTicketId") int commissionTicketId,
                                          @Param("doctorIds") List<Integer> doctorIds);

    @Delete("DELETE FROM medical_commission WHERE title = #{ticketTitle};")
    void denyMedicalCommission(String ticketTitle);
}