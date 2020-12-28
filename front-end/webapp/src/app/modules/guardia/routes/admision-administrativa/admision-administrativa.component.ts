import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { BasicPatientDto, MasterDataInterface, PatientMedicalCoverageDto, PersonPhotoDto } from '@api-rest/api-model';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { MedicalCoverageComponent, PatientMedicalCoverage } from '@core/dialogs/medical-coverage/medical-coverage.component';
import { MapperService } from '@core/services/mapper.service';
import { MapperService as PatientMapperService } from '@presentation/services/mapper.service';
import { hasError, TIME_PATTERN } from '@core/utils/form.utils';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { MotivoNuevaConsultaService } from 'src/app/modules/historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from 'src/app/modules/historia-clinica/services/snomed.service';
import { Patient, SearchPatientComponent } from 'src/app/modules/pacientes/component/search-patient/search-patient.component';
import { AdministrativeAdmissionDto, NewEpisodeService } from '../../services/new-episode.service';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { PatientService } from "@api-rest/services/patient.service";

@Component({
	selector: 'app-admision-administrativa',
	templateUrl: './admision-administrativa.component.html',
	styleUrls: ['./admision-administrativa.component.scss']
})
export class AdmisionAdministrativaComponent implements OnInit {

	hasError = hasError;
	TIME_PATTERN = TIME_PATTERN;
	patientCardInfo: {
		photo: PersonPhotoDto,
		basicData: PatientBasicData
	}

	patientMedicalCoverages: PatientMedicalCoverageDto[];
	emergencyCareEntranceType$: Observable<MasterDataInterface<number>[]>;
	emergencyCareType$: Observable<MasterDataInterface<number>[]>;
	hasPoliceIntervention = false;
	form: FormGroup;
	today: Date = new Date();

	motivoNuevaConsultaService: MotivoNuevaConsultaService; // sacarlo de historia clinica
	readonly WITH_DOCTOR_IN_AMBULANCE = 3;
	readonly WITHOUT_DOCTOR_IN_AMBULANCE = 4;
	private readonly routePrefix;
	private selectedPatient;

	constructor(
		private readonly dialog: MatDialog,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly emergencyCareMasterData: EmergencyCareMasterDataService,
		private formBuilder: FormBuilder,
		private readonly mapperService: MapperService,
		private readonly patientMapperService: PatientMapperService,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly newEpisodeService: NewEpisodeService,
		private router: Router,
		private readonly contextService: ContextService,
		private readonly patientService: PatientService

	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, snomedService);
		this.routePrefix = `institucion/${this.contextService.institutionId}/`
	}

	ngOnInit(): void {

		let administrativeAdmissionDto = this.newEpisodeService.getAdministrativeAdmission();
		if(administrativeAdmissionDto?.patient?.id) {
			this.loadPatient(administrativeAdmissionDto.patient.id);
		}
		this.form = this.formBuilder.group({
			patientMedicalCoverageId: [administrativeAdmissionDto?.patient.patientMedicalCoverageId],
			emergencyCareTypeId: [administrativeAdmissionDto?.typeId],
			emergencyCareEntranceTypeId: [administrativeAdmissionDto?.entranceTypeId],
			ambulanceCompanyId: [administrativeAdmissionDto?.ambulanceCompanyId],
			dateCall: [administrativeAdmissionDto?.policeIntervention?.dateCall],
			timeCall: [administrativeAdmissionDto?.policeIntervention?.timeCall],
			plateNumber: [administrativeAdmissionDto?.policeIntervention?.plateNumber],
			firstName: [administrativeAdmissionDto?.policeIntervention?.firstName],
			lastName: [administrativeAdmissionDto?.policeIntervention?.lastName],

		});
		if(administrativeAdmissionDto?.policeIntervention){
			this.hasPoliceIntervention = true;
		}
		this.emergencyCareType$ = this.emergencyCareMasterData.getType();
		this.emergencyCareEntranceType$ = this.emergencyCareMasterData.getEntranceType();
	}

	loadPatient(patientId: number): void {
		this.patientService.getPatientBasicData(patientId).subscribe((basicData: BasicPatientDto) => {
			this.patientService.getPatientPhoto(patientId).subscribe((photo: PersonPhotoDto) => {
				this.setPatientAndMedicalCoverages(basicData, photo);
			})
		});

	}

	setPatientAndMedicalCoverages(basicData: BasicPatientDto, photo: PersonPhotoDto): void {
		this.patientCardInfo = {
			basicData: this.patientMapperService.toPatientBasicData(basicData),
			photo: photo
		};
		this.selectedPatient = {
			id: basicData.id,
			genderId: basicData.person.gender.id,
			identificationNumber: basicData.person.identificationNumber,
			identificationTypeId: basicData.person.identificationTypeId,
			initValues: null
		};
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(basicData.id).subscribe(coverages => {
			this.patientMedicalCoverages = coverages;
		});
	}

	searchPatient(): void {
		const dialogRef = this.dialog.open(SearchPatientComponent);

		dialogRef.afterClosed()
			.subscribe((data: Patient) => {
				this.setPatientAndMedicalCoverages(data.basicData, data.photo);
			});

	}

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.selectedPatient.genderId,
				identificationNumber: this.selectedPatient.identificationNumber,
				identificationTypeId: this.selectedPatient.identificationTypeId,
				initValues: this.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverage(s)),
			}
		});

		dialogRef.afterClosed().subscribe(values => {
			if (values) {
				const patientCoverages: PatientMedicalCoverageDto[] =
					values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

				this.patientMedicalCoverageService.addPatientMedicalCoverages(this.selectedPatient.id, patientCoverages).subscribe(_ => {
					this.snackBarService.showSuccess('Las coberturas fueron actualizadas correctamente');
					this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.selectedPatient.id).subscribe(updatedCoverages => {
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
		const policeIntervention = this.hasPoliceIntervention ? toPoliceIntervention() : null;
		const administrativeAdmisionDto: AdministrativeAdmissionDto = {
			patient: {
				id: this.selectedPatient?.id,
				patientMedicalCoverageId: formValue.patientMedicalCoverageId,
			},
			reasons: this.motivoNuevaConsultaService.getMotivosConsulta().map(s => s.snomed),
			typeId: formValue.emergencyCareTypeId,
			entranceTypeId: formValue.emergencyCareEntranceTypeId,
			ambulanceCompanyId: formValue.ambulanceCompanyId,
			policeIntervention,
		}
		this.goToBasicTriage(administrativeAdmisionDto);

		function toPoliceIntervention() {
			return {
				dateCall: formValue.dateCall ? momentFormat(formValue.dateCall, DateFormat.API_DATE) : null,
				timeCall: formValue.timeCall,
				plateNumber: formValue.plateNumber,
				firstName: formValue.firstName,
				lastName: formValue.lastName
			};
		}
	}

	onChange(mrChange): void {
		this.hasPoliceIntervention = eval(mrChange.value);
		if (!this.hasPoliceIntervention) {
			this.form.controls.dateCall.setValue(null);
			this.form.controls.timeCall.setValue(null);
			this.form.controls.plateNumber.setValue(null);
			this.form.controls.firstName.setValue(null);
			this.form.controls.lastName.setValue(null);
		}
	}

	goBack(): void {
		this.newEpisodeService.destroy();
		const url = `${this.routePrefix}guardia`;
		this.router.navigateByUrl(url);
	}

	setAmbulanceCompanyIdStatus(): void {
		if (this.form.value.emergencyCareEntranceTypeId === this.WITH_DOCTOR_IN_AMBULANCE || this.form.value.emergencyCareEntranceTypeId === this.WITHOUT_DOCTOR_IN_AMBULANCE) {
			this.form.controls.ambulanceCompanyId.enable();
		}
		else {
			this.form.controls.ambulanceCompanyId.disable();
		}
	}

	goToBasicTriage(administrativeAdmisionDto: AdministrativeAdmissionDto): void {
		this.newEpisodeService.setAdministrativeAdmission(administrativeAdmisionDto);
		const url = `${this.routePrefix}guardia/nuevo-episodio/triage-administrativo`;
		this.router.navigateByUrl(url);
	}
}
