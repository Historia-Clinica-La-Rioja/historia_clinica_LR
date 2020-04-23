import { Component, OnInit } from '@angular/core';
import { SnomedDto, HealthHistoryConditionDto } from '@api-rest/api-model';
import { TableModel } from '@core/components/table/table.component';
import { MatTableDataSource } from '@angular/material/table';

@Component({
	selector: 'app-antecentes-personales',
	templateUrl: './antecentes-personales.component.html',
	styleUrls: ['./antecentes-personales.component.scss']
})
export class AntecentesPersonalesComponent implements OnInit {

	current: any = {};
	form: FormGroup;
	today: Date = new Date();

	//Mat table
	columns = [
		{
			def: "problemType",
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.PROBLEM_TYPE',
			text: ap => ap.snomed.fsn
		},
		{
			def: 'date',
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.REGISTRY_DATE',
			text: ap => ap.date
		},
	];
	displayedColumns: string[] = [];
	apDataSource = new MatTableDataSource<any>([]);

	constructor(
		private formBuilder: FormBuilder
	) {
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			date: [],
		});
	}

	chosenYearHandler(newDate: Date) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Date = this.form.controls.date.value;
			ctrlDate.setFullYear(newDate.getFullYear());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	chosenMonthHandler(newDate: Date) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Date = this.form.controls.date.value;
			ctrlDate.setMonth(newDate.getMonth());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.current.snomed = selectedConcept;
	}

	add(ap): void {
		// TODO validacion snomed requerido'
		// had to concat to produce a change on the variable observed by mat-table (apDataSource)
		this.apDataSource.data = this.apDataSource.data.concat([ap]);
		this.current = {};
	}

	remove(ap: any): void {
		this.apDataSource.data = this.apDataSource.data.filter(_ap => _ap !== ap);
	}

}
