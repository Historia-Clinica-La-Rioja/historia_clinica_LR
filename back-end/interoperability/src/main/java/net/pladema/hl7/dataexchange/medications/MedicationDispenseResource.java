package net.pladema.hl7.dataexchange.medications;

import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.dataexchange.exceptions.DispenseValidationEnumException;
import net.pladema.hl7.dataexchange.exceptions.DispenseValidationException;
import net.pladema.hl7.dataexchange.model.domain.CoverageVo;
import net.pladema.hl7.dataexchange.model.domain.DosageVo;
import net.pladema.hl7.dataexchange.model.domain.MedicationDispenseVo;
import net.pladema.hl7.dataexchange.model.domain.MedicationRequestVo;
import net.pladema.hl7.dataexchange.model.domain.PatientVo;
import net.pladema.hl7.dataexchange.model.domain.PractitionerVo;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;

import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;

import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.r4.model.BaseResource;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.MedicationDispense;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.Timing;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
@Conditional(InteroperabilityCondition.class)
public class MedicationDispenseResource extends IResourceFhir {


	protected MedicationDispenseResource(FhirPersistentStore store) {
		super(store);
	}

	@Override
	public ResourceType getResourceType() {
		return null;
	}

	public static MedicationDispenseVo encode(BaseResource baseResource) {
		MedicationDispenseVo data = new MedicationDispenseVo();
		MedicationDispense resource = (MedicationDispense) baseResource;

		data.setIdentifier(resource.getId());
		String statusDisplay = resource.getStatus().getDisplay();
		data.setStatus(statusDisplay);
		data.setStatusId(setMedicationDispenseStatus(statusDisplay));

		if(resource.hasDosageInstruction()) {
			Dosage dosage = resource.getDosageInstruction().get(0);
			DosageVo dosageVo = new DosageVo();
			dosageVo.setSequence(dosage.getSequence() != 0 ? dosage.getSequence() : null);

			if (dosage.hasRoute()) {
				Pair<String,String> route = decodeCoding(dosage.getRoute());
				dosageVo.setRouteCode(route.getKey());
				dosageVo.setRouteTerm(route.getValue());
			}
			if (dosage.hasTiming()) {
				Timing.TimingRepeatComponent repeat = dosage.getTiming().getRepeat();
				dosageVo.setPeriodUnit(repeat.getPeriodUnit().getDisplay());
				dosageVo.setFrequency(repeat.getFrequency());
			}
			if (dosage.hasDoseAndRate()) {
				Dosage.DosageDoseAndRateComponent doseAndRate = dosage.getDoseAndRate().get(0);
				if(doseAndRate.hasDoseQuantity()) {
					Quantity quantity = doseAndRate.getDoseQuantity();
					dosageVo.setDoseQuantityCode(quantity.getCode());
					dosageVo.setDoseQuantityUnit(quantity.getUnit());
					dosageVo.setDoseQuantityValue(quantity.getValue().doubleValue());
				}
			}
			data.setDosage(dosageVo);
		}

		data.setMedicationSnomedCode(resource.getMedicationCodeableConcept().getCodingFirstRep().getCode());
		data.setPatientId(resource.getSubject().getReferenceElement().getIdPart());
		data.setPerformerId(resource.getPerformerFirstRep().getActor().getReferenceElement().getIdPart());
		data.setLocationId(resource.getLocation().getReferenceElement().getIdPart());
		/*String[] ids = resource.getAuthorizingPrescriptionFirstRep().getReferenceElement().getIdPart().split("-");
		data.setPrescriptionLineNumber(ids[2]);
		data.setMedicationRequestId(ids[1]);*/
		data.setLineUuid(UUID.fromString(resource.getAuthorizingPrescriptionFirstRep().getReferenceElement().getIdPart()));

		data.setQuantity(resource.getQuantity());
		data.setWhenHandedOver(resource.getWhenHandedOver());
		data.setNote(resource.getNoteFirstRep().getText());

		return data;
	}

	private static Short setMedicationDispenseStatus(String statusDisplay) {
		switch (statusDisplay.toLowerCase()) {
			case "in progress":
				return 1;
			case "completed":
				return 2;
			case "on hold":
				return 3;
			case "stopped":
				return 4;
			case "declined":
				return 5;
			case "cancelled":
				return 6;
			default:
				return null;
		}
	}
	
	public void validateDispense(MedicationDispenseVo medicationDispense) {
		MedicationRequestVo medicationRequest = store.getMedicationDataForValidation(medicationDispense.getMedicationRequestVo().getRequestUuid(),
				medicationDispense.getLineUuid());

		if (medicationRequest == null)
			throw new DispenseValidationException(DispenseValidationEnumException.MEDICATION_DONT_MATCH, HttpStatus.BAD_REQUEST, "No existe, o no coincide la receta con el renglon de la misma.");

		medicationDispense.setMedicationId(medicationRequest.getMedicationStatementId());

		PatientVo patient = medicationDispense.getPatientVo();
		if (/*!medicationRequest.getPatientId().equals(patient.getId()) ||*/ !medicationRequest.getPatientIdentificationNumber().equals(patient.getIdentificationNumber()))
			throw new DispenseValidationException(DispenseValidationEnumException.PATIENT_DONT_MATCH, HttpStatus.BAD_REQUEST, "No coincide el paciente para la dispensa con el de la receta.");
		if (!medicationRequest.getMedicationCode().equals(medicationDispense.getMedicationSnomedCode()))
			throw new DispenseValidationException(DispenseValidationEnumException.MEDICATION_DONT_MATCH, HttpStatus.BAD_REQUEST, "La medicacion del renglon de la receta no coincide con la de la receta.");
		CoverageVo coverage = medicationDispense.getCoverageVo();
		if (coverage != null && medicationRequest.getMedicalCoverageId() != null) {
			if (!medicationRequest.getMedicalCoverageId().equals(coverage.getMedicalCoverageId()))
				throw new DispenseValidationException(DispenseValidationEnumException.COVERAGE_NOT_MATCH, HttpStatus.BAD_REQUEST, "No coincide la cobertura para dispensa con la de la receta.");
			if ((medicationRequest.getCoverageAffiliationNumber() != null) && (!medicationRequest.getCoverageAffiliationNumber().equals(coverage.getAffiliateNumber())))
				throw new DispenseValidationException(DispenseValidationEnumException.COVERAGE_AFFILIATE_NUMBER_DONT_MATCH, HttpStatus.BAD_REQUEST, "No coincide el numero de afiliado de la cobertura de la receta con la de la dispensa.");
		}
		/*PractitionerVo practitioner = medicationDispense.getPractitionerVo();
		if (practitioner == null || medicationRequest.getDoctorId() == null)
			throw new DispenseValidationException(DispenseValidationEnumException.PRACTITIONER_CANT_BE_NULL, HttpStatus.BAD_REQUEST, "El prescriptor tiene que estar tanto en la receta como en la dispensa.");
		if (!practitioner.getId().equals(medicationRequest.getDoctorId().toString()))
			throw new DispenseValidationException(DispenseValidationEnumException.PRACTITIONER_NOT_MATCH, HttpStatus.BAD_REQUEST, "No coincide el id del doctor de la receta con el de la dispensa.");*/
		DosageVo dosage = medicationDispense.getDosage();
		DosageVo requestDosage = medicationRequest.getDosage();
		validateDosageInstruction(dosage,requestDosage);
		validateQuantity(medicationDispense.getQuantity());
		validateDate(medicationDispense.getWhenHandedOver());
		validateStateTransition(medicationRequest.getPrescriptionLineState(),medicationDispense);
	}

	private void validateQuantity(Quantity quantity) {
		BigDecimal value = quantity.getValue();
		if (value.compareTo(BigDecimal.ZERO) < 0)
			throw new DispenseValidationException(DispenseValidationEnumException.NEGATIVE_QUANTITY, HttpStatus.BAD_REQUEST, "La cantidad de medicamento dispensada no puede ser negativa.");
	}

	private void validateDate(Date date) {
		if (date.after(Date.from(Instant.now()))) // o esta bien que sea en el futuro?
			throw new DispenseValidationException(DispenseValidationEnumException.DATE_AFTER_TODAY, HttpStatus.BAD_REQUEST, "La fecha de entrega ingresada es en el futuro.");
	}

	private void validateDosageInstruction(DosageVo dosage, DosageVo requestDosage) {
		if (dosage != null && requestDosage != null) {
			if (dosage.hasSequence() && requestDosage.hasSequence()) {
				if (!dosage.getSequence().equals(requestDosage.getSequence()))
					throw new DispenseValidationException(DispenseValidationEnumException.DOSAGE_SEQUENCE_NOT_MATCH, HttpStatus.BAD_REQUEST, "La secuencia de la dosis de la receta y dispensa no coinciden.");
			} else if (!(!dosage.hasSequence() && !requestDosage.hasSequence()))
				throw new DispenseValidationException(DispenseValidationEnumException.DOSAGE_SEQUENCE_NOT_MATCH, HttpStatus.BAD_REQUEST, "La secuencia de la dosis no esta presente tanto en la receta como en la dispensa.");

			if (dosage.getFrequency() != null && requestDosage.getFrequency() != null) {
				if (!dosage.getFrequency().equals(requestDosage.getFrequency()))
					throw new DispenseValidationException(DispenseValidationEnumException.DOSAGE_FREQUENCY_NOT_MATCH, HttpStatus.BAD_REQUEST, "La frecuencia de la dosis de la receta y dispensa no coinciden.");
			} else if (!(dosage.getFrequency() == null && requestDosage.getFrequency() == null))
				throw new DispenseValidationException(DispenseValidationEnumException.DOSAGE_FREQUENCY_NOT_MATCH, HttpStatus.BAD_REQUEST, "La frecuencia de la dosis no esta presente tanto en la receta como en la dispensa.");

			if (dosage.getPeriodUnit() != null && requestDosage.getPeriodUnit() != null) {
				if (!dosage.getPeriodUnit().equals(Timing.UnitsOfTime.fromCode(requestDosage.getPeriodUnit()).getDisplay()))
					throw new DispenseValidationException(DispenseValidationEnumException.DOSAGE_PERIOD_UNIT_NOT_MATCH, HttpStatus.BAD_REQUEST, "La unidad del periodo de la dosis de la receta y dispensa no coinciden.");
			} else if (!(dosage.getPeriodUnit() == null && requestDosage.getPeriodUnit() == null))
				throw new DispenseValidationException(DispenseValidationEnumException.DOSAGE_PERIOD_UNIT_NOT_MATCH, HttpStatus.BAD_REQUEST, "La unidad del periodo de la dosis no esta presente tanto en la receta como en la dispensa.");
		} else if (!(dosage == null && requestDosage == null))
			throw new DispenseValidationException(DispenseValidationEnumException.DOSAGE_NOT_MATCH, HttpStatus.BAD_REQUEST, "El dosage de la dispensa no coincide con el de la receta.");
	}

	private void validateStateTransition(Short actualState, MedicationDispenseVo medicationDispense) {
		Short newState = medicationDispense.getStatusId();
		switch (actualState) {
			case 1:
				if (newState != 2 && newState != 3)
					throw new DispenseValidationException(DispenseValidationEnumException.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "Desde el estado de dispensa activa solamente se puede transitar al algun estado de dispensado.");
				break;
			case 2:
				throw new DispenseValidationException(DispenseValidationEnumException.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "Desde el estado de dispensado cerrado no se puede cambiar.");
			case 3:
				if (newState == 5) {
					medicationDispense.setStatusId((short)1);
					if (!medicationDispense.hasNote())
						throw new DispenseValidationException(DispenseValidationEnumException.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "Para cambiar de dispensa provisoria a dispensa activa, es necesaria una nota o observacion.");
				}
				if (newState != 5 && newState != 2)
					throw new DispenseValidationException(DispenseValidationEnumException.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "Desde el estado de dispensa provisoria solamente se puede transitar a dispensa completada o activa.");
				break;
			case 4:
				throw new DispenseValidationException(DispenseValidationEnumException.REQUEST_EXPIRED, HttpStatus.BAD_REQUEST, "La receta excedio su tiempo limite y esta vencida.");
			case 6:
				throw new DispenseValidationException(DispenseValidationEnumException.REQUEST_CANCELED, HttpStatus.BAD_REQUEST, "La receta ha sido cancelada por el sistema.");
			default:
				throw new DispenseValidationException(DispenseValidationEnumException.TRANSITION_STATE_INVALID, HttpStatus.BAD_REQUEST, "La receta no tiene estado o es un estado inalcanzable.");
		}
	}

	public void uppdateRequestDispensed(Integer id, Short statusToUpdate) {
		store.setMedicationRequestDispensed(Arrays.asList(id),statusToUpdate.intValue());
	}

}
