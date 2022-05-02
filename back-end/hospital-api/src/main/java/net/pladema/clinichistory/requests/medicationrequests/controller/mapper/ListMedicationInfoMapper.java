package net.pladema.clinichistory.requests.medicationrequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DoctorInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DosageInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.HealthConditionInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.MedicationInfoDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import net.pladema.staff.controller.dto.ProfessionalDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        result.setDosage(DosageInfoDto.from(medicationBo.getDosage()));
        result.setCreatedOn(new DateDto(medicationBo.getCreatedOn().getYear(),
                medicationBo.getCreatedOn().getMonthValue(),
                medicationBo.getCreatedOn().getDayOfMonth()));
        result.setTotalDays(calculateTotalDays(medicationBo.getDosage().getStartDate(), medicationBo.getCreatedOn()));
        LOG.trace("parseTo result -> {} ", result);
        return result;
    }

    private int calculateTotalDays(LocalDateTime dosageStartDate, LocalDate medicationStartDate){
        if (dosageStartDate != null)
            return (int) ChronoUnit.DAYS.between(dosageStartDate.toLocalDate(), LocalDate.now());
        return (int) ChronoUnit.DAYS.between(medicationStartDate, LocalDate.now());
    }

}
