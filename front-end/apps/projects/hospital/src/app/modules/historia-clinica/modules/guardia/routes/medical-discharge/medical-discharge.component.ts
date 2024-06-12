import { Component, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AMedicalDischargeDto, DiagnosesGeneralStateDto, MasterDataInterface, ResponseEmergencyCareDto, TimeDto } from '@api-rest/api-model';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DischargeTypes, medicalDischargeCustomOrder } from '@api-rest/masterdata';
import { EmergencyCareEpisodeMedicalDischargeService } from '@api-rest/services/emergency-care-episode-medical-discharge.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { ContextService } from '@core/services/context.service';
import { hasError, beforeTimeDtoValidationDate, futureTimeDtoValidationDate } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';
import { MIN_DATE } from "@core/utils/date.utils";
import { isSameDay } from 'date-fns';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';
import { EmergencyCareStateService } from '@api-rest/services/emergency-care-state.service';

@Component({
	selector: 'app-medical-discharge',
	templateUrl: './medical-discharge.component.html',
	styleUrls: ['./medical-discharge.component.scss']
})
export class MedicalDischargeComponent implements OnInit {

	hasError = hasError;

	form: UntypedFormGroup;
	dischargeTypes$: Observable<MasterDataInterface<number>[]>;
	dischargeTypesEnum = DischargeTypes;

	selectedProblems: Map<number, DiagnosesGeneralStateDto> = new Map();
	problems$: Observable<DiagnosesGeneralStateDto[]>;
	today = new Date();
	episodeCreatedOn: Date;
	formSubmited = false;
	isLoading = false;
	private episodeId: number;
	private patientId: number;

	timePickerData : TimePickerData = {
        defaultTime : {
			hours: this.today.getHours(),
			minutes: this.today.getMinutes(),
		}
    };
	minDate = MIN_DATE;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly contextService: ContextService,
		private readonly emergencyCareEspisodeDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly emergencyCareStateService : EmergencyCareStateService,
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly snackBarService: SnackBarService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
	) {}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			dateTime: this.formBuilder.group({
				date: [new Date(), Validators.required],
				time: [this.timePickerData.defaultTime],
			}),
			autopsy: [null],
			dischargeTypeId: [DischargeTypes.ALTA_MEDICA, Validators.required],
			otherDischargeDescription: [null]
		});

		this.form.get('dischargeTypeId').valueChanges.subscribe(discharge => {
			this.updateDischargeTypeValidators(discharge);
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
			});
		});

        this.dischargeTypes$ = this.emergencyCareMasterDataService.getDischargeType()
            .pipe(
                map(dischargeTypes => this.customOrderDischargeTypes(dischargeTypes))
            );

			this.problems$ = this.emergencyCareStateService.getEmergencyCareEpisodeDiagnoses(this.episodeId).pipe(
				map(problems => {
					problems.forEach(problem => {
						if (problem.main) {
							this.selectedProblems.set(problem.id, problem);
						}
					});
					return problems;
				})
			);
	}

	private customOrderDischargeTypes(dischargeTypes: MasterDataInterface<number>[]): MasterDataInterface<number>[] {
        return dischargeTypes.sort((firstDischargeType, secondDischargeType) => {
            const firstIndex = medicalDischargeCustomOrder.indexOf(firstDischargeType.id);
            const secondIndex = medicalDischargeCustomOrder.indexOf(secondDischargeType.id);
            return firstIndex - secondIndex;
        });
    }

	checkIfShouldDisable(problem: DiagnosesGeneralStateDto): boolean {
		return this.selectedProblems.size === 1 && this.selectedProblems.has(problem.id);
	}

    toggleSelection(problem: DiagnosesGeneralStateDto): void {
		this.selectedProblems.has(problem.id) ?
		this.selectedProblems.delete(problem.id) : this.selectedProblems.set(problem.id, problem);
    }

	private updateDischargeTypeValidators(value: DischargeTypes): void {
		const autopsyControl = this.form.get('autopsy');
		const descriptionControl = this.form.get('otherDischargeDescription');

		this.setControlValidators(autopsyControl, value === this.dischargeTypesEnum.DEFUNCION);
		this.setControlValidators(descriptionControl, value === this.dischargeTypesEnum.OTRO);
	}

	private setControlValidators(control, condition: boolean): void {
		if (condition) {
			control.setValidators([Validators.required]);
		} else {
		  control.setValue(null);
		  control.clearValidators();
		}
		control.updateValueAndValidity();
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
		const selectedProblemsList: DiagnosesGeneralStateDto[] = Array.from(this.selectedProblems.values());
		if (this.form.valid && selectedProblemsList.length) {
			const validForm: MedicalDischargeForm = { ... this.form.value, problems: selectedProblemsList };
			const medicalCoverageDto: AMedicalDischargeDto = this.guardiaMapperService.formToAMedicalDischargeDto(validForm);
			this.emergencyCareEspisodeDischargeService.newMedicalDischarge
				(this.episodeId, medicalCoverageDto).subscribe(
					saved => {
						this.isLoading = false
						if (saved) {
							this.goToEpisodeDetails();
							this.snackBarService.showSuccess('guardia.episode.medical_discharge.messages.SUCCESS');
						}
					}, error => {
						this.isLoading = false
						this.snackBarService.showError(error.message ? error.message : 'guardia.episode.medical_discharge.messages.ERROR');
					}
				);
		} else{
			this.formSubmited = false;
			this.isLoading = false;
		}
	}

	goToEpisodeDetails(): void {
		const url = `institucion/${this.contextService.institutionId}/ambulatoria/paciente/${this.patientId}`;
		this.router.navigateByUrl(url, { state: { toEmergencyCareTab: true } });

	}

	private setDateTimeValidation(episodeCreatedOn: Date): void {
		const dateControl: UntypedFormGroup = (this.form.controls.dateTime) as UntypedFormGroup;
		const timeControl: AbstractControl = dateControl.controls.time;
		timeControl.setValidators([Validators.required, beforeTimeDtoValidationDate(episodeCreatedOn),
			futureTimeDtoValidationDate]);

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
					timeControl.setValidators([existingValidators, futureTimeDtoValidationDate]);
					timeControl.updateValueAndValidity();
				}

				function afterEpisodeCreationValidation(): void {
					const existingValidators = timeControl.validator;
					timeControl.setValidators([existingValidators, beforeTimeDtoValidationDate(episodeCreatedOn)]);
					timeControl.updateValueAndValidity();
				}
			}
		);
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
}
