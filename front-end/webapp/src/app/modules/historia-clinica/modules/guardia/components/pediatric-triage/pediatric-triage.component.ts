import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DateTimeDto, MasterDataDto, NewEffectiveClinicalObservationDto, TriagePediatricDto } from '@api-rest/api-model';
import { dateToDateTimeDto } from '@api-rest/mapper/date-dto.mapper';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { VITAL_SIGNS } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-pediatric-triage',
	templateUrl: './pediatric-triage.component.html',
	styleUrls: ['./pediatric-triage.component.scss']
})
export class PediatricTriageComponent implements OnInit {

	@Input('confirmLabel') confirmLabel = 'Confirmar episodio';
	@Input('cancelLabel') cancelLabel = 'Volver';
	@Output() onConfirm = new EventEmitter();
	@Output() onCancel = new EventEmitter();
	private triageCategoryId: number;
	private doctorsOfficeId: number;
	pediatricForm: FormGroup;
	bodyTemperatures$: Observable<MasterDataDto[]>;
	muscleHypertonyaOptions$: Observable<MasterDataDto[]>;
	perfusionOptions$: Observable<MasterDataDto[]>;
	respiratoryRetractionOptions$: Observable<MasterDataDto[]>;

	hasError = hasError;
	minValue = VITAL_SIGNS.min_value;
	constructor(
		private formBuilder: FormBuilder,
		private readonly triageMasterDataService: TriageMasterDataService,
	) {
	}

	ngOnInit(): void {
		this.pediatricForm = this.formBuilder.group({
			notes: [null],
			appearance: this.formBuilder.group({
				bodyTemperatureId: [null],
				cryingExcessive: [null],
				muscleHypertoniaId: [null]

			}),
			breathing: this.formBuilder.group({
				bloodOxygenSaturation: [null, Validators.min(this.minValue)],
				respiratoryRate: [null, Validators.min(this.minValue)],
				respiratoryRetractionId: [null],
				stridor: [null]
			}),
			circulation: this.formBuilder.group({
				heartRate: [null, Validators.min(this.minValue)],
				perfusionId: [null]
			}),
		});
		this.bodyTemperatures$ = this.triageMasterDataService.getBodyTemperature();
		this.muscleHypertonyaOptions$ = this.triageMasterDataService.getMuscleHypertonia();
		this.perfusionOptions$ = this.triageMasterDataService.getPerfusion();
		this.respiratoryRetractionOptions$ = this.triageMasterDataService.getRespiratoryRetraction();
	}

	setTriageCategoryId(triageCategoryId: number): void {
		this.triageCategoryId = triageCategoryId;
	}

	setDoctorsOfficeId(doctorsOfficeId: number): void {
		this.doctorsOfficeId = doctorsOfficeId;
	}

	confirm(): void {
		if (this.pediatricForm.valid) {
			const triage: TriagePediatricDto = this.toTriagePediatricDto();
			this.onConfirm.emit(triage);
		}
	}

	back(): void {
		this.onCancel.emit();
	}

	private toNewEffectiveClinicalObservationDto(effectiveTime: DateTimeDto, value: any): NewEffectiveClinicalObservationDto {
		return value ? { effectiveTime, value } : null;
	}


	private toTriagePediatricDto(): TriagePediatricDto {
		const formValue = this.pediatricForm.value;
		const dateTimeDto: DateTimeDto = dateToDateTimeDto(new Date());
		const triage: TriagePediatricDto = {
			categoryId: this.triageCategoryId,
			doctorsOfficeId: this.doctorsOfficeId,
			...formValue,
		};
		triage.breathing.bloodOxygenSaturation = this.toNewEffectiveClinicalObservationDto(dateTimeDto, formValue.breathing.bloodOxygenSaturation);
		triage.breathing.respiratoryRate = this.toNewEffectiveClinicalObservationDto(dateTimeDto, formValue.breathing.respiratoryRate);
		triage.circulation.heartRate = this.toNewEffectiveClinicalObservationDto(dateTimeDto, formValue.circulation.heartRate);
		return triage;
	}

}
