package net.pladema.clinichistory.requests.medicationrequests.service.impl.notification;

import java.util.HashMap;

import org.springframework.core.io.ByteArrayResource;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NewMedicationRequestNotificationArgs {

	private BasicPatientDto patient;

	private Integer recipeId;

	private HashMap<String, ByteArrayResource> resources;
}
