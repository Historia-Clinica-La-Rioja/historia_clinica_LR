import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HealthHistoryConditionDto, SnomedDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { pushTo, removeFrom } from '@core/utils/array.utils';

@Component({
	selector: 'app-antecedentes-personales',
	templateUrl: './antecedentes-personales.component.html',
	styleUrls: ['./antecedentes-personales.component.scss']
})
export class AntecedentesPersonalesComponent implements OnInit {

	private personalHistoriesValue: HealthHistoryConditionDto[];

	@Output() personalHistoriesChange = new EventEmitter();

	@Input()
	set personalHistories(personalHistories: HealthHistoryConditionDto[]) {
		this.personalHistoriesValue = personalHistories;
		this.personalHistoriesChange.emit(this.personalHistoriesValue);
	}

	get personalHistories(): HealthHistoryConditionDto[] {
		return this.personalHistoriesValue;
	}

	private snomedConcept: SnomedDto;

	form: FormGroup;

	//Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.PERSONAL_HISTORY',
			text: ap => ap.snomed.pt
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
		this.dataSource = new MatTableDataSource<HealthHistoryConditionDto>(this.personalHistories);
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required]
		});
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const antecedentePersonal: HealthHistoryConditionDto = {
				date: null,
				note: null,
				verificationId: null,
				id: null,
				snomed: this.snomedConcept,
				statusId: null
			};
			this.add(antecedentePersonal);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	add(ap: HealthHistoryConditionDto): void {
		this.dataSource.data = pushTo<HealthHistoryConditionDto>(this.dataSource.data, ap);
		this.personalHistories.push(ap);
	}

	remove(index: number): void {
		const toRemove = this.dataSource.data[index];
		if (toRemove.id == null) {
			this.dataSource.data = removeFrom<HealthHistoryConditionDto>(this.dataSource.data, index);
			this.personalHistories = this.personalHistories.filter(item => toRemove !== item);
		}
	}

}
