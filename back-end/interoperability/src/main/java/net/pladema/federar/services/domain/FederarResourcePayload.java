package net.pladema.federar.services.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
public class FederarResourcePayload {

	private static final String FEMALE = "female";
	private static final String MALE = "male";

	private static final String USE_OFFICIAL = "official";
	private static final String FAMILY_URL = "http://hl7.org/fhir/StructureDefinition/humanname-fathers-family";
	private static final String DNI_URL = "http://www.renaper.gob.ar/dni";
	
	public static final String PATIENT_TYPE="Patient";

	private String resourceType = PATIENT_TYPE;

	private String id;

	private List<FederarIdentifierPayload> identifier;

	private Boolean active;
	@EqualsAndHashCode.Include
	private List<FederarNamePayload> name;
	@EqualsAndHashCode.Include
	private String gender;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@EqualsAndHashCode.Include
	private LocalDate birthDate;

	public FederarResourcePayload(FederarResourceAttributes attributes){
		setId(attributes.getPatientId());
		setGender(attributes.getGenderId());
		setName(attributes.getFirstName(), attributes.getMiddleNames(), attributes.getLastName(), attributes.getOtherLastNames());
		setBirthDate(attributes.getBirthDate());
	}

	private void setGender(Short genderId){
		this.gender = genderId == (short)1 ? FEMALE : MALE;
	}

	private void setGender(String gender){
		this.gender = gender;
	}

	private void setName(String firstname, String middlenames, String lastname, String otherlastnames){
		FederarNamePayload namePayload = new FederarNamePayload();
		namePayload.setUse(USE_OFFICIAL);
		namePayload.setText(Stream.of(firstname, middlenames, lastname, otherlastnames)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" ")));
		namePayload.setFamily(lastname);
		namePayload.setGiven(Stream.of(firstname, middlenames).filter(Objects::nonNull)
				.collect(Collectors.toList()));

		FederarExtensionPayload familyExtension = new FederarExtensionPayload(FAMILY_URL, lastname);
		namePayload.setFamilyExtension(new FederarFamilyPayload(Collections.singletonList(familyExtension)));

		this.name = Collections.singletonList(namePayload);
	}

	public void setIdentifier(String domain, FederarResourceAttributes attributes){
		this.identifier = new ArrayList<>();
		this.identifier.add(new FederarIdentifierPayload(domain, attributes.getPatientId()));
		this.identifier.add(new FederarIdentifierPayload(DNI_URL, attributes.getIdentificationNumber()));
	}

	public boolean isSamePatient(FederarResourcePayload requestBody) {
		return gender.equals(requestBody.gender) &&
				birthDate.equals(requestBody.birthDate) &&
				(name.size() == requestBody.name.size() && name.containsAll(requestBody.name));
	}
}
