package ar.lamansys.sgh.shared.infrastructure.input.service.datastructures;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageDto", implementation = PageDto.class)
public class PageDto<T> {

	private List<T> content;

	private Long totalElementsAmount;

	public static <T> PageDto<T> fromPage(Page<T> page) {
		PageDto<T> result = new PageDto<>();
		result.setContent(page.getContent());
		result.setTotalElementsAmount(page.getTotalElements());
		return result;
	}

}
