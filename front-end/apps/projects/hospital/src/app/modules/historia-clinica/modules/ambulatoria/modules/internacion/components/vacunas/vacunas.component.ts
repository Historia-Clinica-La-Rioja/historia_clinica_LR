import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ImmunizationDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL} from '@api-rest/api-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Moment } from 'moment';
import { DatePipe } from '@angular/common';
import { DateFormat, newMoment } from '@core/utils/moment.utils';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { MIN_DATE } from "@core/utils/date.utils";
import { SnomedService, SnomedSemanticSearch } from '@historia-clinica/services/snomed.service';

@Component({
	selector: 'app-vacunas',
	templateUrl: './vacunas.component.html',
	styleUrls: ['./vacunas.component.scss']
})
export class VacunasComponent implements OnInit {

	private immunizationsValue: ImmunizationDto[];

	@Output() immunizationsChange = new EventEmitter();

	@Input()
	set immunizations(immunizations: ImmunizationDto[]) {
		this.immunizationsValue = immunizations;
		this.immunizationsChange.emit(this.immunizationsValue);
	}

	get immunizations(): ImmunizationDto[] {
		return this.immunizationsValue;
	}

	snomedConcept: SnomedDto;

	form: FormGroup;
	today: Moment = newMoment();

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

	minDate = MIN_DATE;

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
			const vacuna: ImmunizationDto = {
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

	add(vacuna: ImmunizationDto): void {
		this.immunizations = pushTo<ImmunizationDto>(this.immunizations, vacuna);
	}

	remove(index: number): void {
		this.immunizations = removeFrom<ImmunizationDto>(this.immunizations, index);
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.VACCINE
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

}
