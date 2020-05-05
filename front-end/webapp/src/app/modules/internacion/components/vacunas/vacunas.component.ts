import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { InmunizationDto, SnomedDto, MasterDataInterface } from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Moment } from 'moment';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
import { DateFormat } from '@core/utils/moment.utils';
import { pushTo, removeFrom } from '@core/utils/array.utils';

@Component({
	selector: 'app-vacunas',
	templateUrl: './vacunas.component.html',
	styleUrls: ['./vacunas.component.scss']
})
export class VacunasComponent implements OnInit {

	private inmunizationsValue: InmunizationDto[];

	@Output() inmunizationsChange = new EventEmitter();

	@Input()
	set inmunizations(inmunizations: InmunizationDto[]) {
		this.inmunizationsValue = inmunizations;
		this.inmunizationsChange.emit(this.inmunizationsValue);
	}

	get inmunizations(): InmunizationDto[] {
		return this.inmunizationsValue;
	}

	snomedConcept: SnomedDto;

	form: FormGroup;
	today: Moment = moment();

	// Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.vacunas.table.columns.INMUNIZATION',
			text: v => v.snomed.pt
		},
		{
			def: 'date',
			header: 'internaciones.anamnesis.vacunas.table.columns.REGISTRY_DATE',
			text: v => this.datePipe.transform(v.administrationDate, 'dd/MM/yyyy')
		},
	];
	displayedColumns: string[] = [];

	constructor(
		private formBuilder: FormBuilder,
		private datePipe: DatePipe,
	) {
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
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
			const vacuna: InmunizationDto = {
				administrationDate: this.form.value.date ? this.form.value.date.format(DateFormat.API_DATE) : null,
				note: null,
				id: null,
				snomed: this.snomedConcept,
				statusId: null
			};
			this.add(vacuna);
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

	add(vacuna: InmunizationDto): void {
		this.inmunizations = pushTo<InmunizationDto>(this.inmunizations, vacuna);
	}

	remove(index: number): void {
		this.inmunizations = removeFrom<InmunizationDto>(this.inmunizations, index);

	}

}
