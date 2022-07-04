import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProblemasService } from '@historia-clinica/services/problemas.service';
import { MIN_DATE } from '@core/utils/date.utils';

@Component({
  selector: 'app-new-consultation-add-diagnose-form',
  templateUrl: './new-consultation-add-diagnose-form.component.html',
  styleUrls: ['./new-consultation-add-diagnose-form.component.scss']
})
export class NewConsultationAddDiagnoseFormComponent {

  readonly today = new Date();
  readonly minDate = MIN_DATE;


  constructor(
    public dialogRef: MatDialogRef<NewConsultationAddDiagnoseFormComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: DiagnosesData,
  ) { }

  addDiagnose() {
    if (this.data.diagnosesService.addToList()) {
      this.dialogRef.close();
    }
  }

  close(): void {
    this.data.diagnosesService.resetForm();
    this.dialogRef.close()
  }

}

interface DiagnosesData {
  diagnosesService: ProblemasService,
  searchConceptsLocallyFF: boolean,
  severityTypes: any[]
}
