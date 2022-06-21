import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MIN_DATE } from '@core/utils/date.utils';
import { AntecedentesFamiliaresNuevaConsultaService } from '../../services/antecedentes-familiares-nueva-consulta.service';

@Component({
  selector: 'app-new-consultation-family-history-form',
  templateUrl: './new-consultation-family-history-form.component.html',
  styleUrls: ['./new-consultation-family-history-form.component.scss']
})
export class NewConsultationFamilyHistoryFormComponent {

  minDate = MIN_DATE;

  constructor(
    public dialogRef: MatDialogRef<NewConsultationFamilyHistoryFormComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: FamilyHistoryData,
  ) { }

  addFamilyHistory(): void {
    if (this.data.familyHistoryService.addToList()) {
      this.dialogRef.close();
    }
  }

  close(): void {
    this.data.familyHistoryService.resetForm();
    this.dialogRef.close()
  }

}

interface FamilyHistoryData {
  familyHistoryService: AntecedentesFamiliaresNuevaConsultaService,
  searchConceptsLocallyFF: boolean,
}