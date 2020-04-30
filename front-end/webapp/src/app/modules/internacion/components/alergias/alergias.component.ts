import { Component, Input, OnInit } from '@angular/core';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { AllergyConditionDto, SnomedDto, MasterDataInterface } from '@api-rest/api-model';
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

	@Input() allergies: AllergyConditionDto[] = [];

	private snomedConcept: SnomedDto;

	form: FormGroup;
	today: Moment = moment();
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];
	categories: MasterDataInterface<string>[];

	// Mat table
	columns = [
		{
			def: 'problemType',
			header: 'internaciones.anamnesis.alergias.table.columns.PROBLEM_TYPE',
			text: a => a.snomed.fsn
		},
		{
			def: 'category',
			header: 'internaciones.anamnesis.alergias.table.columns.CATEGORY',
			text: a => this.categories?.find(category => category.id === a.categoryId).description
		},
		{
			def: 'severity',
			header: 'internaciones.anamnesis.alergias.table.columns.SEVERITY',
			text: a => a.severity
		},
		{
			def: 'clinicalStatus',
			header: 'internaciones.anamnesis.alergias.table.columns.STATUS',
			text: a => this.clinicalStatus?.find(status => status.id === a.statusId).description
		},
		{
			def: 'verification',
			header: 'internaciones.anamnesis.alergias.table.columns.VERIFICATION',
			text: a => this.verifications?.find(verification => verification.id === a.verificationId).description
		},
		{
			def: 'date',
			header: 'internaciones.anamnesis.alergias.table.columns.REGISTRY_DATE',
			text: a => this.datePipe.transform(a.date, 'dd/MM/yyyy')
		},
	];
	displayedColumns: string[] = [];
	dataSource = new MatTableDataSource<any>(this.allergies);

	constructor(
		private formBuilder: FormBuilder,
		private datePipe: DatePipe,
		private internacionMasterDataService: InternacionMasterDataService
	) {
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			date: [null, Validators.required],
			severity: [null, Validators.required],
			categoryId: [null, Validators.required],
			note: [null, Validators.required],
			verificationId: [null, Validators.required],
			statusId: [null, Validators.required],
			snomed: [null, Validators.required]
		});

		this.internacionMasterDataService.getAllergyClinical().subscribe(clinicalStatus => {
			this.clinicalStatus = clinicalStatus;
		});

		this.internacionMasterDataService.getAllergyVerifications().subscribe(verifications => {
			this.verifications = verifications;
		});

		this.internacionMasterDataService.getAllergyCategories().subscribe(categories => {
			this.categories = categories;
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
				categoryId: this.form.value.categoryId,
				date: this.form.value.date.format(DateFormat.API_DATE),
				severity: this.form.value.severity,
				verificationId: this.form.value.verificationId,
				id: null,
				snomed: this.snomedConcept,
				statusId: this.form.value.statusId
			};
			this.add(alergia);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const fsn = selectedConcept ? selectedConcept.fsn : '';
		this.form.controls.snomed.setValue(fsn);
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
