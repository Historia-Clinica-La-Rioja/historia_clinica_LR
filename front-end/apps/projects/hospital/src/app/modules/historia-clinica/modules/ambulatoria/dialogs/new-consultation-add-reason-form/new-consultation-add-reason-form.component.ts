import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';

@Component({
  selector: 'app-new-consultation-add-reason-form',
  templateUrl: './new-consultation-add-reason-form.component.html',
  styleUrls: ['./new-consultation-add-reason-form.component.scss']
})
export class NewConsultationAddReasonFormComponent {

  constructor(
    public dialogRef: MatDialogRef<NewConsultationAddReasonFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      reasonService: MotivoNuevaConsultaService,
      searchConceptsLocallyFF: boolean,
    },
  ) { }

  addReason(): void {
    this.data.reasonService.addToList();
    this.dialogRef.close();
  }

  close(): void {
    this.data.reasonService.resetForm();
    this.dialogRef.close()
  }

}
