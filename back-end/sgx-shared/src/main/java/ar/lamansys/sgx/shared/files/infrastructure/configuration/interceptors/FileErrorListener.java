package ar.lamansys.sgx.shared.files.infrastructure.configuration.interceptors;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ar.lamansys.sgx.shared.files.infrastructure.output.repository.FileErrorInfoRepository;

@Component
public class FileErrorListener {
	private final FileErrorInfoRepository errorInfoRepository;

	public FileErrorListener(FileErrorInfoRepository errorInfoRepository) {
		super();
		this.errorInfoRepository = errorInfoRepository;
	}

	@Async
	@EventListener
	void saveFileError(FileErrorEvent event) {
		errorInfoRepository.saveAndFlush(event.getFileErrorInfo());
	}
}