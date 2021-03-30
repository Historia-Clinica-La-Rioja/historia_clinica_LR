import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import {
	BasicPatientDto, DoctorsOfficeDto,
	MasterDataInterface,
	PatientMedicalCoverageDto,
	PersonPhotoDto,
} from '@api-rest/api-model';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { MedicalCoverageComponent } from '@core/dialogs/medical-coverage/medical-coverage.component';
import { MapperService } from '@core/services/mapper.service';
import { MapperService as PatientMapperService } from '@presentation/services/mapper.service';
import { hasError, TIME_PATTERN } from '@core/utils/form.utils';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { MotivoNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { Patient, SearchPatientComponent } from '@pacientes/component/search-patient/search-patient.component';
import { AdministrativeAdmission, NewEpisodeService } from '../../services/new-episode.service';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { PatientService } from '@api-rest/services/patient.service';
import { AMBULANCE, PERSON, POLICE_OFFICER } from '@core/constants/validation-constants';
import { EmergencyCareEntranceType } from '@api-rest/masterdata';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';
import {DoctorsOfficeService} from '@api-rest/services/doctors-office.service';
import {SECTOR_AMBULATORIO} from '../../constants/masterdata';

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
	};

	readonly POLICE_OFFICER = POLICE_OFFICER;
	readonly PERSON = PERSON;
	readonly AMBULANCE = AMBULANCE;

	patientMedicalCoverages: PatientMedicalCoverageDto[];
	emergencyCareEntranceType$: Observable<MasterDataInterface<number>[]>;
	emergencyCareType$: Observable<MasterDataInterface<number>[]>;
	form: FormGroup;
	today: Date = new Date();

	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	readonly EMERGENCY_CARE_ENTRANCE_TYPE = EmergencyCareEntranceType;

	doctorsOffices$: Observable<DoctorsOfficeDto[]>;

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
		private readonly patientService: PatientService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly doctorsOfficeService: DoctorsOfficeService
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, this.snomedService);
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
	}

	ngOnInit(): void {

		this.emergencyCareType$ = this.emergencyCareMasterData.getType();
		this.emergencyCareEntranceType$ = this.emergencyCareMasterData.getEntranceType();
		this.doctorsOffices$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);

		this.form = this.formBuilder.group({
			patientMedicalCoverageId: [null],
			emergencyCareTypeId: [null],
			emergencyCareEntranceTypeId: [null],
			doctorsOfficeId: [null],
			ambulanceCompanyId: [null, Validators.maxLength(AMBULANCE.COMPANY_ID.max_length)],
			hasPoliceIntervention: [null],
			callDate: [null],
			callTime: [null],
			plateNumber: [null, Validators.maxLength(POLICE_OFFICER.PLATE_NUMBER.max_length)],
			firstName: [null, Validators.maxLength(PERSON.MAX_LENGTH.firstName)],
			lastName: [null, Validators.maxLength(PERSON.MAX_LENGTH.firstName)],
			reasons: [null],
			patientId: [null]
		});

		if (window.history.state.commingBack) {

			const administrativeAdmission: AdministrativeAdmission = this.newEpisodeService.getAdministrativeAdmission();
			if (!administrativeAdmission) {
				return;
			}
			this.form.setValue(administrativeAdmission);

			if (administrativeAdmission?.patientId) {
				this.loadPatient(administrativeAdmission.patientId);
			}

			this.form.value.reasons.forEach(reason => this.motivoNuevaConsultaService.add(reason));

		} else {
			this.newEpisodeService.destroy();
		}

	}

	searchPatient(): void {
		const dialogRef = this.dialog.open(SearchPatientComponent);

		dialogRef.afterClosed()
			.subscribe((foundPatient: Patient) => {
				if (foundPatient) {
					this.setPatientAndMedicalCoverages(foundPatient.basicData, foundPatient.photo);
				}
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

				this.patientMedicalCoverageService.addPatientMedicalCoverages(this.selectedPatient.id, patientCoverages).subscribe(
					_ => {
						this.snackBarService.showSuccess('Las coberturas fueron actualizadas correctamente');
						this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.selectedPatient.id).subscribe(updatedCoverages => {
							this.patientMedicalCoverages = updatedCoverages;
						});
					},
					_ => this.snackBarService.showError('Ocurrió un error al actualizar las coberturas')
				);
			}
		});
	}

	clearSelectedPatient(): void {
		this.selectedPatient = null;
		this.patientCardInfo = null;
		this.form.controls.patientId.setValue(null);
		this.form.controls.patientMedicalCoverageId.setValue(null);
	}

	getFullMedicalCoverageText(patientMedicalCoverage): string {
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name]
			.filter(Boolean).join(' - ');
		return [medicalCoverageText, patientMedicalCoverage.affiliateNumber].filter(Boolean).join(' / ');
	}

	continue(): void {
		this.form.controls.reasons.setValue(this.motivoNuevaConsultaService.getMotivosConsulta());

		const formValue: AdministrativeAdmission = this.form.getRawValue();
		if (this.form.valid) {
			this.goToTriage(formValue);
		}

	}

	onChange(): void {
		if (!this.form.controls.hasPoliceIntervention.value) {
			this.form.controls.callDate.setValue(null);
			this.form.controls.callTime.setValue(null);
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
		if (this.form.value.emergencyCareEntranceTypeId !== EmergencyCareEntranceType.AMBULANCIA_CON_MEDICO
			|| this.form.value.emergencyCareEntranceTypeId !== EmergencyCareEntranceType.AMBULANCIA_SIN_MEDICO) {
				this.form.controls.ambulanceCompanyId.setValue(null);
		}
	}

	goToTriage(administrativeAdmission: AdministrativeAdmission): void {
		this.newEpisodeService.setAdministrativeAdmission(administrativeAdmission);
		this.triageDefinitionsService.getTriagePath(administrativeAdmission?.emergencyCareTypeId)
			.subscribe(({ url }) => this.router.navigateByUrl(url));
	}

	private setPatientAndMedicalCoverages(basicData: BasicPatientDto, photo: PersonPhotoDto): void {

		this.form.controls.patientId.setValue(basicData.id);
		this.patientCardInfo = {
			basicData: this.patientMapperService.toPatientBasicData(basicData),
			photo
		};
		this.selectedPatient = {
			id: basicData.id,
			genderId: basicData.person.gender.id,
			identificationNumber: basicData.person.identificationNumber,
			identificationTypeId: basicData.person.identificationTypeId,
		};
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(basicData.id).subscribe(coverages => {
			this.patientMedicalCoverages = coverages;
		});
	}

	private loadPatient(patientId: number): void {
		this.patientService.getPatientBasicData(patientId).subscribe((basicData: BasicPatientDto) => {
			this.patientService.getPatientPhoto(patientId).subscribe((photo: PersonPhotoDto) => {
				this.setPatientAndMedicalCoverages(basicData, photo);
			});
		});

	}
}
