import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonType } from '@presentation/components/button/button.component';

@Component({
  selector: 'app-emergency-care-temporary-patient',
  templateUrl: './emergency-care-temporary-patient.component.html',
  styleUrls: ['./emergency-care-temporary-patient.component.scss']
})
export class EmergencyCareTemporaryPatientComponent {

  patientDescription: string;
  BUTTON_TYPE_ICON = ButtonType.ICON;

  @Input() canDeleteSelectedPatient = true;
  @Input() set preloadedDescriptionPatient(patientDescription: string) {
    if (patientDescription) {
      this.patientDescription = patientDescription;
      this.selectedPatientDescription.emit(this.patientDescription);
    }
  };
  @Output() selectedPatientDescription = new EventEmitter<string>;

  constructor() { }

  clearSelectedTemporaryPatient() {
    this.patientDescription = null;
    this.selectedPatientDescription.emit(null);
  }

}
