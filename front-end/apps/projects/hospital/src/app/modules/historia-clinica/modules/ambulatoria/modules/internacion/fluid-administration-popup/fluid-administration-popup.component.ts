import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FluidAdministrationService } from '../services/fluid-administration.service';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-fluid-administration-popup',
  templateUrl: './fluid-administration-popup.component.html',
  styleUrls: ['./fluid-administration-popup.component.scss']
})
export class FluidAdministrationPopupComponent implements OnInit {

  form:FormGroup

  constructor(
    public dialogRef: MatDialogRef<FluidAdministrationPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public readonly data: FluidAdministrationPopupData,
  ) { }

  ngOnInit(): void {
    this.form= this.data.fluidAdministrationService.getForm()
  }

  close(): void {
    this.dialogRef.close()
    this.data.fluidAdministrationService.resetForm();
  }

  addFluidAdministration(): void {
    if (this.form.valid) {
      this.data.fluidAdministrationService.addToList();
      this.dialogRef.close();
    }
  }

}

interface FluidAdministrationPopupData {
  fluidAdministrationService: FluidAdministrationService,
  searchConceptsLocallyFF: boolean,
}