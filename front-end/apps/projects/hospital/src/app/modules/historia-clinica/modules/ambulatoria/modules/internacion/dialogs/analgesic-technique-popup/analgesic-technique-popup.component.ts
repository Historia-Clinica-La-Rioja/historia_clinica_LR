import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { AnalgesicTechniqueService, Cateter_options } from '../../services/analgesic-technique.service';
import { hasError } from '@core/utils/form.utils';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-analgesic-technique-popup',
  templateUrl: './analgesic-technique-popup.component.html',
  styleUrls: ['./analgesic-technique-popup.component.scss']
})
export class AnalgesicTechniquePopupComponent implements OnInit {

  readonly hasError = hasError;
  form: FormGroup;
  cateterNoteEnable = false
  CATETER_OPTIONS = Cateter_options

  constructor(
    public dialogRef: MatDialogRef<AnalgesicTechniquePopupComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: AnalgesicTechniqueData,
  ) { }


  ngOnInit(): void {
    this.form = this.data.analgesicTechniqueService.getForm()
    this.data.analgesicTechniqueService.getCateterInputStatus().subscribe
    (
        cateterStatus => {
            this.cateterNoteEnable = cateterStatus === this.CATETER_OPTIONS.CATETER_ENABLED
            this.data.analgesicTechniqueService.HandleValidatorRequiredCateterNotes(cateterStatus)
        }
    )
  }

  close(): void {
    this.dialogRef.close()
    this.data.analgesicTechniqueService.resetForm();
}

addAnalgesicTechnique(): void {
    if (this.form.valid) {
        this.data.analgesicTechniqueService.addToList();
        this.dialogRef.close();
    }
}

}

interface AnalgesicTechniqueData {
  analgesicTechniqueService: AnalgesicTechniqueService,
  searchConceptsLocallyFF: boolean,
}