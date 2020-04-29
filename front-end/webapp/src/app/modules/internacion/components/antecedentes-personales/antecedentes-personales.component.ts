import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HealthHistoryConditionDto, MasterDataInterface, SnomedDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { DateFormat } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import * as moment from 'moment';
import { DatePipe } from '@angular/common';
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
	today: Moment = moment();
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];

	//Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.PROBLEM_TYPE',
			text: ap => ap.snomed.fsn
		},
		{
			def: 'clinicalStatus',
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.STATUS',
			text: ap => this.clinicalStatus?.find(status => status.id === ap.statusId).description
		},
		{
			def: 'verification',
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.VERIFICATION',
			text: ap => this.verifications?.find(verification => verification.id === ap.verificationId).description
		},
		{
			def: 'date',
			header: 'internaciones.anamnesis.antecedentes-personales.table.columns.REGISTRY_DATE',
			text: ap => this.datePipe.transform(ap.date, 'dd/MM/yyyy')
		},
	];
	displayedColumns: string[] = [];
	dataSource: MatTableDataSource<HealthHistoryConditionDto>;

	constructor(
		private formBuilder: FormBuilder,
		private datePipe: DatePipe,
		private internacionMasterDataService: InternacionMasterDataService
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.dataSource = new MatTableDataSource<HealthHistoryConditionDto>(this.personalHistories);
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
			const antecedentePersonal: HealthHistoryConditionDto = {
				date: this.form.value.date.format(DateFormat.API_DATE),
				note: this.form.value.note,
				verificationId: this.form.value.verificationId,
				id: null,
				snomed: this.snomedConcept,
				statusId: this.form.value.statusId
			};
			this.add(antecedentePersonal);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let fsn = selectedConcept ? selectedConcept.fsn : '';
		this.form.controls.snomed.setValue(fsn);
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
