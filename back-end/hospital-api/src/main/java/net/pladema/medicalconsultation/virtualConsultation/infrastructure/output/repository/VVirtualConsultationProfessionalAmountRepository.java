package net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository;

import java.util.List;

import net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo;
import net.pladema.medicalconsultation.virtualConsultation.infrastructure.output.repository.entity.VVirtualConsultationProfessionalAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VVirtualConsultationProfessionalAmountRepository extends JpaRepository<VVirtualConsultationProfessionalAmount, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT NEW net.pladema.medicalconsultation.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo(vvcpa.id, vvcpa.availableProfessionalAmount) " +
			"FROM VVirtualConsultationProfessionalAmount vvcpa " +
			"WHERE vvcpa.id IN :virtualConsultationIds")
	List<VirtualConsultationAvailableProfessionalAmountBo> getAvailableProfessionalAmountByVirtualConsultationIds(@Param("virtualConsultationIds") List<Integer> virtualConsultationIds);

}
