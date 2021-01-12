import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MasterDataDto, TriageDto } from '@api-rest/api-model';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-pediatric-triage',
	templateUrl: './pediatric-triage.component.html',
	styleUrls: ['./pediatric-triage.component.scss']
})
export class PediatricTriageComponent implements OnInit {

	@Input('confirmLabel') confirmLabel: string = 'Confirmar episodio';
	@Input('cancelLabel') cancelLabel: string = 'Volver';
	@Output() onConfirm = new EventEmitter();
	@Output() onCancel = new EventEmitter();
	private triageCategoryId: number;
	private doctorsOfficeId: number;
	pediatricForm: FormGroup;
	bodyTemperatures$: Observable<MasterDataDto[]>;
	muscleHypertonyaOptions$: Observable<MasterDataDto[]>;
	perfusionOptions$: Observable<MasterDataDto[]>;
	respiratoryRetractionOptions$: Observable<MasterDataDto[]>;

	constructor(private formBuilder: FormBuilder,
	            private readonly triageMasterDataService: TriageMasterDataService) {
	}

	ngOnInit(): void {
		this.pediatricForm = this.formBuilder.group({
			evaluation: [null],
			bodyTemperatureId: [null],
			cryingExcesive: [null],
			triageCategoryId: [null],
			respiratoryRate: [null],
			respiratoryRetractionId: [null],
			stridor: [null],
			bloodOxygenSaturation: [null],
			perfusionId: [null],
			heartRate: [null]
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
		const triage: TriageDto = {
			categoryId: this.triageCategoryId,
			doctorsOfficeId: this.doctorsOfficeId
		};
		this.onConfirm.emit(triage);
	}

	back(): void {
		this.onCancel.emit();
	}
}
