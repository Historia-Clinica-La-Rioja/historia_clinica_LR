package net.pladema.clinichistory.requests.medicationrequests.service.impl.notification;

import java.util.List;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.Builder;

@Builder
public class NewMedicationRequestNotificationArgs {
	public final boolean selfPerceivedFF;
	public final BasicPatientDto patient;
	public final String recipeIdWithDomain;
	public final Integer recipeId;
	public final List<StoredFileBo> resources;
}
