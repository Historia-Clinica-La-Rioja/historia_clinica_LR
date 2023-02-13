package net.pladema.clinichistory.requests.medicationrequests.service;

import net.pladema.clinichistory.requests.medicationrequests.service.impl.notification.NewMedicationRequestNotificationArgs;

public interface NewMedicationRequestNotification {

	void run(NewMedicationRequestNotificationArgs args);
}
