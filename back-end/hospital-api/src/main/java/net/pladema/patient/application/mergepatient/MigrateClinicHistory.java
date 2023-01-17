package net.pladema.patient.application.mergepatient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrateClinicHistory {

	private final MigrateInternmentEpisode migrateInternmentEpisode;

	public void execute(List<Integer> oldPatients, Integer newPatient) {

		migrateInternmentEpisode.execute(oldPatients,newPatient);

	}
}
