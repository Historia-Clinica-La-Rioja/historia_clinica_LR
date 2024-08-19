package net.pladema.hl7.concept.administration;

import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import net.pladema.hl7.dataexchange.model.adaptor.FhirNarrative;
import net.pladema.hl7.dataexchange.model.domain.CoverageVo;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;

import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;
import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;

import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Conditional(InteroperabilityCondition.class)
public class CoverageResource extends ISingleResourceFhir {

	public CoverageResource(FhirPersistentStore store) {
		super(store);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.Coverage;
	}

	@Override
	public Coverage fetch(String id, Map<ResourceType, Reference> references) {
		CoverageVo coverage = store.getCoverage(id);

		Coverage resource = new Coverage();

		resource.setMeta(new Meta().setProfile(List.of(new CanonicalType(CodingProfile.Coverage.BASEURL))));

		resource.setId(coverage.getMedicalCoverageId().toString()); // ID INTERNO O LO DEL IDENTIFIER
		String affiliateNumber = "206850-01";
		if (coverage.getAffiliateNumber() != null)
			affiliateNumber = coverage.getAffiliateNumber();
		resource.addIdentifier(newIdentifier("https://www.example-cobertura.com.ar/",affiliateNumber));
		//resource.setSubscriberId(coverage.getAffiliateNumber());

		if (coverage.getIsActive())
			resource.setStatus(Coverage.CoverageStatus.ACTIVE);
		else
			resource.setStatus(Coverage.CoverageStatus.CANCELLED);

		/*Period period = new Period();
		if (coverage.getStartDate() != null && coverage.getEndDate() != null) {
			period.setStart(coverage.getStartDate());
			period.setEnd(coverage.getEndDate());
		}
		resource.setPeriod(period);*/

		if (references.containsKey(ResourceType.Patient))
			resource.setBeneficiary(references.get(ResourceType.Patient));

		Reference payor = new Reference();
		Identifier payorIdentifier = newIdentifier(CodingSystem.Coverage.PAYOR_IDENTIFIER_SYSTEM,"13566"); // 111 code example para la cobertura
		payor.setIdentifier(payorIdentifier);
		payor.setDisplay(coverage.getName());
		resource.setPayor(List.of(payor));

		CodeableConcept type = new CodeableConcept();
		Coding coding = new Coding();
		coding.setSystem(CodingSystem.Coverage.CLASS_CODE);
		coding.setCode("plan");
		coding.setDisplay("Plan");
		type.setCoding(List.of(coding));
		Coverage.ClassComponent classComponent = new Coverage.ClassComponent();
		classComponent.setType(type);
		if (coverage.havePlan()) {
			classComponent.setName(coverage.getPlanName());
			classComponent.setValue(coverage.getPlanId().toString());
		} else {
			classComponent.setName("Example plan");
			classComponent.setValue("1234_5");
		}
		resource.setText(FhirNarrative.buildNarrativeAdditional(coverage.getName() + " - " + classComponent.getName() + " - " + affiliateNumber));
		resource.setClass_(List.of(classComponent));

		return resource;
	}

	public static CoverageVo encode (Resource baseResource) {
		CoverageVo data = new CoverageVo();
		Coverage resource = (Coverage) baseResource;

		data.setMedicalCoverageId(Integer.parseInt(resource.getIdElement().getIdPart()));
		data.setAffiliateNumber(resource.getIdentifierFirstRep().getValue());
		//data.setAffiliateNumber(resource.getSubscriberId());

		switch (resource.getStatus()) {
			case ACTIVE:
				data.setIsActive(true);
			case CANCELLED:
				data.setIsActive(false);
		}

		if (resource.hasPeriod()) {
			Period period = resource.getPeriod();
			if (period.hasStart())
				data.setStartDate(period.getStart());
			if (period.hasEnd())
				data.setEndDate(period.getEnd());
		}

		data.setPatientId(Integer.parseInt(resource.getBeneficiary().getReferenceElement().getIdPart()));

		return data;
	}
}
