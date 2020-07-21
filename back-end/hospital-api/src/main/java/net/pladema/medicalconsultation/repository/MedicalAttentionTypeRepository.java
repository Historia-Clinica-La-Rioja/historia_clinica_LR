package net.pladema.medicalconsultation.repository;

import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalAttentionTypeRepository extends JpaRepository<MedicalAttentionType, Short> {
}
