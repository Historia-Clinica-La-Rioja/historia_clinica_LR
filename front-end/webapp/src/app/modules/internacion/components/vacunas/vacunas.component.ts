import { Component, OnInit } from '@angular/core';
import { InmunizationDto, SnomedDto, MasterDataInterface } from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Moment } from 'moment';
import * as moment from 'moment';
import { MatTableDataSource } from '@angular/material/table';
import { DatePipe } from '@angular/common';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DateFormat } from '@core/utils/moment.utils';
import { pushTo, removeFrom } from '@core/utils/array.utils';

@Component({
	selector: 'app-vacunas',
	templateUrl: './vacunas.component.html',
	styleUrls: ['./vacunas.component.scss']
})
export class VacunasComponent implements OnInit {

	private snomedConcept: SnomedDto;

	form: FormGroup;
	today: Moment = moment();
	inmunizationStatus: MasterDataInterface<string>[];

	//Mat table
	columns = [
		{
			def: "problemType",
			header: 'internaciones.anamnesis.vacunas.table.columns.PROBLEM_TYPE',
			text: v => v.snomed.fsn
		},
		{
			def: "clinicalStatus",
			header: 'internaciones.anamnesis.vacunas.table.columns.STATUS',
			text: v => this.inmunizationStatus.find(status => status.id === v.statusId).description
		},
		{
			def: 'date',
			header: 'internaciones.anamnesis.vacunas.table.columns.REGISTRY_DATE',
			text: v => this.datePipe.transform(v.administrationDate, 'dd/MM/yyyy')
		},
	];
	displayedColumns: string[] = [];
	dataSource = new MatTableDataSource<any>([]);

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
			statusId: [null, Validators.required],
			snomed: [null, Validators.required]
		});

		this.internacionMasterDataService.getInmunizationClinical().subscribe(inmunizationStatus => {
			this.inmunizationStatus = inmunizationStatus;
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
			let vacuna: InmunizationDto = this.form.value;
			vacuna.administrationDate = this.form.controls.date.value.format(DateFormat.API_DATE);
			vacuna.snomed = this.snomedConcept;
			this.add(vacuna);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let fsn = selectedConcept ? selectedConcept.fsn : '';
		this.form.controls.snomed.setValue(fsn);
	}

	add(vacuna: InmunizationDto): void {
		this.dataSource.data = pushTo<InmunizationDto>(this.dataSource.data, vacuna);
	}

	remove(index: number): void {
		this.dataSource.data = removeFrom<InmunizationDto>(this.dataSource.data, index);
	}

}
