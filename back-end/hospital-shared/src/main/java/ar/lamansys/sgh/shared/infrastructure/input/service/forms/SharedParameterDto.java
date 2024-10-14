package ar.lamansys.sgh.shared.infrastructure.input.service.forms;

import ar.lamansys.sgh.shared.domain.forms.enums.EParameterType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SharedParameterDto {

	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	public static class TextOptionDto {
		private Integer id;
		private String description;
	}

	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	public static class UnitOfMeasureDto {
		private Integer id;
		private String description;
		private	String code;
	}

	private Integer id;
	private Integer loincId;
	private String description;
	private EParameterType type;
	private Short inputCount;
	private Integer snomedGroupId;
	private UnitOfMeasureDto unitOfMeasure;
	private List<TextOptionDto> textOptions;
	private Short orderNumber;

	public SharedParameterDto(Integer id, Short orderNumber){
		this.id = id;
		this.orderNumber = orderNumber;
	}

}
