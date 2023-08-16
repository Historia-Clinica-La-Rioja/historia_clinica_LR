package ar.lamansys.virtualConsultation.infrastructure.output.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo;
import ar.lamansys.virtualConsultation.infrastructure.output.repository.entity.VVirtualConsultationProfessionalAmount;

@Repository
public interface VVirtualConsultationProfessionalAmountRepository extends JpaRepository<VVirtualConsultationProfessionalAmount, Integer> {

	@Transactional(readOnly = true)
	@Query(" SELECT NEW ar.lamansys.virtualConsultation.domain.VirtualConsultationAvailableProfessionalAmountBo(vvcpa.id, vvcpa.availableProfessionalAmount) " +
			"FROM VVirtualConsultationProfessionalAmount vvcpa " +
			"WHERE vvcpa.id IN :virtualConsultationIds")
	List<VirtualConsultationAvailableProfessionalAmountBo> getAvailableProfessionalAmountByVirtualConsultationIds(@Param("virtualConsultationIds") List<Integer> virtualConsultationIds);

}
