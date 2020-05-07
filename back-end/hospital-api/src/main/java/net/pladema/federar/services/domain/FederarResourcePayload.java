package net.pladema.federar.services.domain;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FederarResourcePayload {
	
	public static final String PATIENT_TYPE="Patient";
	
	private String resourceType;
	private String id;
	private List<FederarIdentifierPayload> identifier;
	private Boolean active;
	private List<FederarNamePayload> name;
	private String gender;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

}
