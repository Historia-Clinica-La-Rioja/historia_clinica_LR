package net.pladema.access.application;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import net.pladema.access.infrastructure.output.repository.ClinicHistoryAccessRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ClinicHistoryAccessServiceImpl implements ClinicHistoryAccessService{

	private final ClinicHistoryAccessRepository clinicHistoryAccessRepository;

	@Override
	public Boolean has24HoursAccessByPatientId(Integer patientId) {
		return clinicHistoryAccessRepository.findByPatientIdAndUserIdOrderByAccessDateDesc(patientId, UserInfo.getCurrentAuditor())
				.stream()
				.findFirst()
				.map(audit -> {
					LocalDateTime lastAccessDate = audit.getAccessDate();
					return lastAccessDate.isAfter(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)) &&
							lastAccessDate.isBefore(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59));
				})
				.orElse(false);
	}
}
