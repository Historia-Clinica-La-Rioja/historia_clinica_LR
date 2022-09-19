package ar.lamansys.sgh.clinichistory.application.updatenursingrecordstatus;

import ar.lamansys.sgh.clinichistory.application.indication.updatenursingrecordstatus.UpdateNursingRecordStatus;

import ar.lamansys.sgh.clinichistory.application.ports.IndicationStorage;
import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;

import ar.lamansys.sgh.clinichistory.domain.ips.DietBo;
import ar.lamansys.sgh.clinichistory.domain.ips.NursingRecordBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ENursingRecordStatus;

import ar.lamansys.sgx.shared.security.UserInfo;

import io.jsonwebtoken.lang.Assert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class UpdateNursingRecordStatusTest {

	private UpdateNursingRecordStatus updateNursingRecordStatus;

	@Mock
	private NursingRecordStorage nursingRecordStorage;

	@Mock
	private IndicationStorage indicationStorage;

	@BeforeEach
	void setUp(){
		updateNursingRecordStatus = new UpdateNursingRecordStatus(nursingRecordStorage, indicationStorage);
	}

	@Test
	void test_updateNursingRecordStatus_success() {

		Integer id = 1;
		Integer indicationId = 2;
		ENursingRecordStatus completedStatus = ENursingRecordStatus.COMPLETED;
		LocalDateTime administrationTime = LocalDateTime.now();
		Integer userId = UserInfo.getCurrentAuditor();
		DietBo diet = new DietBo(indicationId, 1, EIndicationType.DIET.getId(), EIndicationStatus.INDICATED.getId(), userId, null, LocalDate.now(), null, "description");
		NursingRecordBo updatedNursingRecordBo = new NursingRecordBo(id, diet, null, null, ENursingRecordStatus.COMPLETED.getId(), "description", administrationTime, userId, "name", "reason");

		ArgumentCaptor<Integer> indicationIdCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Short> statusIdCaptor = ArgumentCaptor.forClass(Short.class);
		ArgumentCaptor<Integer> userIdCaptor = ArgumentCaptor.forClass(Integer.class);

		when(nursingRecordStorage.getIndicationIdById(id)).thenReturn(Optional.of(indicationId));
		when(nursingRecordStorage.getIndicationNursingRecords(indicationId)).thenReturn(Arrays.asList(updatedNursingRecordBo));
		when(nursingRecordStorage.updateStatus(id, completedStatus.getId(), administrationTime, UserInfo.getCurrentAuditor(), "reason")).thenReturn(true);

		Assert.isTrue(updateNursingRecordStatus.run(id, completedStatus.getValue(), administrationTime, userId, "reason"));

		verify(indicationStorage, times(1)).updateStatus(indicationIdCaptor.capture(), statusIdCaptor.capture(), userIdCaptor.capture());
		assertThat(indicationIdCaptor.getValue()).isEqualTo(indicationId);
		assertThat(statusIdCaptor.getValue()).isEqualTo(EIndicationStatus.COMPLETED.getId());
		assertThat(userIdCaptor.getValue()).isEqualTo(userId);

	}


}
