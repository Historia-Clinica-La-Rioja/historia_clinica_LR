import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ClinicalSpecialtyDto, ImmunizationDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { AplicarVacuna2Component } from '../aplicar-vacuna-2/aplicar-vacuna-2.component';

@Component({
  selector: 'app-agregar-vacunas',
  templateUrl: './agregar-vacunas.component.html',
  styleUrls: ['./agregar-vacunas.component.scss']
})
export class AgregarVacunasComponent implements OnInit {

  form: FormGroup;
  fixedSpecialty = true;
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
      clinicalSpecialty: [null, Validators.required]
    });

    this.setLoggedProfessionalSpecialties();
  }

  public setLoggedProfessionalSpecialties(): void {
    this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
      this.specialties = specialties;
      this.fixedSpecialty = false;
      this.form.get('clinicalSpecialty').setValue(0);
    });
  }

  public displayDate(administrationDate: string): string {
    return momentFormat(momentParseDate(administrationDate), DateFormat.VIEW_DATE);
  }

  public remove(vaccineIndex: number): void {
    this.appliedVaccines.splice(vaccineIndex, 1);
  }

  public edit(vaccineIndex: number): void {
    const dialogRef = this.dialog.open(AplicarVacuna2Component, {
      disableClose: true,
      width: '45%',
      data: {
        immunization: this.appliedVaccines[vaccineIndex]
      }
    });

    dialogRef.afterClosed().subscribe(submitted => {
      if (submitted) {
        this.remove(vaccineIndex);
        this.appliedVaccines.push(submitted);
      }
    });

  }

  public addVaccine(): void {
    const dialogRef = this.dialog.open(AplicarVacuna2Component, {
      disableClose: true,
      width: '45%'
    });

    dialogRef.afterClosed().subscribe(submitted => {
      if (submitted) {
        this.appliedVaccines.push(submitted);
      }
    });
  }
}