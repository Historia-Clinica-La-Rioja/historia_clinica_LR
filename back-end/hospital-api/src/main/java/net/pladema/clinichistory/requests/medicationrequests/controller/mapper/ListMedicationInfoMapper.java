package net.pladema.clinichistory.requests.medicationrequests.controller.mapper;

import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.HealthConditionInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.MedicationInfoDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public class ListMedicationInfoMapper {

    private static final Logger LOG = LoggerFactory.getLogger(ListMedicationInfoMapper.class);

    @Named("parseTo")
    public MedicationInfoDto parseTo(MedicationBo medicationBo, ProfessionalDto professionalDto) {
        LOG.trace("parseTo -> medicationBo {} ", medicationBo);
        MedicationInfoDto result = new MedicationInfoDto();
        result.setId(medicationBo.getId());
        result.setSnomed(SnomedDto.from(medicationBo.getSnomed()));
        result.setHealthCondition(HealthConditionInfoDto.from(medicationBo.getHealthCondition()));
        result.setStatusId(medicationBo.getStatusId());
        result.setMedicationRequestId(medicationBo.getEncounterId());
        result.setHasRecipe(medicationBo.isHasRecipe());
        result.setObservations(medicationBo.getNote());
        result.setDoctor(DoctorInfoDto.from(professionalDto));
        LOG.trace("parseTo result -> {} ", result);
        return result;
    }

}
