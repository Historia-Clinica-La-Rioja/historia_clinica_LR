import { Component, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DiagnosesGeneralStateDto, ERole, ResponseEmergencyCareDto, SnomedDto, TimeDto, AMedicalDischargeDto, ApiErrorMessageDto } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DischargeTypes } from '@api-rest/masterdata';
import { EmergencyCareEpisodeMedicalDischargeService } from '@api-rest/services/emergency-care-episode-medical-discharge.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { ContextService } from '@core/services/context.service';
import { hasError, beforeTimeValidationDate, futureTimeValidationDate} from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { MIN_DATE } from "@core/utils/date.utils";
import { isSameDay } from 'date-fns';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';
import { EmergencyCareStateService } from '@api-rest/services/emergency-care-state.service';
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';
import { PermissionsService } from '@core/services/permissions.service';
import { MedicalDischargeByNurse } from '../../components/medical-discharge-by-nurse/medical-discharge-by-nurse.component';
import { PatientSummaryDataService } from '../../services/patient-summary-data.service';

const HEALTCARE_PROFESSIONALS = [ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD];

@Component({
	selector: 'app-medical-discharge',
	templateUrl: './medical-discharge.component.html',
	styleUrls: ['./medical-discharge.component.scss']
})
export class MedicalDischargeComponent implements OnInit {

	hasError = hasError;

	patientSummary: PatientSummary;

	form: UntypedFormGroup;

	selectedProblems: Map<number, DiagnosesGeneralStateDto> = new Map();
	problems$: Observable<DiagnosesGeneralStateDto[]>;
	today = new Date();
	episodeCreatedOn: Date;
	formSubmited = false;
	isLoading = false;
	private episodeId: number;
	private patientId: number;

	timePickerData = this.buildTimePickerData();
	minDate = MIN_DATE;
	isNurse = false;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly patientSummaryDataService: PatientSummaryDataService,
		private readonly contextService: ContextService,
		private readonly emergencyCareEspisodeDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly emergencyCareStateService : EmergencyCareStateService,
		private readonly snackBarService: SnackBarService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly permissionService: PermissionsService,
	) {}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			dateTime: this.formBuilder.group({
				date: [new Date(), Validators.required],
				time: [this.timePickerData.defaultTime],
			}),
			autopsy: [null],
			dischargeTypeId: [DischargeTypes.ALTA_MEDICA, Validators.required],
			otherDischargeDescription: [null],
			observations: [null],
		});

		this.route.paramMap.subscribe(params => {
			this.episodeId = Number(params.get('id'));
			const episodeCreatedOn$ = this.emergencyCareEpisodeService.getCreationDate(this.episodeId)
				.pipe(map(dateTimeDtoToDate));

			episodeCreatedOn$.subscribe(episodeCreatedOn => {
				this.episodeCreatedOn = episodeCreatedOn;
				this.setDateTimeValidation(episodeCreatedOn);
			});

			this.emergencyCareEpisodeService.getAdministrative(this.episodeId).subscribe((dto: ResponseEmergencyCareDto) => {
				this.patientId = dto.patient ? dto.patient.id : null;

				if (this.patientId) {
					this.loadPatient(this.patientId);
				}

			});
		});

		this.loadProblems();

		this.permissionService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.isNurse = !userRoles.some(userRole =>  HEALTCARE_PROFESSIONALS.includes(userRole));
		});

	}

	private buildTimePickerData(): TimePickerData {
		return {
			defaultTime: {
				hours: this.today.getHours(),
				minutes: this.today.getMinutes(),
			}
		};
	}

	private loadPatient(patientId: number) {
		this.patientSummaryDataService.loadPatient(patientId).subscribe(patientSummary => {
			this.patientSummary = patientSummary;
		});
	}

	private loadProblems() {
		this.problems$ = this.emergencyCareStateService.getEmergencyCareEpisodeDiagnoses(this.episodeId).pipe(
			map(problems => this.selectMainProblem(problems))
		);
	}

	private selectMainProblem(problems: DiagnosesGeneralStateDto[]): DiagnosesGeneralStateDto[] {
		problems.forEach(problem => {
			if (problem.main) {
				this.selectedProblems.set(problem.id, problem);
			}
		});
		return problems;
	}

	checkIfShouldDisable(problem: DiagnosesGeneralStateDto): boolean {
		return this.selectedProblems.size === 1 && this.selectedProblems.has(problem.id);
	}

    toggleSelection(problem: DiagnosesGeneralStateDto): void {
		this.selectedProblems.has(problem.id) ?
		this.selectedProblems.delete(problem.id) : this.selectedProblems.set(problem.id, problem);
    }

	dischargedDateChanged(date: Date) {
		this.form.controls.dateTime.get('date').setValue(date);
	}

	dischargedTimeChanged(time: TimeDto) {
		this.form.controls.dateTime.get('time').setValue(time);
    }

	confirm(): void {
		this.formSubmited = true;
		this.isLoading = true;
		const selectedProblems: DiagnosesGeneralStateDto[] = Array.from(this.selectedProblems.values());
		if (this.form.valid && selectedProblems.length) {
			const validForm: MedicalDischargeForm = { ... this.form.value, problems: selectedProblems };
			const medicalCoverageDto: AMedicalDischargeDto = this.guardiaMapperService.formToAMedicalDischargeDto(validForm);
			this.emergencyCareEspisodeDischargeService.newMedicalDischarge
				(this.episodeId, medicalCoverageDto).subscribe({
					next: (saved) => {
						this.isLoading = false
						if (saved) {
							this.goToEpisodeDetails();
							this.snackBarService.showSuccess('guardia.episode.medical_discharge.messages.SUCCESS');
						}
					},
					error: (err: ApiErrorMessageDto) => {
						this.isLoading = false
						this.snackBarService.showError(err.text);
					}
				});
		} else{
			this.markFormGroupTouched(this.form);
			this.formSubmited = false;
			this.isLoading = false;
		}
	}

	private markFormGroupTouched(formGroup: UntypedFormGroup) {
		Object.keys(formGroup.controls).forEach(key => {
			const control = formGroup.get(key);
			control.markAsTouched();
			if (control instanceof UntypedFormGroup) {
				this.markFormGroupTouched(control);
			}
		});
	}

	goToEpisodeDetails(): void {
		const url = `institucion/${this.contextService.institutionId}/ambulatoria/paciente/${this.patientId}`;
		this.router.navigateByUrl(url, { state: { toEmergencyCareTab: true } });

	}

	private setDateTimeValidation(episodeCreatedOn: Date): void {
		const dateTimeControl: UntypedFormGroup = (this.form.controls.dateTime) as UntypedFormGroup;
		const dateControl: AbstractControl = dateTimeControl.controls.date;
		const timeControl: AbstractControl = dateTimeControl.controls.time;

		timeControl.setValidators([Validators.required, beforeTimeValidationDate(episodeCreatedOn,dateControl.value),
			futureTimeValidationDate()]);

		this.form.get('dateTime.date').valueChanges.subscribe(
			selectedDate => {
				timeControl.clearValidators();
				requiredValidation();
				if (isSameDay(new Date(), new Date(selectedDate))) {
					beforeTodayValidation();
				}
				if (isSameDay(episodeCreatedOn, new Date(selectedDate))) {
					afterEpisodeCreationValidation();
				}

				function requiredValidation(): void {
					timeControl.setValidators([Validators.required]);
					timeControl.updateValueAndValidity();
				}

				function beforeTodayValidation(): void {
					const existingValidators = timeControl.validator;
					timeControl.setValidators([existingValidators, futureTimeValidationDate()]);
					timeControl.updateValueAndValidity();
				}

				function afterEpisodeCreationValidation(): void {
					const existingValidators = timeControl.validator;
					timeControl.setValidators([existingValidators, beforeTimeValidationDate(episodeCreatedOn,dateControl.value)]);
					timeControl.updateValueAndValidity();
				}
			}
		);
	}

	setMedicalDischargeByNurse(medicalDischargeByNurse: MedicalDischargeByNurse) {
		this.form.controls.dischargeTypeId.setValue(medicalDischargeByNurse.dischargeTypeId);
		this.form.controls.observations.setValue(medicalDischargeByNurse.observations);
		const selectedProblemsByNurse = this.buildSelectedProblems(medicalDischargeByNurse.problem);
		this.selectedProblems.set(selectedProblemsByNurse.key, selectedProblemsByNurse.value);
	}

	private buildSelectedProblems(medicalDischargeByNurseProblem: SnomedDto): {key: number, value: DiagnosesGeneralStateDto} {
		return {
			key: Number(medicalDischargeByNurseProblem.sctid),
			value: {
				main: false, snomed: medicalDischargeByNurseProblem
			}
		}
	}
}

export class MedicalDischargeForm {
	dateTime: {
		date: Date,
		time: TimeDto
	};
	autopsy: boolean;
	dischargeTypeId: number;
	problems: DiagnosesGeneralStateDto[];
	otherDischargeDescription: string;
	observations: string;
}
