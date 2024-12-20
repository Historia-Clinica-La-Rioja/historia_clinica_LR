package net.pladema.clinichistory.outpatient.application.port.output;

import java.util.Optional;

public interface GetOdontologyDocumentIdPort {

	Optional<Long> run(Integer healthConditionId);
}
