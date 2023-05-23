import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ClinicalSpecialtyDto, ExternalCoverageDto, PatientMedicalCoverageDto, ReducedPatientDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '@core/services/mapper.service';
import { MedicalCoverageInfoService } from '@historia-clinica/modules/ambulatoria/services/medical-coverage-info.service';
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { map } from 'rxjs';

@Component({
	selector: 'app-episode-data',
	templateUrl: './episode-data.component.html',
	styleUrls: ['./episode-data.component.scss']
})
export class EpisodeDataComponent implements OnInit {
	UPDATE_CONVERGE_CORRECTLY = 'ambulatoria.paciente.nueva-consulta.episode-data.UPDATE_CONVERGE_CORRECTLY';
	ERROR_CONVERGE_CORRECTLY = 'ambulatoria.paciente.nueva-consulta.episode-data.ERROR_CONVERGE_CORRECTLY';
	fixedSpecialty = true;
	form: FormGroup;
	defaultSpecialty: ClinicalSpecialtyDto;
	specialties: ClinicalSpecialtyDto[] = [];
	patientMedicalCoverages: PatientMedicalCoverage[] = [];
	patient: ReducedPatientDto;
	patientId: number;
	disabledCoverage = false;
	appointmentConfirmedCoverageInfo: ExternalCoverageDto;
	@Input() idPatient: number;
	@Output() medicalCoverage = new EventEmitter<PatientMedicalCoverage>();
	@Output() clinicalSpecialty = new EventEmitter<ClinicalSpecialtyDto>();

	constructor(
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly dialog: MatDialog,
		private readonly formBuilder: FormBuilder,
		private readonly mapperService: MapperService,
		private readonly snackBarService: SnackBarService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly patientService: PatientService,
		private readonly medicalCoverageInfoService: MedicalCoverageInfoService,

	) {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
			this.setSpecialtyFields(specialties, false);
		});

	}

	ngOnInit(): void {
		this.patientId = this.idPatient;
		this.patientService.getBasicPersonalData(this.idPatient)
			.subscribe((reducedPatientDto: ReducedPatientDto) => {
				this.patient = reducedPatientDto;
			})
		this.form = this.formBuilder.group({
			clinicalSpecialty: [null, [Validators.required]],
			patientMedicalCoverage: [null],
		});
		this.appointmentConfirmedCoverageInfo = this.medicalCoverageInfoService?.appointmentConfirmedCoverageInfo?.medicalCoverage;
		this.setMedicalCoverages();
	}

	setDefaultSpecialty() {
		this.clinicalSpecialty.emit(this.form.controls.clinicalSpecialty.value);
	}

	setMedicalCoverage() {
		this.medicalCoverage.emit(this.form.controls.patientMedicalCoverage.value);
	}

	openMedicalCoverageDialog() {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.patient.personalDataDto.genderId,
				identificationNumber: this.patient.personalDataDto.identificationNumber,
				identificationTypeId: this.patient.personalDataDto.identificationTypeId,
				initValues: this.patientMedicalCoverages,
				patientId: this.idPatient
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {

				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));
					this.patientMedicalCoverageService.addPatientMedicalCoverages(this.idPatient, patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess(this.UPDATE_CONVERGE_CORRECTLY);
						},
						_ => this.snackBarService.showError(this.ERROR_CONVERGE_CORRECTLY)
					);
				}
			}
		);
	}

	private setSpecialtyFields(specialtyArray, fixedSpecialty) {
		this.specialties = specialtyArray;
		this.fixedSpecialty = fixedSpecialty;
		this.defaultSpecialty = specialtyArray[0];
		this.form.get('clinicalSpecialty').patchValue(this.defaultSpecialty);
		this.form.get('clinicalSpecialty').markAsTouched();
		this.setDefaultSpecialty();

	}

	private setCoverage(p: PatientMedicalCoverage) {
		this.form.get('patientMedicalCoverage').patchValue(p);
		this.form.get('patientMedicalCoverage').markAsTouched();
		//this.disabledCoverage = true;
		this.setMedicalCoverage();
	}

	private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.idPatient)
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => {
				this.patientMedicalCoverages = patientMedicalCoverages;
				this.patientMedicalCoverages.find(p => {
					if (p?.medicalCoverage?.id === this.appointmentConfirmedCoverageInfo?.id) {
						this.disabledCoverage = true;
						this.setCoverage(p);
					}
				})

			});
	}
}
