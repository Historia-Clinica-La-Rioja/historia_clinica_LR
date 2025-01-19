package ar.lamansys.sgx.shared.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class CustomContainer<T> {

	protected List<T> content;

	public CustomContainer() {
		content = new ArrayList<>();
	}

}
