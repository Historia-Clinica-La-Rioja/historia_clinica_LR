import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AllergyConditionDto, SnomedDto } from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Moment } from 'moment';
import * as moment from 'moment';
import { MatTableDataSource } from '@angular/material/table';
import { DatePipe } from '@angular/common';
import { DateFormat } from '@core/utils/moment.utils';
import { pushTo, removeFrom } from '@core/utils/array.utils';

@Component({
	selector: 'app-alergias',
	templateUrl: './alergias.component.html',
	styleUrls: ['./alergias.component.scss']
})
export class AlergiasComponent implements OnInit {

	private allergiesValue: AllergyConditionDto[];

	@Output() allergiesChange = new EventEmitter();

	@Input()
	set allergies(allergies: AllergyConditionDto[]) {
		this.allergiesValue = allergies;
		this.allergiesChange.emit(this.allergiesValue);
	}

	get allergies(): AllergyConditionDto[] {
		return this.allergiesValue;
	}

	snomedConcept: SnomedDto;

	form: FormGroup;
	today: Moment = moment();

	// Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.alergias.table.columns.ALLERGY',
			text: a => a.snomed.pt
		},
		{
			def: 'date',
			header: 'internaciones.anamnesis.alergias.table.columns.REGISTRY_DATE',
			text: a => this.datePipe.transform(a.date, 'dd/MM/yyyy')
		},
	];
	displayedColumns: string[] = [];
	dataSource: MatTableDataSource<AllergyConditionDto>;

	constructor(
		private formBuilder: FormBuilder,
		private datePipe: DatePipe
	) {
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.dataSource = new MatTableDataSource<AllergyConditionDto>(this.allergies);
		this.form = this.formBuilder.group({
			date: [null],
			snomed: [null, Validators.required]
		});
	}

	chosenYearHandler(newDate: Moment) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Moment = this.form.controls.date.value;
			ctrlDate.year(newDate.year());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	chosenMonthHandler(newDate: Moment) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Moment = this.form.controls.date.value;
			ctrlDate.month(newDate.month());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const alergia: AllergyConditionDto = {
				categoryId: null,
				date: this.form.value.date ? this.form.value.date.format(DateFormat.API_DATE) : null,
				severity: null,
				verificationId: null,
				id: null,
				snomed: this.snomedConcept,
				statusId: null
			};
			this.add(alergia);
			this.resetForm();
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	add(a: AllergyConditionDto): void {
		this.dataSource.data = pushTo<AllergyConditionDto>(this.dataSource.data, a);
		this.allergies.push(a);
	}

	remove(index: number): void {
		const toRemove = this.dataSource.data[index];
		if (toRemove.id == null) {
			this.dataSource.data = removeFrom<AllergyConditionDto>(this.dataSource.data, index);
			this.allergies = this.allergies.filter(item => toRemove !== item);
		}
	}

}
