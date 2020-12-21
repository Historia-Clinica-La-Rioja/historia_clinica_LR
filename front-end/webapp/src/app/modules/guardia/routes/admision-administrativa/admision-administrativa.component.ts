import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { BasicPatientDto, MasterDataInterface, PatientMedicalCoverageDto, PersonPhotoDto } from '@api-rest/api-model';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@core/dialogs/medical-coverage/medical-coverage.component';
import { MapperService } from '@core/services/mapper.service';
import { hasError } from '@core/utils/form.utils';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { MotivoNuevaConsultaService } from 'src/app/modules/historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from 'src/app/modules/historia-clinica/services/snomed.service';
import { Patient, SearchPatientComponent } from 'src/app/modules/pacientes/component/search-patient/search-patient.component';

@Component({
	selector: 'app-admision-administrativa',
	templateUrl: './admision-administrativa.component.html',
	styleUrls: ['./admision-administrativa.component.scss']
})
export class AdmisionAdministrativaComponent implements OnInit {

	hasError = hasError;

	patientCardInfo: {
		photo: PersonPhotoDto,
		basicData: PatientBasicData
	}

	patientMedicalCoverages: PatientMedicalCoverageDto[];
	emergencyCareEntranceType$: Observable<MasterDataInterface<number>[]>;
	emergencyCareType$: Observable<MasterDataInterface<number>[]>;
	hasPoliceIntervention = false;
	form: FormGroup;

	motivoNuevaConsultaService: MotivoNuevaConsultaService; // sacarlo de historia clinica

	private selectedPatient;
	constructor(
		private readonly dialog: MatDialog,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly emergencyCareMasterData: EmergencyCareMasterDataService,
		private formBuilder: FormBuilder,
		private readonly mapperService: MapperService,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,

	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, snomedService);
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			patientMedicalCoverageId: [],
			emergencyCareTypeId: [],
			emergencyCareEntranceTypeId: [],
			ambulanceCompanyId: [],
			dateCall: [],
			timeCall: [],
			plateNumber: [],
			firstName: [],
			lastName: [],

		});

		this.emergencyCareType$ = this.emergencyCareMasterData.getType();
		this.emergencyCareEntranceType$ = this.emergencyCareMasterData.getEntranceType();
	}

	searchPatient(): void {
		const dialogRef = this.dialog.open(SearchPatientComponent);

		dialogRef.afterClosed()
			.subscribe((data: Patient) => {
				this.patientCardInfo = {
					basicData: toPatientBasicData(data.basicData),
					photo: data.photo
				}
				this.selectedPatient = {
					id: data.basicData.id,
					genderId: data.basicData.person.gender.id,
					identificationNumber: data.basicData.person.identificationNumber,
					identificationTypeId: data.basicData.person.identificationTypeId,
					initValues: null
				}
				this.patientMedicalCoverageService.getActivePatientMedicalCoverages(data.basicData.id).subscribe(coverages => {
					this.patientMedicalCoverages = coverages;
				});

				function toPatientBasicData(s: BasicPatientDto): PatientBasicData {
					return {
						id: s.id,
						firstName: s.person.firstName,
						lastName: s.person.lastName,
						age: s.person.age,
						gender: s.person.gender.description,
					}
				}
			});

	}

	 	openMedicalCoverageDialog(): void {
			const dialogRef = this.dialog.open(MedicalCoverageComponent, {
				data: {
					genderId: this.selectedPatient.genderId,
					identificationNumber: this.selectedPatient.identificationNumber,
					identificationTypeId: this.selectedPatient.identificationTypeId,
					initValues: this.patientMedicalCoverages.map( s => this.mapperService.toPatientMedicalCoverage(s)),
				}
			});

			dialogRef.afterClosed().subscribe(values => {
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

					this.patientMedicalCoverageService.addPatientMedicalCoverages(this.selectedPatient.id, patientCoverages).subscribe(_ => {
						this.snackBarService.showSuccess('Las coberturas fueron actualizadas correctamente');
						this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.selectedPatient.id).subscribe( updatedCoverages => {
							this.patientMedicalCoverages = updatedCoverages;
						});
					}), _ => this.snackBarService.showError('Ocurrió un error al actualizar las coberturas');
				}
			});
		}

	clearSelectedPatient(): void {
		this.patientCardInfo = null;
		this.form.controls.patientMedicalCoverageId.setValue(null);
	}

	getFullMedicalCoverageText(patientMedicalCoverage): string {
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name]
			.filter(Boolean).join(' - ');
		return [medicalCoverageText, patientMedicalCoverage.affiliateNumber].filter(Boolean).join(' / ');
	}

	continue(): void {
		const formValue = this.form.value;
		const administrative = {
			patient: {
				id: this.selectedPatient?.id,
				patientMedicalCoverageId: formValue.patientMedicalCoverageId,
			},
			reasons: this.motivoNuevaConsultaService.getMotivosConsulta(),
			typeId: formValue.emergencyCareTypeId,
			entranceTypeId: formValue.emergencyCareEntranceTypeId,
			ambulanceCompanyId: formValue.ambulanceCompanyId,
			policeIntervention: {
				dateCall: formValue.dateCall ? momentFormat(formValue.dateCall, DateFormat.API_DATE) : null,
				timeCall: formValue.timeCall,
				plateNumber: formValue.plateNumber,
				firstName: formValue.firstName,
				lastName: formValue.lastName
			}
		}

		console.log(administrative);
	}

	onChange(mrChange): void {
		this.hasPoliceIntervention = eval(mrChange.value);
	}


	skip(): void {

	}

	goBack(): void {

	}
}
