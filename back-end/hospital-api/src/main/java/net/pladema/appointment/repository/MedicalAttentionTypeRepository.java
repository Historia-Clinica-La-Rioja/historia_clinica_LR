package net.pladema.appointment.repository;

import net.pladema.appointment.repository.entity.MedicalAttentionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalAttentionTypeRepository extends JpaRepository<MedicalAttentionType, Short> {
}
