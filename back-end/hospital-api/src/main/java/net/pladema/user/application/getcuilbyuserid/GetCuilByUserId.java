package net.pladema.user.application.getcuilbyuserid;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import net.pladema.user.application.port.HospitalUserStorage;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetCuilByUserId {

	private final HospitalUserStorage hospitalUserStorage;

	public Optional<String> run(Integer userId){
		log.debug("Input parameters -> userId {}", userId);
		Optional<String> result = hospitalUserStorage.getCuilByUserId(userId);
		log.debug("Output result -> {}", result);
		return result;
	}
}
