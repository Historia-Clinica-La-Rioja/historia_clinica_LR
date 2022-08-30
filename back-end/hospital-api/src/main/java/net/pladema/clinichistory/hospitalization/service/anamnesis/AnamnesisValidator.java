package net.pladema.clinichistory.hospitalization.service.anamnesis;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import net.pladema.clinichistory.hospitalization.repository.domain.InternmentEpisode;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.hospitalization.service.documents.validation.InternmentDocumentValidator;
import org.springframework.stereotype.Component;
import javax.validation.ConstraintViolationException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class AnamnesisValidator extends InternmentDocumentValidator {

	private final FeatureFlagsService featureFlagsService;


	public void assertAnamnesisValid(AnamnesisBo anamnesis) {
		anamnesis.validateSelf();
		assertMainDiagnosisValid(anamnesis);
		super.assertDocumentValid(anamnesis);
	}

	public void assertDoesNotHaveAnamnesis(InternmentEpisode internmentEpisode) {
		if(internmentEpisode.getAnamnesisDocId() != null) {
			throw new ConstraintViolationException("Esta internación ya posee una anamnesis", Collections.emptySet());
		}
	}

	private void assertMainDiagnosisValid(AnamnesisBo anamnesis) {
		if (featureFlagsService.isOn(AppFeature.MAIN_DIAGNOSIS_REQUIRED)
				&& anamnesis.getMainDiagnosis() == null)
			throw new ConstraintViolationException("Diagnóstico principal obligatorio", Collections.emptySet());
		if (anamnesis.getMainDiagnosis() == null)
			return;
		if (anamnesis.getAlternativeDiagnosis() == null)
			return;
		SnomedBo snomedMainDiagnosis = anamnesis.getMainDiagnosis().getSnomed();
		if(anamnesis.getAlternativeDiagnosis().stream()
				.map(DiagnosisBo::getSnomed)
				.anyMatch(d -> d.equals(snomedMainDiagnosis))){
			throw new ConstraintViolationException("Diagnostico principal duplicado en los secundarios", Collections.emptySet());
		}
	}
}
