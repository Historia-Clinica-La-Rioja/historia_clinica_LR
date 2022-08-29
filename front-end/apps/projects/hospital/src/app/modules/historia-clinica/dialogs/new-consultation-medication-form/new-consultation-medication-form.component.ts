import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { MedicacionesNuevaConsultaService } from '../../modules/ambulatoria/services/medicaciones-nueva-consulta.service';

@Component({
  selector: 'app-new-consultation-medication-form',
  templateUrl: './new-consultation-medication-form.component.html',
  styleUrls: ['./new-consultation-medication-form.component.scss']
})
export class NewConsultationMedicationFormComponent {

  readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
  hasError = hasError;

  constructor(
    public dialogRef: MatDialogRef<NewConsultationMedicationFormComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: MedicationData,
  ) { }

  addMedication(): void {
    if (this.data.medicationService.addToList()) {
      this.dialogRef.close();
    }
  }

  close(): void {
    this.data.medicationService.resetForm();
    this.dialogRef.close()
  }

}

interface MedicationData {
  medicationService: MedicacionesNuevaConsultaService,
  searchConceptsLocallyFF: boolean,
}