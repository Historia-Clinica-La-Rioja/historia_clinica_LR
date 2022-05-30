package net.pladema.patient.repository;

import net.pladema.patient.repository.domain.PrivateHealthInsuranceVo;
import net.pladema.patient.repository.entity.PrivateHealthInsurance;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PrivateHealthInsuranceRepository extends JpaRepository<PrivateHealthInsurance, Integer> {

    @Transactional(readOnly = true)
    @Query("SELECT new net.pladema.patient.repository.domain.PrivateHealthInsuranceVo(mc.id, mc.name, mc.cuit, mc.type) " +
            "FROM MedicalCoverage as mc " +
            "JOIN PrivateHealthInsurance as phi ON (phi.id = mc.id) " +
            "WHERE mc.deleteable.deleted = false")
    List<PrivateHealthInsuranceVo> getAllWithNames(Sort sort);
}
