import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EndDateColor, IsolationAlertDetail } from '@historia-clinica/components/isolation-alert-detail/isolation-alert-detail.component';

@Component({
  selector: 'app-details-of-patient-isolation-alerts',
  templateUrl: './details-of-patient-isolation-alerts.component.html',
  styleUrls: ['./details-of-patient-isolation-alerts.component.scss']
})
export class DetailsOfPatientIsolationAlertsComponent {
  
	readonly END_DATE_COLOR = EndDateColor;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DetailsOfPatientIsolationAlerts,
  ) { }

}

export interface DetailsOfPatientIsolationAlerts {
  isolationAlerts: IsolationAlertDetail[];
}
