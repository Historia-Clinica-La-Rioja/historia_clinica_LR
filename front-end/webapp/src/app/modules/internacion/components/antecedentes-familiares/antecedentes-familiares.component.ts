import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { SnomedDto, MasterDataInterface, HealthHistoryConditionDto } from '@api-rest/api-model';
import { Moment } from 'moment';
import * as moment from 'moment';
import { MatTableDataSource } from '@angular/material/table';
import { DatePipe } from '@angular/common';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DateFormat } from '@core/utils/moment.utils';
import { pushTo, removeFrom } from '@core/utils/array.utils';

@Component({
	selector: 'app-antecedentes-familiares',
	templateUrl: './antecedentes-familiares.component.html',
	styleUrls: ['./antecedentes-familiares.component.scss']
})
export class AntecedentesFamiliaresComponent implements OnInit {

	private snomedConcept: SnomedDto;

	form: FormGroup;
	today: Moment = moment();
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];

	//Mat table
	columns = [
		{
			def: "problemType",
			header: 'internaciones.anamnesis.antecedentes-familiares.table.columns.PROBLEM_TYPE',
			text: af => af.snomed.fsn
		},
		{
			def: "clinicalStatus",
			header: 'internaciones.anamnesis.antecedentes-familiares.table.columns.STATUS',
			text: af => this.clinicalStatus.find(status => status.id === af.statusId).description
		},
		{
			def: 'verification',
			header: 'internaciones.anamnesis.antecedentes-familiares.table.columns.VERIFICATION',
			text: af => this.verifications.find(verification => verification.id === af.verificationId).description
		},
		{
			def: 'date',
			header: 'internaciones.anamnesis.antecedentes-familiares.table.columns.REGISTRY_DATE',
			text: af => this.datePipe.transform(af.date, 'dd/MM/yyyy')
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
			verificationId: [null, Validators.required],
			statusId: [null, Validators.required],
			snomed: [null, Validators.required]
		});

		this.internacionMasterDataService.getHealthClinical().subscribe(healthClinical => {
			this.clinicalStatus = healthClinical;
		});

		this.internacionMasterDataService.getHealthVerification().subscribe(healthVerification => {
			this.verifications = healthVerification;
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
			let antecedenteFamiliar: HealthHistoryConditionDto = this.form.value;
			antecedenteFamiliar.date = this.form.controls.date.value.format(DateFormat.API_DATE);
			antecedenteFamiliar.snomed = this.snomedConcept;
			this.add(antecedenteFamiliar);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let fsn = selectedConcept ? selectedConcept.fsn : '';
		this.form.controls.snomed.setValue(fsn);
	}

	add(af: HealthHistoryConditionDto): void {
		this.dataSource.data = pushTo<HealthHistoryConditionDto>(this.dataSource.data, af);
	}

	remove(index: number): void {
		this.dataSource.data = removeFrom<HealthHistoryConditionDto>(this.dataSource.data, index);
	}

}
