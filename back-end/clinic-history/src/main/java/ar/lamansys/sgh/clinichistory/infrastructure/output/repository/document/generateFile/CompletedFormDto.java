package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompletedFormDto {

	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Parameter {
		private String description;
		private String value;
	}

	private Integer id;
	private String name;
	private List<Parameter> parameters;

	public CompletedFormDto(Integer id, String name){
		this.id = getId();
		this.name = name;
		this.parameters = new ArrayList<>();
	}

}
