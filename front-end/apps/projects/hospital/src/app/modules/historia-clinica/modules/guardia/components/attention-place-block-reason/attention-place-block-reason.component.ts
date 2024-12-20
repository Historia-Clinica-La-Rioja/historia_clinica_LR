import { Component, EventEmitter, forwardRef, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { BlockAttentionPlaceCommandDto, MasterDataDto } from '@api-rest/api-model';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { getElementAtPosition } from '@core/utils/array.utils';
import { ToFormGroup } from '@core/utils/form.utils';

const OBSERVATION_REQUIRED = 3;

@Component({
	selector: 'app-attention-place-block-reason',
	templateUrl: './attention-place-block-reason.component.html',
	styleUrls: ['./attention-place-block-reason.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => AttentionPlaceBlockReasonComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => AttentionPlaceBlockReasonComponent),
		},
	],
})
export class AttentionPlaceBlockReasonComponent extends AbstractCustomForm implements OnInit {

	blockReasons: MasterDataDto[] = [];
	form: FormGroup<ToFormGroup<BlockAttentionPlaceCommandDto>>;

	@Input()
	set submitParentFormEvent(event: EventEmitter<void>) {
		super.subscribeToSubmitParentForm(event);
	};

	constructor(
		private readonly masterDataService: EmergencyCareMasterDataService,
	) {
		super();
	}

	ngOnInit(): void {
		this.createForm();
		this.loadBlockReasons();
	}

	createForm() {
		this.form = new FormGroup<ToFormGroup<BlockAttentionPlaceCommandDto>>({
			reasonId: new FormControl(null, Validators.required),
			reason: new FormControl(null),
		});
	}

	updateFormValidations() {
		const selectedOption = this.form.value.reasonId;
		if (selectedOption === OBSERVATION_REQUIRED)
			this.addRequiredValidation();
		else {
			if (this.form.controls.reason.hasValidator(Validators.required))
				this.clearValidators();
		}
	}

	private addRequiredValidation() {
		this.form.controls.reason.addValidators(Validators.required);
		this.form.controls.reason.updateValueAndValidity();
	}

	private clearValidators() {
		this.form.controls.reason.clearValidators();
		this.form.controls.reason.updateValueAndValidity();
	}

	private loadBlockReasons() {
		this.masterDataService.getAttentionPlaceBlockReasons().subscribe(blockReasons => {
			this.blockReasons = blockReasons;
			const firstElement = getElementAtPosition<MasterDataDto>(this.blockReasons, 0).id;
			this.form.controls.reasonId.setValue(firstElement);
		});
	}

}
