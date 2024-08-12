import { Component, Input } from '@angular/core';
import { Episode } from '../emergency-care-patients-summary/emergency-care-patients-summary.component';
import { PatientType } from '@historia-clinica/constants/summaries';

@Component({
  selector: 'app-emergency-care-patient-summary-item',
  templateUrl: './emergency-care-patient-summary-item.component.html',
  styleUrls: ['./emergency-care-patient-summary-item.component.scss']
})
export class EmergencyCarePatientSummaryItemComponent {

  readonly PACIENTE_TEMPORAL = 3;
	readonly EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;

  @Input() episode: Episode;

  constructor() { }

}
