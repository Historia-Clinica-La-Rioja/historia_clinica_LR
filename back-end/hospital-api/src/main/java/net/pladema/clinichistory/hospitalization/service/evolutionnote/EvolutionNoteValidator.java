package net.pladema.clinichistory.hospitalization.service.evolutionnote;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.documents.validation.InternmentDocumentValidator;
import net.pladema.clinichistory.hospitalization.service.evolutionnote.domain.EvolutionNoteBo;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.exceptions.PermissionDeniedException;
import net.pladema.sgx.session.infrastructure.input.service.FetchLoggedUserRolesExternalService;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EvolutionNoteValidator extends InternmentDocumentValidator {

	private final FetchLoggedUserRolesExternalService fetchLoggedUserRolesExternalService;

	private final InternmentEpisodeService internmentEpisodeService;

	public void validateRolePermission(EvolutionNoteBo evolutionNote) {
		if (evolutionNote.getIsNursingEvolutionNote() && !isNursingProfessional(evolutionNote)
				|| (!evolutionNote.getIsNursingEvolutionNote() && isNursingProfessional(evolutionNote)))
			throw new ConstraintViolationException("El usuario no posee el rol correspondiente para modificar la nota de evolución", Collections.emptySet());
	}

	public void validateNursePermissionToLoadProcedures(EvolutionNoteBo evolutionNote) {


		if(evolutionNote.getProcedures() != null && !evolutionNote.getProcedures().isEmpty()
				&& this.isNursingProfessional(evolutionNote)) {
			throw new PermissionDeniedException(
					String.format("Los usuarios con roles %s y %s no tienen permiso para agregar un procedimiento a una nota de evolución",
							ERole.ENFERMERO.getValue(), ERole.ENFERMERO_ADULTO_MAYOR.getValue()));
		}
	}

	public void assertDoesNotHaveEpicrisis(Integer internmentEpisodeId) {
		if(internmentEpisodeService.haveEpicrisis(internmentEpisodeId)){
				throw new ConstraintViolationException("Esta internación ya posee una epicrisis", Collections.emptySet());
		}
	}


	public void assertEvolutionNoteValid(EvolutionNoteBo evolutionNote) {
		evolutionNote.validateSelf();
		super.assertDocumentValid(evolutionNote);
	}


	public EvolutionNoteBo verifyEvolutionNoteDiagnosis(EvolutionNoteBo evolutionNote, HealthConditionBo mainDiagnosis) {
		if (evolutionNote.getDiagnosis().isEmpty() && mainDiagnosis != null)
			evolutionNote.setMainDiagnosis(new DiagnosisBo(mainDiagnosis.getSnomed()));
		return evolutionNote;
	}

	private boolean isNursingProfessional(EvolutionNoteBo evolutionNote) {
		var institutionId = evolutionNote.getInstitutionId();

		var roles = fetchLoggedUserRolesExternalService.execute().collect(Collectors.toList());
		var isNurse = roles.stream().anyMatch(r -> r.institutionId.equals(institutionId)
				&& (r.role.equals(ERole.ENFERMERO)
				|| r.role.equals(ERole.ENFERMERO_ADULTO_MAYOR))
		);
		var isProfessionalOrSpecialist = roles.stream().anyMatch(r -> r.institutionId.equals(institutionId)
				&& (r.role.equals(ERole.ESPECIALISTA_MEDICO)
				|| r.role.equals(ERole.PROFESIONAL_DE_SALUD)
				|| r.role.equals(ERole.ESPECIALISTA_EN_ODONTOLOGIA))
		);

		return isNurse && ! isProfessionalOrSpecialist;
	}
}
