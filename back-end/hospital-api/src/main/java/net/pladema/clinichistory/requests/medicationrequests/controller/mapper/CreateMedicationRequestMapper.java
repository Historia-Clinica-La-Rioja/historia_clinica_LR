package net.pladema.clinichistory.requests.medicationrequests.controller.mapper;

import net.pladema.clinichistory.documents.service.ips.domain.DosageBo;
import net.pladema.clinichistory.documents.service.ips.domain.HealthConditionBo;
import net.pladema.clinichistory.documents.service.ips.domain.MedicationBo;
import net.pladema.clinichistory.documents.service.ips.domain.SnomedBo;
import net.pladema.clinichistory.documents.service.ips.domain.enums.EUnitsOfTimeBo;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.NewDosageDto;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Mapper
public class CreateMedicationRequestMapper {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMedicationRequestMapper.class);
    private static final String OUTPUT = "OUTPUT -> {}";

    @Named("parseTo")
    public MedicationRequestBo parseTo(Integer doctorId, Integer patientId, PrescriptionDto medicationRequest) {
        LOG.debug("parseTo -> doctorId {}, patientId {}, medicationRequest {} ", doctorId, patientId, medicationRequest);
        MedicationRequestBo result = new MedicationRequestBo();
        result.setDoctorId(doctorId);
        result.setPatientId(patientId);
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
        result.setStartDate(LocalDate.now());
        result.setFrequency(dosage.getFrequency());
        result.setDuration(dosage.getDuration());
        result.setPeriodUnit(dosage.isDiary() ? EUnitsOfTimeBo.DAY : EUnitsOfTimeBo.HOUR);
        result.setChronic(dosage.isChronic());
        LOG.debug(OUTPUT, result);
        return result;
    }
}
