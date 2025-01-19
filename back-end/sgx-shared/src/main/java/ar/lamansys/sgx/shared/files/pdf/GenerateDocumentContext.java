package ar.lamansys.sgx.shared.files.pdf;

import java.util.Map;

public interface GenerateDocumentContext<T> {

	Map<String, Object> run(T data);

}
