import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HealthHistoryConditionDto, MasterDataInterface, SnomedDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DateFormat } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';

@Component({
	selector: 'app-antecentes-personales',
	templateUrl: './antecentes-personales.component.html',
	styleUrls: ['./antecentes-personales.component.scss']
})
export class AntecentesPersonalesComponent implements OnInit {

	current: any = {};
	form: FormGroup;
	today: Moment = moment();

	conditionsVerification: MasterDataInterface<string>[] = [{id: 'a', description: 'description a'}, {id: 'b', description: 'description b'}, {id: 'c', description: 'description c'}];
	conditionsClinicalStatus: MasterDataInterface<string>[] = [{id: 'a', description: 'description a'}, {id: 'b', description: 'description b'}, {id: 'c', description: 'description c'}];

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
			text: ap => this.datePipe.transform(ap.date, 'dd/MM/yyyy')
		},
	];
	displayedColumns: string[] = [];
	apDataSource = new MatTableDataSource<any>([]);

	constructor(
		private formBuilder: FormBuilder,
		private datePipe: DatePipe,
		private internacionMasterDataService: InternacionMasterDataService
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			date: [null, Validators.required],
			note: [null, Validators.required],
			verificationId: [null, Validators.required],
			statusId: [null, Validators.required],
			snomed: [null, Validators.required]
		});

		/*this.internacionMasterDataService.getHealthClinical().subscribe(healthClinical => {
			this.conditionsClinicalStatus = healthClinical;
		});

		this.internacionMasterDataService.getHealthVerification().subscribe(healthVerification => {
			this.conditionsVerification = healthVerification;
		});*/
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

	save() {
		if (this.form.valid) {
			let aux: HealthHistoryConditionDto = this.form.value;
			aux.date = this.form.controls.date.value.format(DateFormat.API_DATE);
			this.add(aux);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.current.snomed = selectedConcept;
		this.form.controls.snomed.setValue(selectedConcept);
	}

	add(ap): void {
		// TODO validacion snomed requerido
		// had to use an assignment instead of push method to produce a change on the variable observed by mat-table (apDataSource)
		this.apDataSource.data = this.apDataSource.data.concat([ap]);
		this.current = {};
	}

	remove(ap: any): void {
		this.apDataSource.data = this.apDataSource.data.filter(_ap => _ap !== ap);
	}

}
