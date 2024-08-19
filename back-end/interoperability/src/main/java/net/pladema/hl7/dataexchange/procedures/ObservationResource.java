package net.pladema.hl7.dataexchange.procedures;

import net.pladema.hl7.dataexchange.IResourceFhir;
import net.pladema.hl7.dataexchange.model.domain.ObservationVo;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;

import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(InteroperabilityCondition.class)
public class ObservationResource extends IResourceFhir {


	protected ObservationResource(FhirPersistentStore store) {
		super(store);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.Observation;
	}

	public static ObservationVo encode(Resource baseResource) {
		Observation resource = (Observation) baseResource;
		ObservationVo data = new ObservationVo();

		data.setId(resource.getId());
		data.setStatus(resource.getStatus().getDisplay());
		data.setCategoryCode(resource.getCategoryFirstRep().getText());
		data.setCode(resource.getCode().getCodingFirstRep().getCode());
		data.setPatientId(resource.getSubject().getReferenceElement().getIdPart());
		data.setDoctorId(resource.getPerformerFirstRep().getReferenceElement().getIdPart());

		if (resource.hasValueIntegerType())
			data.setValueInteger(resource.getValueIntegerType().getValue());
		if (resource.hasValueStringType())
			data.setValueString(resource.getValueStringType().getValueAsString());
		if (resource.hasValueBooleanType())
			data.setValueBoolean(resource.getValueBooleanType().getValue());
		if (resource.hasValueQuantity()) {
			data.setQuantityValue(resource.getValueQuantity().getValue().floatValue());
			data.setQuantityUnit(resource.getValueQuantity().getUnit());
		}

		data.setBodyPartCode(resource.getBodySite().getCodingFirstRep().getCode());
		data.setNote(resource.getNoteFirstRep().getText());

		return data;
	}
}
