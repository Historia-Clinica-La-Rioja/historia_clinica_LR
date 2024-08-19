package net.pladema.hl7.concept.administration;

import net.pladema.hl7.dataexchange.ISingleResourceFhir;
import net.pladema.hl7.dataexchange.model.adaptor.FhirCode;
import net.pladema.hl7.dataexchange.model.adaptor.FhirDateMapper;
import net.pladema.hl7.dataexchange.model.adaptor.FhirNarrative;
import net.pladema.hl7.dataexchange.model.adaptor.FhirString;
import net.pladema.hl7.dataexchange.model.domain.PractitionerVo;
import net.pladema.hl7.supporting.conformance.InteroperabilityCondition;

import net.pladema.hl7.supporting.exchange.database.FhirPersistentStore;

import net.pladema.hl7.supporting.terminology.coding.CodingProfile;
import net.pladema.hl7.supporting.terminology.coding.CodingSystem;

import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.hl7.fhir.r4.model.StringType;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Conditional(InteroperabilityCondition.class)
public class PractitionerResource extends ISingleResourceFhir {

	public PractitionerResource(FhirPersistentStore store) {
		super(store);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.Practitioner;
	}

	@Override
	public Practitioner fetch(String id, Map<ResourceType, Reference> references) {
		PractitionerVo practitioner = store.getPractitioner(id);

		Practitioner resource = new Practitioner();

		resource.setMeta(new Meta().setProfile(List.of(new CanonicalType(CodingProfile.Practitioner.BASEURL))));
		resource.setText(FhirNarrative.buildNarrativeAdditional(practitioner.getFullName()));
		
		resource.setId(id);

		Identifier practitionerRenaperIdentifier = newIdentifier(CodingSystem.Patient.IDENTIFIER, practitioner.getIdentificationNumber());
		practitionerRenaperIdentifier.setUse(Identifier.IdentifierUse.OFFICIAL);
		/*CodeableConcept type = newCodeableConcept(CodingSystem.Practitioner.IDENTIFIER_TYPE_SYSTEM,new FhirCode("NI","National unique individual identifier"));
		practitionerRenaperIdentifier.setType(type);*/
		practitionerRenaperIdentifier.setAssigner(new Reference().setDisplay("RENAPER"));
		resource.addIdentifier(practitionerRenaperIdentifier);

		Identifier practitionerREFEPSIdentifier = newIdentifier(CodingSystem.Practitioner.IDENTIFIER_REFEPS, "111111111");
		practitionerREFEPSIdentifier.setUse(Identifier.IdentifierUse.USUAL);
		/*CodeableConcept typeREFEPS = newCodeableConcept(CodingSystem.Practitioner.IDENTIFIER_TYPE_SYSTEM,new FhirCode("AC","Accreditation/Certification Identifier"));
		practitionerREFEPSIdentifier.setType(typeREFEPS);*/
		practitionerREFEPSIdentifier.setPeriod(new Period().setStart(new Date()));
		resource.addIdentifier(practitionerREFEPSIdentifier); // falta agregar el refeps
		// falta identifier de REFEPS

		resource.addTelecom(newTelecom(practitioner.getPhonePrefix() + practitioner.getPhoneNumber()));

		/*resource.setGender(Enumerations.AdministrativeGender.fromCode(practitioner.getGender()));

		resource.setActive(true);*/

		List<HumanName> names = new ArrayList<>();
		HumanName name = new HumanName();
		/*name.setUse(HumanName.NameUse.OFFICIAL)
				.addGiven(practitioner.getFirstName())
				.setFamily(practitioner.getLastName())
				.setText(practitioner.getFullName());*/
		name.setText(practitioner.getFullName());
		names.add(name);
		resource.setName(names);

		//addNonRequiredAttributes(resource,practitioner);

		return resource;
	}

	private void addNonRequiredAttributes(Practitioner resource, PractitionerVo practitioner) {
		if(practitioner.getFullAddress() != null && practitioner.getFullAddress().hasAddressData())
			resource.addAddress(newAddress(practitioner.getFullAddress()));

		if(practitioner.getBirthDate() != null) {
			DateType birthdate = new DateType();
			birthdate.fromStringValue(practitioner.getStringBirthDate());
			resource.setBirthDateElement(birthdate);
		}
	}

	public static PractitionerVo encode(Resource baseResource){
		PractitionerVo data = new PractitionerVo();
		Practitioner resource = (Practitioner) baseResource;

		data.setId(resource.getIdElement().getIdPart());
		Optional<Identifier> identifier = resource.getIdentifier().stream().filter(i -> i.getSystem().equals(CodingSystem.Patient.IDENTIFIER)).findAny();
		identifier.ifPresent(value -> data.setIdentificationNumber(value.getValue()));
		Optional<Identifier> refepsIdentifier = resource.getIdentifier().stream().filter(i -> i.getSystem().equals(CodingSystem.Practitioner.IDENTIFIER_REFEPS)).findAny();
		refepsIdentifier.ifPresent(value -> data.setRefeps(value.getValue()));

		if(resource.hasName()){
			HumanName humanName = resource.getName().get(0);
			List<StringType> names = humanName.getGiven();
			data.setTextName(humanName.getText());
			try {
				data.setFirstName(names.get(0).getValue());
				data.setMiddleNames(names.get(1).getValue());
			}
			catch (IndexOutOfBoundsException  ex){
				//nothing to do
			}
			data.setLastName(humanName.getFamily());
			if(humanName.hasFamilyElement()) {
				String otherLastNames = humanName.getFamilyElement().getExtension()
						.stream()
						.map(e -> e.getValue().toString())
						.filter(e -> !e.equals(data.getLastName()))
						.collect(Collectors.joining(" "));
				if(FhirString.hasText(otherLastNames))
					data.setOtherLastNames(otherLastNames);
			}
		}
		if(resource.hasGender())
			data.setGender(resource.getGender().getDisplay());
		if(resource.hasBirthDate())
			data.setBirthDate(FhirDateMapper.toLocalDate(resource.getBirthDate()));
		if(resource.hasTelecom())
			data.setPhoneNumber(resource.getTelecom().get(0).getValue());
		if(resource.hasAddress())
			data.setFullAddress(decodeAddress(resource.getAddress().get(0)));

		return data;
	}
}
