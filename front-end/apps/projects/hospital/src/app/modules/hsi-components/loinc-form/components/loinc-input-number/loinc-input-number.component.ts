import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ProcedureParameterUnitOfMeasureFullSummaryDto } from '@api-rest/api-model';
import { ButtonService } from '@historia-clinica/modules/ambulatoria/services/button.service';
import { ChangeEvent } from 'react';
@Component({
	selector: 'app-loinc-input-number',
	templateUrl: './loinc-input-number.component.html',
	styleUrls: ['./loinc-input-number.component.scss']
})

export class LoincInputNumberComponent implements OnInit {
	_preloadUnit
	unitOfMeasureId: number;
	value = '';
	valueNumeric: number = 0;
	numberControl: FormControl;

	@Input() title: string;
	@Input() listOptionsUnits: ProcedureParameterUnitOfMeasureFullSummaryDto[];
	@Input() preloadValue;
	@Input() preloadUnit: number;
	@Output() valueSelected: EventEmitter<NumberWithUnit> = new EventEmitter<NumberWithUnit>();

	protected readonly numberPattern = '^[\\-\\+]?[0-9]*,?[0-9]+([eE][\\-\\+]?[0-9]+)?$';

	constructor(
		readonly buttonService: ButtonService,
	) { }

	ngOnInit() {
		this.numberControl = new FormControl(this.preloadValue || '', [
			Validators.pattern(this.numberPattern),
		]);

		this.disablePartialStudyButton(this.numberControl.valid)

		this.numberControl.valueChanges.subscribe(value => {
			this.numberControl.markAsTouched()
			this.disablePartialStudyButton(this.numberControl.valid)
			if (this.numberControl.valid) {
				this.value = value;
				this.emitValueSelected();
			}
		});

		this.setPreloadUnited();

		if (this.preloadValue)
			this.emitValuesPreload();
		else {
			this.setFirstElment();
		}
	}

	private setPreloadUnited() {
		this._preloadUnit = this.listOptionsUnits.find(elem => elem.unitOfMeasureId === this.preloadUnit);
	}

	emitValueChange($event: ChangeEvent<HTMLInputElement>) {
		this.value = $event.target.value
		this.emitValueSelected();
	}

	onUnitChange(unitOfMeasureId: number) {
		this.unitOfMeasureId = unitOfMeasureId;
		this.emitValueSelected();
	}

	private emitValuesPreload() {

		this.synchronizePreloadedValues();
		this.emitValueSelected();
	}

	private synchronizePreloadedValues() {
		this.value = this.preloadValue;
		this.unitOfMeasureId = this.preloadUnit;
	}

	private emitValueSelected() {
		if (!this.numberControl.valid) return;

		const value: NumberWithUnit = {
			value: this.value,
			unitOfMeasureId: this.unitOfMeasureId,
			valueNumeric: parseFloat(this.value?.replace(',', '.')),
		};
		this.valueSelected.emit(value);
	}

	private setFirstElment() {
		const firstElment = this.listOptionsUnits[0]
		this._preloadUnit = firstElment;
		this.onUnitChange(firstElment.unitOfMeasureId);
	}

	private disablePartialStudyButton(valid: boolean) {
		this.buttonService.updateFormPartialSaveStatus(valid ? false : true)
	}

}

export interface NumberWithUnit {
	value: string,
	unitOfMeasureId: number,
	valueNumeric: number
}
