import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MasterDataInterface, SnomedDto, HealthHistoryConditionDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';

@Component({
	selector: 'app-diagnosticos',
	templateUrl: './diagnosticos.component.html',
	styleUrls: ['./diagnosticos.component.scss']
})
export class DiagnosticosComponent implements OnInit {

	private snomedConcept: SnomedDto;

	form: FormGroup;
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];

	//Mat table
	columns = [
		{
			def: "diagnosis",
			header: 'internaciones.anamnesis.diagnosticos.table.columns.DIAGNOSIS',
			text: ap => ap.snomed.fsn
		},
		{
			def: "clinicalStatus",
			header: 'internaciones.anamnesis.diagnosticos.table.columns.STATUS',
			text: ap => this.clinicalStatus.find(status => status.id === ap.statusId).description
		},
		{
			def: 'verification',
			header: 'internaciones.anamnesis.diagnosticos.table.columns.VERIFICATION',
			text: ap => this.verifications.find(verification => verification.id === ap.verificationId).description
		},
	];
	displayedColumns: string[] = [];
	apDataSource = new MatTableDataSource<any>([]);

	constructor(
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService,
	)
	{
		this.displayedColumns = this.columns?.map(c => c.def).concat(['remove']);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
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

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			let newDiagnosis: HealthHistoryConditionDto = this.form.value;
			newDiagnosis.snomed = this.snomedConcept;
			this.add(newDiagnosis);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let fsn = selectedConcept ? selectedConcept.fsn : '';
		this.form.controls.snomed.setValue(fsn);
	}

	add(diagnosis): void {
		this.apDataSource.data = pushTo(this.apDataSource.data, diagnosis);
	}

	remove(index: number): void {
		this.apDataSource.data = removeFrom(this.apDataSource.data, index);
	}

}
