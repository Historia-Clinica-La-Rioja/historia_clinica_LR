package ar.lamansys.sgh.publicapi.userinformation.infrastructure.output;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import ar.lamansys.sgh.publicapi.UnitRepository;
import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserPersonFromTokenBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.user.dto.UserSharedInfoDto;

@ExtendWith(MockitoExtension.class)
public class FetchUserPersonFromTokenStorageImplTest extends UnitRepository {

	private static final String TOKEN = "token";

	private FetchUserPersonFromTokenStorageImpl fetchUserPersonFromTokenStorage;

	@Autowired
	private FetchUserPersonFromTokenRepository fetchUserPersonFromTokenRepository;

	@MockBean
	private SharedHospitalUserPort sharedHospitalUserPort;

	@BeforeEach
	void setup() {
		this.fetchUserPersonFromTokenStorage = new FetchUserPersonFromTokenStorageImpl(sharedHospitalUserPort, fetchUserPersonFromTokenRepository);
	}

	@Test
	void testSuccessfulSaveAndGet() {
		Integer madeUpUserId = 1;
		String madeUpUsername = "username";
		tokenSuccessfullyDecoded(madeUpUserId, madeUpUsername);
		UserPersonData userPersonData = madeUpUserPersonEntity(madeUpUserId, madeUpUsername);
		savePersonData(userPersonData);
		shouldBeSamePerson(userPersonData, getSavedPerson());
	}

	private void shouldBeSamePerson(UserPersonData userPersonData, FetchUserPersonFromTokenBo savedPersonData) {
		Assertions.assertEquals(savedPersonData.getId(), userPersonData.getId());
		Assertions.assertEquals(savedPersonData.getCuil(), userPersonData.getCuil());
		Assertions.assertEquals(savedPersonData.getEmail(), userPersonData.getEmail());
		Assertions.assertEquals(savedPersonData.getSub(), userPersonData.getSub());
		Assertions.assertEquals(savedPersonData.getIdentificationNumber(), userPersonData.getIdentificationNumber());
		Assertions.assertEquals(savedPersonData.getIdentificationNumber(), userPersonData.getIdentificationNumber());
	}

	private FetchUserPersonFromTokenBo getSavedPerson() {
		var personInfo = fetchUserPersonFromTokenStorage.getUserPersonFromToken(TOKEN);
		Assertions.assertTrue(personInfo.isPresent());
		return personInfo.get();
	}

	private static UserPersonData madeUpUserPersonEntity(Integer madeUpUserId, String madeUpUsername) {
		return new UserPersonData(
				madeUpUserId,
				madeUpUsername,
				madeUpUsername + "@mail.com",
				madeUpUsername + "_name",
				madeUpUsername + "_givenName",
				madeUpUsername + "_cuil",
				madeUpUsername + "_type",
				madeUpUsername + "_idNumber",
				"M");
	}

	private void savePersonData(UserPersonData userPersonData) {
		save(userPersonData);
	}

	private void tokenSuccessfullyDecoded(Integer madeUpId, String madeUpUsername) {
		when(sharedHospitalUserPort.fetchUserInfoFromNormalToken(any()))
				.thenReturn(Optional.of(new UserSharedInfoDto(madeUpId, madeUpUsername)));
	}
}

