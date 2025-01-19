package net.pladema.clinichistory.requests.medicationrequests.controller.mapper;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.CommercialMedicationPrescriptionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DosageBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.QuantityBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EUnitsOfTimeBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.clinichistory.requests.controller.dto.CommercialMedicationPrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NewDosageDto;
import ar.lamansys.sgh.clinichistory.domain.document.impl.DigitalRecipeMedicationRequestBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Mapper
public class CreateMedicationRequestMapper {

    private static final Logger LOG = LoggerFactory.getLogger(CreateMedicationRequestMapper.class);
    private static final String OUTPUT = "OUTPUT -> {}";

	@Autowired
	private FeatureFlagsService featureFlagsService;

    @Named("parseTo")
    public MedicationRequestBo parseTo(Integer institutionId, Integer doctorId, Integer patientId, PrescriptionDto medicationRequest) {
        LOG.debug("parseTo -> doctorId {}, patientId {}, medicationRequest {} ", doctorId, patientId, medicationRequest);
		MedicationRequestBo result;
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_RECETA_DIGITAL))
			result = new MedicationRequestBo();
		else
			result = new DigitalRecipeMedicationRequestBo();
        result.setDoctorId(doctorId);
		result.setPatientInfo(new PatientInfoBo(patientId));
        result.setHasRecipe(medicationRequest.isHasRecipe());
        result.setMedicalCoverageId(medicationRequest.getMedicalCoverageId());
        result.setMedications(medicationRequest.getItems().stream().map(this::parseTo).collect(Collectors.toList()));
		result.setRepetitions(medicationRequest.getRepetitions());
		result.setIsPostDated(medicationRequest.getIsPostDated());
		result.setClinicalSpecialtyId(medicationRequest.getClinicalSpecialtyId());
		result.setIsArchived(medicationRequest.getIsArchived());
		result.setInstitutionId(institutionId);
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
		result.setPrescriptionLineNumber(pid.getPrescriptionLineNumber());
		result.setIsDigital(featureFlagsService.isOn(AppFeature.HABILITAR_RECETA_DIGITAL));
		result.setCommercialMedicationPrescription(parseTopidCommercialMedicationPrescriptionBo(pid.getCommercialMedicationPrescription()));
		result.setSuggestedCommercialMedication(parseTo(pid.getSuggestedCommercialMedication()));
        LOG.debug(OUTPUT, result);
        return result;
    }

	private CommercialMedicationPrescriptionBo parseTopidCommercialMedicationPrescriptionBo(CommercialMedicationPrescriptionDto commercialMedicationPrescription) {
		if (commercialMedicationPrescription != null)
			return CommercialMedicationPrescriptionBo.builder()
					.presentationUnitQuantity(commercialMedicationPrescription.getPresentationUnitQuantity())
					.medicationPackQuantity(commercialMedicationPrescription.getMedicationPackQuantity())
					.build();
		return null;
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
		result.setDosesByDay(dosage.getDosesByDay());
		result.setDosesByUnit(dosage.getDosesByUnit());

		assert dosage.getQuantity() != null;
		QuantityBo quantity = new QuantityBo(dosage.getQuantity().getValue(), dosage.getQuantity().getUnit() == null ? "" : dosage.getQuantity().getUnit());
		result.setQuantity(quantity);
        LOG.debug(OUTPUT, result);
        return result;
    }
}
