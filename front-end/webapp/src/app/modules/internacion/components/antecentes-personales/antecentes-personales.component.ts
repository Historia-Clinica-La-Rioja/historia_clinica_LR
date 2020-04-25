import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { HealthHistoryConditionDto, MasterDataInterface, SnomedDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DateFormat } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
import { AnamnesisFormService } from '../../services/anamnesis-form.service';

@Component({
	selector: 'app-antecentes-personales',
	templateUrl: './antecentes-personales.component.html',
	styleUrls: ['./antecentes-personales.component.scss']
})
export class AntecentesPersonalesComponent implements OnInit {

	current: any = {};
	form: FormGroup;
	today: Moment = moment();
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];

	//Mat table
	columns = [
		{
			def: "problemType",
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.PROBLEM_TYPE',
			text: ap => ap.snomed.fsn
		},
		{
			def: "clinicalStatus",
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.STATUS',
			text: ap => this.clinicalStatus.find(status => status.id === ap.statusId).description
		},
		{
			def: 'verification',
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.VERIFICATION',
			text: ap => this.verifications.find(verification => verification.id === ap.verificationId).description
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
		private internacionMasterDataService: InternacionMasterDataService,
		private anamnesisFormService: AnamnesisFormService
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
		this.anamnesisFormService.changeSubmitted(true);
		if (this.form.valid) {
			let newHealthHistoryCondition: HealthHistoryConditionDto = this.form.value;
			newHealthHistoryCondition.date = this.form.controls.date.value.format(DateFormat.API_DATE);
			this.add(newHealthHistoryCondition);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.current.snomed = selectedConcept;
		this.form.controls.snomed.setValue(selectedConcept);
	}

	add(ap): void {
		// had to use an assignment instead of push method to produce a change on the variable observed by mat-table (apDataSource)
		this.apDataSource.data = this.apDataSource.data.concat([ap]);
		this.current = {};
	}

	remove(ap: any): void {
		this.apDataSource.data = this.apDataSource.data.filter(_ap => _ap !== ap);
	}

}
