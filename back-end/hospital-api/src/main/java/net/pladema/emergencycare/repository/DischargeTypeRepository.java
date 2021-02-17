package net.pladema.emergencycare.repository;


import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DischargeTypeRepository extends JpaRepository<DischargeType, Short> {

    @Transactional(readOnly = true)
    @Query("SELECT dt " +
            "FROM DischargeType dt " +
            "WHERE dt.internment = TRUE ")
    List<DischargeType> getAllInternmentTypes();

    @Transactional(readOnly = true)
    @Query("SELECT dt " +
            "FROM DischargeType dt " +
            "WHERE dt.emergencyCare = TRUE ")
    List<DischargeType> getAllEmergencyCareTypes();

}
