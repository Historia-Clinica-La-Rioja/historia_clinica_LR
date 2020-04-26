import { Component, OnInit } from '@angular/core';
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

	private snomedConcept: SnomedDto;

	form: FormGroup;
	today: Moment = moment();
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];
	categories: MasterDataInterface<string>[];

	//Mat table
	columns = [
		{
			def: "problemType",
			header: 'internaciones.anamnesis.alergias.table.columns.PROBLEM_TYPE',
			text: ap => ap.snomed.fsn
		},
		{
			def: "category",
			header: 'internaciones.anamnesis.alergias.table.columns.CATEGORY',
			text: ap => this.categories.find(category => category.id === ap.categoryId).description
		},
		{
			def: "severity",
			header: 'internaciones.anamnesis.alergias.table.columns.SEVERITY',
			text: ap => ap.severity
		},
		{
			def: "clinicalStatus",
			header: 'internaciones.anamnesis.alergias.table.columns.STATUS',
			text: ap => this.clinicalStatus.find(status => status.id === ap.statusId).description
		},
		{
			def: 'verification',
			header: 'internaciones.anamnesis.alergias.table.columns.VERIFICATION',
			text: ap => this.verifications.find(verification => verification.id === ap.verificationId).description
		},
		{
			def: 'date',
			header: 'internaciones.anamnesis.alergias.table.columns.REGISTRY_DATE',
			text: ap => this.datePipe.transform(ap.date, 'dd/MM/yyyy')
		},
	];
	displayedColumns: string[] = [];
	apDataSource = new MatTableDataSource<any>([]);

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

		this.internacionMasterDataService.getAllergyClinical().subscribe(healthClinical => {
			this.clinicalStatus = healthClinical;
		});

		this.internacionMasterDataService.getAllergyVerifications().subscribe(healthVerification => {
			this.verifications = healthVerification;
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
			let antecedetePersonal: AllergyConditionDto = this.form.value;
			antecedetePersonal.date = this.form.controls.date.value.format(DateFormat.API_DATE);
			antecedetePersonal.snomed = this.snomedConcept;
			this.add(antecedetePersonal);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let fsn = selectedConcept ? selectedConcept.fsn : '';
		this.form.controls.snomed.setValue(fsn);
	}

	add(ap: AllergyConditionDto): void {
		this.apDataSource.data = pushTo<AllergyConditionDto>(this.apDataSource.data, ap);
	}

	remove(index: number): void {
		this.apDataSource.data = removeFrom<AllergyConditionDto>(this.apDataSource.data, index);
	}

}
