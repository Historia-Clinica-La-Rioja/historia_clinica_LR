package ar.lamansys.sgx.shared.files.infrastructure.configuration.interceptors;

import org.springframework.context.ApplicationEvent;

import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileErrorInfo;
import lombok.Getter;


@Getter
public class FileErrorEvent extends ApplicationEvent {
	/**
	 *
	 */
	private static final long serialVersionUID = 6147904039545771860L;
	private final FileErrorInfo fileErrorInfo;

	public FileErrorEvent(FileErrorInfo fileErrorInfo) {
		super(fileErrorInfo);
		this.fileErrorInfo = fileErrorInfo;
	}

}