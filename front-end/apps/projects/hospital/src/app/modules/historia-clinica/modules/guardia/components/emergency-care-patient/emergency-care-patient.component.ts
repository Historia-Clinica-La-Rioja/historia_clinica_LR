import { Component, EventEmitter, Input, OnDestroy, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SearchPatientDialogComponent } from '@pacientes/dialogs/search-patient-dialog/search-patient-dialog.component';
import { PatientSummary } from 'projects/hospital/src/app/modules/hsi-components/patient-summary/patient-summary.component';
import { BasicPatientDto, PatientMedicalCoverageDto, PersonPhotoDto } from '@api-rest/api-model';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '@core/services/mapper.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MedicalCoverageComponent } from '@pacientes/dialogs/medical-coverage/medical-coverage.component';
import { Observable, Subscription, forkJoin } from 'rxjs';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { ButtonType } from '@presentation/components/button/button.component';
import { PatientType } from '@historia-clinica/constants/summaries';
import { EmergencyCareTemporaryPatientService } from '../../services/emergency-care-temporary-patient.service';

@Component({
	selector: 'app-emergency-care-patient',
	templateUrl: './emergency-care-patient.component.html',
	styleUrls: ['./emergency-care-patient.component.scss']
})
export class EmergencyCarePatientComponent implements OnDestroy {

	private selectedPatient: SelectedPatient;
	private _emergencyCarePatientData: EmergencyCarePatient;
	private patientDescriptionSubscription: Subscription;
	readonly BUTTON_TYPE_ICON = ButtonType.ICON;
	patientSummary: PatientSummary;
	patientMedicalCoverages: PatientMedicalCoverageDto[] = [];
	hasToShowButtonsActions = true;
	isAnEmergencyCareTemporaryPatient = false;
	form = this.buildForm();

	@Input() set emergencyCarePatientData(emergencyCarePatientData: EmergencyCarePatient) {
		this._emergencyCarePatientData = emergencyCarePatientData;
		if (emergencyCarePatientData)
			this.setPatientData();
	};

	@Output() selectedPatientData = new EventEmitter<EmergencyCarePatient>;

	constructor(
		private readonly dialog: MatDialog,
		private readonly patientService: PatientService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly mapperService: MapperService,
		private readonly snackBarService: SnackBarService,
		private readonly patientNameService: PatientNameService,
		readonly emergencyCareTemporaryPatientService: EmergencyCareTemporaryPatientService,
	) {
		this.form.valueChanges.subscribe(formValues => {
			const { patientId, patientMedicalCoverageId, patientDescription } = formValues;
			const emergencyCareData: EmergencyCarePatient = { patientId, patientMedicalCoverageId, patientDescription };
			this.selectedPatientData.emit(emergencyCareData);
		});

		this.emergencyCareTemporaryPatientService.patientDescription$.subscribe(patientDescription => {
			this.form.controls.patientDescription.setValue(patientDescription);
			this.isAnEmergencyCareTemporaryPatient = !!patientDescription;
			this.hasToShowButtonsActions = !patientDescription;
		})
	}

	ngOnDestroy(): void {
		this.patientDescriptionSubscription?.unsubscribe();
	}

	searchPatient() {
		const dialogRef = this.dialog.open(SearchPatientDialogComponent);

		dialogRef.afterClosed()
			.subscribe((foundPatient: any) => {
				if (foundPatient && foundPatient != -1) {
					this.hasToShowButtonsActions = false;
					this.setPatientAndMedicalCoverages(foundPatient.basicData, foundPatient.photo);
					this.isAnEmergencyCareTemporaryPatient = false;
				}
			});
	}

	openMedicalCoverageDialog() {
		const { genderId, identificationNumber, identificationTypeId, patientId } = this.selectedPatient;
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId,
				identificationNumber,
				identificationTypeId,
				initValues: this.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverage(s)),
				patientId
			}
		});

		dialogRef.afterClosed().subscribe(values => {
			if (values) {
				const patientCoverages = values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

				this.patientMedicalCoverageService.addPatientMedicalCoverages(this.selectedPatient.patientId, patientCoverages).subscribe(
					_ => {
						this.snackBarService.showSuccess('Las coberturas fueron actualizadas correctamente');
						this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.selectedPatient.patientId).subscribe(updatedCoverages => {
							this.patientMedicalCoverages = updatedCoverages;
						});
					},
					_ => this.snackBarService.showError('Ocurrió un error al actualizar las coberturas')
				);
			}
		});
	}

	private setPatientAndMedicalCoverages(basicData: BasicPatientDto, photo: PersonPhotoDto) {
		this.form.controls.patientId.setValue(basicData.id);
		this.patientSummary = basicData.person ? this.toPatientSummary(basicData, photo) : null;
		this.setSelectedPatient(basicData);
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(basicData.id).subscribe(coverages => {
			this.patientMedicalCoverages = coverages;
		});
	}

	private loadPatient(patientId: number) {
		const patientBasicData$: Observable<BasicPatientDto> = this.patientService.getPatientBasicData(patientId);
		const patientPhoto$: Observable<PersonPhotoDto> = this.patientService.getPatientPhoto(patientId);
		forkJoin([patientBasicData$, patientPhoto$]).subscribe(([patientBasicData, patientPhoto]) => this.setPatientAndMedicalCoverages(patientBasicData, patientPhoto));
	}

	clearSelectedPatient() {
		this.patientSummary = null;
		this.selectedPatient = null;
		this.hasToShowButtonsActions = true;
		this.form.controls.patientId.setValue(null);
		this.form.controls.patientMedicalCoverageId.setValue(null);
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

	setSelectedTemporaryPatientData(patientDescription: string) {
		this.form.controls.patientDescription.setValue(patientDescription);

		if (!patientDescription) {
			this.hasToShowButtonsActions = true;
			this.isAnEmergencyCareTemporaryPatient = false;
		}
	}

	private buildForm(): FormGroup<EmergencyCarePatientForm> {
		return new FormGroup<EmergencyCarePatientForm>({
			patientId: new FormControl(null),
			patientMedicalCoverageId: new FormControl(null),
			patientDescription: new FormControl(null)
		})
	}

	private setSelectedPatient(basicData: BasicPatientDto) {
		this.selectedPatient = {
			patientId: basicData.id,
			genderId: basicData.person?.gender.id,
			identificationNumber: basicData.person?.identificationNumber,
			identificationTypeId: basicData.person?.identificationTypeId,
		};
	}

	private toPatientSummary(basicData: BasicPatientDto, photo: PersonPhotoDto): PatientSummary {
		const { firstName, nameSelfDetermination, lastName, middleNames, otherLastNames } = basicData.person;
		return {
			fullName: this.patientNameService.completeName(firstName, nameSelfDetermination, lastName, middleNames, otherLastNames),
			...(basicData.identificationType && {
				identification: {
					type: basicData.identificationType,
					number: +basicData.identificationNumber
				}
			}),
			id: basicData.id,
			gender: basicData.person.gender?.description || null,
			age: basicData.person.age || null,
			photo: photo.imageData
		}
	}


	private setPatientData() {
		this.preloadedFormData();
		const patientId = this._emergencyCarePatientData.patientId;
		this.isAnEmergencyCareTemporaryPatient = !!this._emergencyCarePatientData.patientDescription && (!patientId || this._emergencyCarePatientData.patientTypeId === PatientType.EMERGENCY_CARE_TEMPORARY);
		this.hasToShowButtonsActions = false;
		if (!this.isAnEmergencyCareTemporaryPatient && patientId)
			this.loadPatient(patientId);
	}

	private preloadedFormData() {
		Object.keys(this.form.controls).forEach(
			controlKey => {
				const keyValue = this._emergencyCarePatientData[controlKey]
				if(keyValue){
					this.form.controls[controlKey].setValue(keyValue);
				}
			}
		)
	}

}

export interface EmergencyCarePatient {
	patientId?: number;
	patientMedicalCoverageId?: number;
	patientDescription?: string;
	patientTypeId?: number;
}

interface EmergencyCarePatientForm {
	patientId: FormControl<number>;
	patientMedicalCoverageId: FormControl<number>;
	patientDescription: FormControl<string>;
}

interface SelectedPatient {
	patientId: number,
	genderId: number,
	identificationNumber: string,
	identificationTypeId: number,
}
