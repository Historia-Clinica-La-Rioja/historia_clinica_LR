import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ClinicalSpecialtyDto, ImmunizationDto, ImmunizePatientDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ImmunizationService } from '@api-rest/services/immunization.service';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { AgregarVacunaComponent } from '../agregar-vacuna/agregar-vacuna.component';
import { AppointmentsService } from "@api-rest/services/appointments.service";
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
  selector: 'app-agregar-vacunas',
  templateUrl: './agregar-vacunas.component.html',
  styleUrls: ['./agregar-vacunas.component.scss']
})
export class AgregarVacunasComponent implements OnInit {
  hasConfirmedAppointment: boolean;
  form: FormGroup;
  specialties: ClinicalSpecialtyDto[];
  appliedVaccines: ImmunizationDto[];
  loading: boolean = false;
  Color = Color;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {
      patientId: number,
    },
    private readonly formBuilder: FormBuilder,
    private readonly dialog: MatDialog,
    private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
    public dialogRef: MatDialogRef<AgregarVacunasComponent>,
    private immunizationService: ImmunizationService,
    private readonly snackBarService: SnackBarService,
    private readonly appointmentsService: AppointmentsService
  ) {
    this.appliedVaccines = [];
  }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      clinicalSpecialty: [{ value: null, disabled: true }, Validators.required]
    });

    this.appointmentsService.hasNewConsultationEnabled(this.data.patientId).subscribe(response => {
      this.hasConfirmedAppointment = response;
    });

    this.setLoggedProfessionalSpecialties();
  }

  public setLoggedProfessionalSpecialties(): void {
    this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(
      (specialties: ClinicalSpecialtyDto[]) => {
        this.specialties = specialties;
        if (this.specialties.length > 0) {
          this.form.get('clinicalSpecialty').setValue(specialties[0].id);
          this.form.get('clinicalSpecialty').enable();
        }
      }
    );
  }

  public displayDate(administrationDate: string): string {
    return momentFormat(momentParseDate(administrationDate), DateFormat.VIEW_DATE);
  }

  public remove(vaccineIndex: number): void {
    this.appliedVaccines.splice(vaccineIndex, 1);
  }

  public edit(vaccineIndex: number): void {
    const dialogRef = this.dialog.open(AgregarVacunaComponent, {
      disableClose: true,
      width: '45%',
      data: {
        immunization: this.appliedVaccines[vaccineIndex],
        edit: true,
        patientId: this.data.patientId,
        hasConfirmedAppointment: this.hasConfirmedAppointment
      },
      autoFocus: false
    });

    dialogRef.afterClosed().subscribe(
      (submitted: ImmunizationDto) => {
        if (submitted)
          this.appliedVaccines[vaccineIndex] = submitted;
      }
    );
  }

  public addVaccine(): void {
    const dialogRef = this.dialog.open(AgregarVacunaComponent, {
      disableClose: true,
      width: '45%',
      data: {
        patientId: this.data.patientId,
        hasConfirmedAppointment: this.hasConfirmedAppointment
      },
      autoFocus: false
    });

    dialogRef.afterClosed().subscribe(
      (submitted: ImmunizationDto) => {
        if (submitted)
          this.appliedVaccines.push(submitted);
      }
    );
  }

  public save(): void {
    if (this.form.valid && (this.appliedVaccines.length > 0)) {
      this.loading = true;

      const immunizePatient: ImmunizePatientDto = {
        clinicalSpecialtyId: this.form.value.clinicalSpecialty,
        immunizations: this.appliedVaccines
      };

      this.immunizationService.immunizePatient(immunizePatient, this.data.patientId).subscribe(
        (success: boolean) => {
          this.loading = false;
          if (success) {
            this.dialogRef.close(true);
            this.snackBarService.showSuccess('ambulatoria.paciente.vacunas.agregar_vacunas.SUCCESS');
            this.appointmentsService.hasNewConsultationEnabled(this.data.patientId).subscribe(response => {
              this.hasConfirmedAppointment = response;
            });
          }
        },
        error => {
          this.loading = false;
          if (error.text)
            this.snackBarService.showError(error.text);
          else
            this.snackBarService.showError('ambulatoria.paciente.vacunas.agregar_vacunas.ERROR');
        }
      );

    }
  }

}
