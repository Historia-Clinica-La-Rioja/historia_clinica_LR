import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SnomedDto, HealthHistoryConditionDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { pushTo, removeFrom } from '@core/utils/array.utils';

@Component({
	selector: 'app-antecedentes-familiares',
	templateUrl: './antecedentes-familiares.component.html',
	styleUrls: ['./antecedentes-familiares.component.scss']
})
export class AntecedentesFamiliaresComponent implements OnInit {

	private familyHistoriesValue: HealthHistoryConditionDto[];

	@Output() familyHistoriesChange = new EventEmitter();

	@Input()
	set familyHistories(familyHistories: HealthHistoryConditionDto[]) {
		this.familyHistoriesValue = familyHistories;
		this.familyHistoriesChange.emit(this.familyHistoriesValue);
	}

	get familyHistories(): HealthHistoryConditionDto[] {
		return this.familyHistoriesValue;
	}

	private snomedConcept: SnomedDto;

	form: FormGroup;

	//Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.antecedentes-familiares.table.columns.FAMILY_HISTORY',
			text: af => af.snomed.pt
		}
	];
	displayedColumns: string[] = [];
	dataSource: MatTableDataSource<HealthHistoryConditionDto>;

	constructor(
		private formBuilder: FormBuilder
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.dataSource = new MatTableDataSource<HealthHistoryConditionDto>(this.familyHistories);
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required]
		});
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const antecedenteFamiliar: HealthHistoryConditionDto = {
				date: null,
				note: null,
				verificationId: null,
				id: null,
				snomed: this.snomedConcept,
				statusId: null
			};
			this.add(antecedenteFamiliar);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	add(af: HealthHistoryConditionDto): void {
		this.dataSource.data = pushTo<HealthHistoryConditionDto>(this.dataSource.data, af);
		this.familyHistories.push(af);
	}

	remove(index: number): void {
		const toRemove = this.dataSource.data[index];
		if (toRemove.id == null) {
			this.dataSource.data = removeFrom<HealthHistoryConditionDto>(this.dataSource.data, index);
			this.familyHistories = this.familyHistories.filter(item => toRemove !== item);
		}
	}

}
