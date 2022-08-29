import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AlergiasNuevaConsultaService } from '../../modules/ambulatoria/services/alergias-nueva-consulta.service';

@Component({
  selector: 'app-new-consultation-allergy-form',
  templateUrl: './new-consultation-allergy-form.component.html',
  styleUrls: ['./new-consultation-allergy-form.component.scss']
})
export class NewConsultationAllergyFormComponent {

  constructor(
    public dialogRef: MatDialogRef<NewConsultationAllergyFormComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: AllergyData,
  ) { }

  addAllergy(): void {
    if (this.data.allergyService.addToList()) {
      this.dialogRef.close();
    }
  }

  close(): void {
    this.data.allergyService.resetForm();
    this.dialogRef.close()
  }

}

interface AllergyData {
  allergyService: AlergiasNuevaConsultaService,
  searchConceptsLocallyFF: boolean,
}