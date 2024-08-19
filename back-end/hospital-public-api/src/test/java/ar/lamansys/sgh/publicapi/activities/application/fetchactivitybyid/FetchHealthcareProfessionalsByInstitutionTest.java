package ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import ar.lamansys.sgh.publicapi.activities.staff.application.exception.InstitutionNotExistsException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.lamansys.sgh.publicapi.TestUtils;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.service.ActivitiesPublicApiPermissions;
import ar.lamansys.sgh.publicapi.activities.staff.application.FetchHealthcareProfessionalsByInstitution;
import ar.lamansys.sgh.publicapi.activities.staff.application.exception.FetchHealthcareProfessionalsDeniedException;
import ar.lamansys.sgh.publicapi.activities.staff.application.port.out.FetchHealthcareProfessionalsByInstitutionStorage;
import ar.lamansys.sgh.publicapi.activities.staff.infrastructure.input.rest.FetchHealthcareProfessionalsByInstitutionController;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.LicenseNumberDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.MedicineDoctorCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionSpecialtyDto;

@ExtendWith(MockitoExtension.class)
public class FetchHealthcareProfessionalsByInstitutionTest {


	private FetchHealthcareProfessionalsByInstitutionController fetchHealthcareProfessionalsByInstitutionController;

	@Mock
	private FetchHealthcareProfessionalsByInstitutionStorage fetchHealthcareProfessionalsByInstitutionStorage;

	@Mock
	private ActivitiesPublicApiPermissions activitiesPublicApiPermissions;

	@BeforeEach
	public void setup() {
		FetchHealthcareProfessionalsByInstitution healthcareProfessionalsByInstitution =
				new FetchHealthcareProfessionalsByInstitution(fetchHealthcareProfessionalsByInstitutionStorage, activitiesPublicApiPermissions);
		this.fetchHealthcareProfessionalsByInstitutionController =
				new FetchHealthcareProfessionalsByInstitutionController(healthcareProfessionalsByInstitution);
	}

	@Test
	void failFetchHealthcareProfessionalsDeniedException() {
		allowAccessPermission(false);
		when(activitiesPublicApiPermissions.findInstitutionId("1")).thenReturn(Optional.of(1));
		TestUtils.shouldThrow(FetchHealthcareProfessionalsDeniedException.class,
				() -> fetchHealthcareProfessionalsByInstitutionController.getProfessionalsByInstitution("1"));
	}

	@Test
	void successFetchHealthcareProfessionals(){
		allowAccessPermission(true);
		when(activitiesPublicApiPermissions.findInstitutionId("1")).thenReturn(Optional.of(1));
		when(fetchHealthcareProfessionalsByInstitutionStorage.fetch(1)).thenReturn(List.of(mockResult()));
		var result = fetchHealthcareProfessionalsByInstitutionController.getProfessionalsByInstitution("1");
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(result.get(0).getProfessions().size(), 1);
	}

	@Test
	void institutionNotFoundEmptyResult(){
		when(activitiesPublicApiPermissions.findInstitutionId("1")).thenReturn(Optional.empty());
		TestUtils.shouldThrow(InstitutionNotExistsException.class,
				() -> fetchHealthcareProfessionalsByInstitutionController.getProfessionalsByInstitution("1"));
	}

	private void allowAccessPermission(boolean canAccess) {
		when(activitiesPublicApiPermissions.canFetchHealthcareProfessionals(any())).thenReturn(canAccess);
	}

	private MedicineDoctorCompleteDto mockResult(){
		return MedicineDoctorCompleteDto.builder()
				.firstName("first")
				.middleNames("middle")
				.lastName("last")
				.nameSelfDetermination("selfdet")
				.otherLastNames("other")
				.identificationType("DNI")
				.identificationNumber("12345678")
				.professions(List.of(new ProfessionCompleteDto(
						1,
						1,
						"profession",
						List.of(new LicenseNumberDto(
							1,
							"98989",
							"MP"
						)),
						List.of(new ProfessionSpecialtyDto(
							1,
							new ClinicalSpecialtyDto(1, "specialty"),
							List.of(new LicenseNumberDto(
									1,
									"98989",
									"MP"
							))
						)))))
				.build();
	}
}
