import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MIN_DATE } from '@core/utils/date.utils';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';

@Component({
  selector: 'app-new-consultation-procedure-form',
  templateUrl: './new-consultation-procedure-form.component.html',
  styleUrls: ['./new-consultation-procedure-form.component.scss']
})
export class NewConsultationProcedureFormComponent {

  minDate = MIN_DATE;

  constructor(
    public dialogRef: MatDialogRef<NewConsultationProcedureFormComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: ProcedureData,
  ) { }

  addProcedure(): void {
    if (this.data.procedureService.addToList()) {
      this.dialogRef.close();
    }
  }

  close(): void {
    this.data.procedureService.resetForm();
    this.dialogRef.close()
  }

}

interface ProcedureData {
  procedureService: ProcedimientosService,
  searchConceptsLocallyFF: boolean,
}