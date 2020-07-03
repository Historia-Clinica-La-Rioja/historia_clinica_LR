import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { InmunizationDto, SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as moment from 'moment';
import { Moment } from 'moment';
import { DatePipe } from '@angular/common';
import { DateFormat } from '@core/utils/moment.utils';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SEMANTICS_CONFIG } from '../../../../constants/snomed-semantics';
import { SnomedSemanticSearch, SnomedService } from '../../../../services/snomed.service';

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
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

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
		private snomedService: SnomedService
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
				snomed: this.snomedConcept
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

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.vaccine
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

}
