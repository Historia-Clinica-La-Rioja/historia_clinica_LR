package net.pladema.user.application.fetchuserdatafromtoken;

import lombok.extern.slf4j.Slf4j;

import net.pladema.user.application.port.HospitalUserStorage;
import net.pladema.user.domain.PersonDataBo;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class FetchUserDataFromToken {
	private final HospitalUserStorage hospitalUserStorage;

	public FetchUserDataFromToken(HospitalUserStorage hospitalUserStorage) {
		this.hospitalUserStorage = hospitalUserStorage;
	}

	public Optional<PersonDataBo> execute(String token) {
		return hospitalUserStorage.fetchUserIdFromNormalToken(token)
				.map(hospitalUserStorage::getPersonDataBoByUserId);
	}
}
