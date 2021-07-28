import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ClinicalSpecialtyDto, ImmunizationDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { AplicarVacuna2Component } from '../aplicar-vacuna-2/aplicar-vacuna-2.component';

@Component({
  selector: 'app-agregar-vacunas',
  templateUrl: './agregar-vacunas.component.html',
  styleUrls: ['./agregar-vacunas.component.scss']
})
export class AgregarVacunasComponent implements OnInit {

  form: FormGroup;
  fixedSpecialty = true;
  defaultSpecialty: ClinicalSpecialtyDto;
  specialties: ClinicalSpecialtyDto[];
  appliedVaccines: ImmunizationDto[];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { patientId: number },
    private readonly formBuilder: FormBuilder,
    private readonly dialog: MatDialog,
    private readonly clinicalSpecialtyService: ClinicalSpecialtyService
  ) {
    this.appliedVaccines = [];
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      clinicalSpecialty: []
    });

    this.setLoggedProfessionalSpecialties();
  }

  setLoggedProfessionalSpecialties() {
    this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
      this.specialties = specialties;
      this.fixedSpecialty = false;
      this.defaultSpecialty = specialties[0];
      this.form.get('clinicalSpecialty').setValue(this.defaultSpecialty);
    });
  }

  setDefaultSpecialty() {
    this.defaultSpecialty = this.form.controls.clinicalSpecialty.value;
  }

  displayDate(vaccine: ImmunizationDto): string {
    return momentFormat(momentParseDate(vaccine.administrationDate));
  }

  remove(vaccine: ImmunizationDto) {
    this.appliedVaccines.forEach((value, index) => {
      if (value.snomed.sctid == vaccine.snomed.sctid) // need change
        this.appliedVaccines.splice(index, 1);
    });
  }

  edit(vaccine: ImmunizationDto) {
    const dialogRef = this.dialog.open(AplicarVacuna2Component, { // maybe need change
      disableClose: true,
      width: '45%',
      data: {
        patientId: this.data.patientId,
        immunization: vaccine
      }
    });

    dialogRef.afterClosed().subscribe(submitted => {
      if (submitted) {
        this.remove(vaccine);
        this.appliedVaccines.push(submitted);
      }
    });

  }

  addVaccine() {
    const dialogRef = this.dialog.open(AplicarVacuna2Component, { // maybe need change
      disableClose: true,
      width: '45%',
      data: {
        patientId: this.data.patientId
      }
    });

    dialogRef.afterClosed().subscribe(submitted => {
      if (submitted) {
        this.appliedVaccines.push(submitted);
      }
    });
  }
}