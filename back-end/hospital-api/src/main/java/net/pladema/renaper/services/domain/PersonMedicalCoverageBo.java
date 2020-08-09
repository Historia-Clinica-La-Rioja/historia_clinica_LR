package net.pladema.renaper.services.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import net.pladema.person.repository.entity.HealthInsurance;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonMedicalCoverageBo {

	private String rnos;
	private String name;
	private String service;
	private String acronym;
	private String dateQuery;
	
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public PersonMedicalCoverageBo(@JsonProperty("rnos") String rnos, @JsonProperty("cobertura") String cobertura, @JsonProperty("servicio")  String servicio, @JsonProperty("fechaConsulta") String fechaConsulta) {
		this.rnos = rnos;
		this.name = cobertura;
		this.service = servicio;
		this.dateQuery = fechaConsulta;
	}

	public PersonMedicalCoverageBo(HealthInsurance healthInsurance){
		this.rnos = healthInsurance.getRnos().toString();
		this.name = healthInsurance.getName();
		this.acronym = healthInsurance.getAcronym();
	}

	@Override
	public String toString() {
		return "MedicalCoveragePersonResponse [" + (rnos != null ? "rnos=" + rnos + ", " : "")
				+ (name != null ? "cobertura=" + name + ", " : "")
				+ (service != null ? "servicio=" + service + ", " : "")
				+ (dateQuery != null ? "fechaConsulta=" + dateQuery : "") + "]";
	}
	
}
