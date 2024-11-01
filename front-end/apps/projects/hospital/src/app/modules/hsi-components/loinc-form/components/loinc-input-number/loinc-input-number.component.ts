import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProcedureParameterUnitOfMeasureFullSummaryDto } from '@api-rest/api-model';
import { ChangeEvent } from 'react';
// import { LoincInput } from '../../loinc-input.model';
// import { FormGroup } from '@angular/forms';

@Component({
	selector: 'app-loinc-input-number',
	templateUrl: './loinc-input-number.component.html',
	styleUrls: ['./loinc-input-number.component.scss']
})

export class LoincInputNumberComponent implements OnInit {
	_preloadUnit
	unitOfMeasureId: number;
	value = '';
	valueNumeric : number = 0;
	@Input() title: string;
	@Input() listOptionsUnits: ProcedureParameterUnitOfMeasureFullSummaryDto[];
	@Input() preloadValue;
	@Input() preloadUnit: number;
	@Output() valueSelected: EventEmitter<NumberWithUnit> = new EventEmitter<NumberWithUnit>();

	ngOnInit() {
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
		const value: NumberWithUnit = {
			value: this.value,
			unitOfMeasureId: this.unitOfMeasureId,
			valueNumeric: parseFloat(this.value && this.value.replace(',', '.')),
		}
		this.valueSelected.emit(value)
	}

	private setFirstElment() {
		const firstElment = this.listOptionsUnits[0]
		this._preloadUnit = firstElment;
		this.onUnitChange(firstElment.unitOfMeasureId);
	}

}

export interface NumberWithUnit {
	value: string,
	unitOfMeasureId: number,
	valueNumeric: number
}
