package ar.lamansys.sgx.shared.validation;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

import java.util.List;

public class CustomContainerValueExtractor implements ValueExtractor<CustomContainer<@ExtractedValue ?>> {

	@Override
	public void extractValues(CustomContainer<?> container, ValueReceiver valueReceiver) {
		List<?> content = container.getContent();
		for (int i = 0; i < content.size(); i++)
			valueReceiver.indexedValue("<iterable element>", i , content.get(i));
	}

}
