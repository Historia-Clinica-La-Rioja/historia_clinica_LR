package net.pladema.medicine.infrastructure.input.rest.mapper;

import net.pladema.imagenetwork.imagequeue.infrastructure.input.rest.dto.ImageQueueListDto;
import net.pladema.medicine.domain.InstitutionMedicineGroupBo;
import net.pladema.medicine.infrastructure.input.rest.dto.MedicineGroupAuditRequiredDto;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface MedicineGroupAuditRequiredMapper {
	@Named("toMedicineGroupAuditRequiredDto")
	MedicineGroupAuditRequiredDto toMedicineGroupAuditRequiredDto(InstitutionMedicineGroupBo institutionMedicineGroupBo);

	@Named("toMedicineGroupAuditRequiredDto")
	@IterableMapping(qualifiedByName = "toMedicineGroupAuditRequiredDto")
	List<MedicineGroupAuditRequiredDto> toMedicineGroupAuditRequiredDto(List<InstitutionMedicineGroupBo> institutionMedicineGroupBo);
}
