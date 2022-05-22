package net.pladema.clinichistory.requests.medicationrequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Mapper
public class CreateMedicationRequestMapper {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMedicationRequestMapper.class);
    private static final String OUTPUT = "OUTPUT -> {}";

    @Named("parseTo")
    public MedicationRequestBo parseTo(Integer doctorId, BasicPatientDto patientDto, PrescriptionDto medicationRequest) {
        LOG.debug("parseTo -> doctorId {}, patientDto {}, medicationRequest {} ", doctorId, patientDto, medicationRequest);
        MedicationRequestBo result = new MedicationRequestBo();
        result.setDoctorId(doctorId);
        result.setPatientInfo(new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()));
        result.setHasRecipe(medicationRequest.isHasRecipe());
        result.setMedicalCoverageId(medicationRequest.getMedicalCoverageId());
        result.setMedications(medicationRequest.getItems().stream().map(this::parseTo).collect(Collectors.toList()));
        LOG.debug(OUTPUT, result);
        return result;
    }

    private MedicationBo parseTo(PrescriptionItemDto pid) {
        LOG.debug("parseTo -> pid {} ", pid);
        MedicationBo result = new MedicationBo();
        result.setSnomed(parseTo(pid.getSnomed()));
        result.setNote(pid.getObservations());
        var healthCondition = new HealthConditionBo();
        healthCondition.setId(pid.getHealthConditionId());
        result.setHealthCondition(healthCondition);
        result.setDosage(parseTo(pid.getDosage()));
        LOG.debug(OUTPUT, result);
        return result;
    }

    private SnomedBo parseTo(SnomedDto snomed) {
        LOG.debug("parseTo -> snomed {} ", snomed);
        if (snomed == null)
            return null;
        SnomedBo result = new SnomedBo();
        result.setSctid(snomed.getSctid());
        result.setPt(snomed.getPt());
        result.setParentFsn(snomed.getParentFsn());
        result.setParentId(snomed.getParentId());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private DosageBo parseTo(NewDosageDto dosage) {
        LOG.debug("parseTo -> dosage {} ", dosage);
        if (dosage == null)
            return null;
        DosageBo result = new DosageBo();
        result.setStartDate(LocalDateTime.now());
        result.setFrequency(dosage.getFrequency());
        result.setDuration(dosage.getDuration());
        result.setPeriodUnit(dosage.isDiary() ? EUnitsOfTimeBo.DAY : EUnitsOfTimeBo.HOUR);
        result.setChronic(dosage.isChronic());
        LOG.debug(OUTPUT, result);
        return result;
    }
}
