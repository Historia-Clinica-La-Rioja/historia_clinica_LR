package ar.lamansys.sgh.shared.infrastructure.input.service.observation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Column;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FhirDiagnosticReportPerformersDto {

	@AllArgsConstructor
	@Getter
	public class Organization {
		private String name;
		private String address;
		private String city;
		private String postcode;
		private String province;
		private String country;
		private String phoneNumber;
		private String email;
	}

	@AllArgsConstructor
	@Getter
	public class Practitioner {
		private String identificationNumber;
		private String firstName;
		private String lastName;
	}

	private List<Organization> organizations;
	private List<Practitioner> practitioners;

	public FhirDiagnosticReportPerformersDto() {
		this.organizations = new ArrayList<>();
		this.practitioners = new ArrayList<>();
	}

	public void addOrganization(String name, String address, String city, String postcode, String province,
		String country, String phoneNumber, String email) {
		if (organizations == null) organizations = new ArrayList<>();
		organizations.add(new Organization(name, address, city, postcode, province, country, phoneNumber, email));
	}

	public void addPractitioner(String identificationNumber, String firstName, String lastName) {
		if (practitioners == null) practitioners = new ArrayList<>();
		practitioners.add(new Practitioner(identificationNumber, firstName, lastName));
	}
}
