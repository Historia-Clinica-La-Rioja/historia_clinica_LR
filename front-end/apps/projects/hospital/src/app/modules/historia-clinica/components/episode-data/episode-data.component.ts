import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, ClinicalSpecialtyDto, ExternalCoverageDto, PatientMedicalCoverageDto, ReducedPatientDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
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

	form: FormGroup;
	specialties: ClinicalSpecialtyDto[] = [];
	patientMedicalCoverages: PatientMedicalCoverage[] = [];
	hierarchicalUnitId: number;
	_patientId: number;
	isEnableJointSignature= false;
	involvedHealthcareProfessionalIds: number[] = [];

	private patient: ReducedPatientDto;
	private appointmentConfirmedCoverageInfo: ExternalCoverageDto;

	@Input()
	set patientId(id: number) {
		if (id) {
			this.setPatientInfo(id);
			this.setMedicalCoverages();
		}
	}
	@Input() showSpecialty = true;
	@Output() episodeData = new EventEmitter<EpisodeData>();

	constructor(
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly dialog: MatDialog,
		private readonly formBuilder: FormBuilder,
		private readonly mapperService: MapperService,
		private readonly snackBarService: SnackBarService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly patientService: PatientService,
		private readonly medicalCoverageInfoService: MedicalCoverageInfoService,
		private readonly featureFlagService: FeatureFlagService,
	) { }

	ngOnInit() {
		this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_CONJUNTA).subscribe(isEnabled => this.isEnableJointSignature = isEnabled);
		this.form = this.formBuilder.group({
			clinicalSpecialty: [null, [Validators.required]],
			patientMedicalCoverage: [null],
		});

		if (this.showSpecialty)
			this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
				this.setSpecialtyFields(specialties);
			});

		this.appointmentConfirmedCoverageInfo = this.medicalCoverageInfoService.appointmentConfirmedCoverageInfo?.medicalCoverage;
	}

	openMedicalCoverageDialog() {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.patient.personalDataDto.genderId,
				identificationNumber: this.patient.personalDataDto.identificationNumber,
				identificationTypeId: this.patient.personalDataDto.identificationTypeId,
				initValues: this.patientMedicalCoverages,
				patientId: this._patientId
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {

				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));
					this.patientMedicalCoverageService.addPatientMedicalCoverages(this._patientId, patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess('ambulatoria.paciente.nueva-consulta.episode-data.UPDATE_CONVERGE_CORRECTLY');
						},
						_ => this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.episode-data.ERROR_CONVERGE_CORRECTLY')
					);
				}
			}
		);
	}

	setEpisodeData() {
		this.episodeData.emit(this.getEpisodeData());
	}

	setHUId(id:number){
		this.hierarchicalUnitId = id;
		this.setEpisodeData();
	}

	setInvolvedHealthcareProfessionals(event){
		this.involvedHealthcareProfessionalIds = event;
		this.setEpisodeData();
	}

	private setSpecialtyFields(specialties: ClinicalSpecialtyDto[]) {
		this.specialties = specialties;
		const defaultSpecialty = specialties[0];
		this.form.get('clinicalSpecialty').patchValue(defaultSpecialty);
		this.form.get('clinicalSpecialty').markAsTouched();
		this.setEpisodeData();
	}

	private setCoverage(p: PatientMedicalCoverage) {
		this.form.get('patientMedicalCoverage').patchValue(p);
		this.form.get('patientMedicalCoverage').disable();
		this.form.get('patientMedicalCoverage').markAsTouched();
		this.setEpisodeData();
	}

	private setMedicalCoverages() {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this._patientId)
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => {
				this.patientMedicalCoverages = patientMedicalCoverages;
				const appointmentMedicalCoverage = this.patientMedicalCoverages.find(p => p.medicalCoverage.id === this.appointmentConfirmedCoverageInfo?.id);
				if (appointmentMedicalCoverage) 
					this.setCoverage(appointmentMedicalCoverage);
			});
	}

	private setPatientInfo(id: number) {
		this._patientId = id;
		this.patientService.getBasicPersonalData(id).subscribe((reducedPatientDto: ReducedPatientDto) => this.patient = reducedPatientDto);
	}

	private getEpisodeData(): EpisodeData {
		return {
			medicalCoverageId: this.form.value.patientMedicalCoverage?.id,
			clinicalSpecialtyId: this.form.value.clinicalSpecialty?.id,
			hierarchicalUnitId: this.hierarchicalUnitId,
			involvedHealthcareProfessionalIds: this.involvedHealthcareProfessionalIds,
		}
	}
}

export interface EpisodeData {
	medicalCoverageId: number;
	clinicalSpecialtyId: number;
	hierarchicalUnitId: number;
	involvedHealthcareProfessionalIds: number[];
}
