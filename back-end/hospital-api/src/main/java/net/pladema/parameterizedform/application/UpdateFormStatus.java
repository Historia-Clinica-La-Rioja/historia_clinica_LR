package net.pladema.parameterizedform.application;

import lombok.RequiredArgsConstructor;

import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;

import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UpdateFormStatus {

	private final ParameterizedFormStorage formStorage;

	public void run(Integer id) {
		formStorage.updateStatus(id);
	}
}
