package net.pladema.clinichistory.requests.medicationrequests.service.impl.notification;

import java.util.List;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NewMedicationRequestNotificationArgs {

	private BasicPatientDto patient;

	private String recipeIdWithDomain;

	private Integer recipeId;

	private List<StoredFileBo> resources;
}
