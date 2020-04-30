import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MasterDataInterface, SnomedDto, HealthConditionDto, DiagnosisDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';

@Component({
	selector: 'app-diagnosticos',
	templateUrl: './diagnosticos.component.html',
	styleUrls: ['./diagnosticos.component.scss']
})
export class DiagnosticosComponent implements OnInit {

	@Input() diagnosis: DiagnosisDto[] = [];

	private snomedConcept: SnomedDto;

	form: FormGroup;
	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];

	//Mat table
	columns = [
		{
			def: 'diagnosis',
			header: 'internaciones.anamnesis.diagnosticos.table.columns.DIAGNOSIS',
			text: ap => ap.snomed.fsn
		},
		{
			def: 'clinicalStatus',
			header: 'internaciones.anamnesis.diagnosticos.table.columns.STATUS',
			text: ap => this.clinicalStatus?.find(status => status.id === ap.statusId).description
		},
		{
			def: 'verification',
			header: 'internaciones.anamnesis.diagnosticos.table.columns.VERIFICATION',
			text: ap => this.verifications?.find(verification => verification.id === ap.verificationId).description
		},
	];
	displayedColumns: string[] = [];
	dataSource = new MatTableDataSource<DiagnosisDto>(this.diagnosis);

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
			let diagnostico: DiagnosisDto = {
				verificationId: this.form.value.verificationId,
				statusId: this.form.value.statusId,
				id: null,
				presumptive: false,
				snomed: this.snomedConcept
			};
			this.add(diagnostico);
		}
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		let fsn = selectedConcept ? selectedConcept.fsn : '';
		this.form.controls.snomed.setValue(fsn);
	}

	add(diagnostico: DiagnosisDto): void {
		this.dataSource.data = pushTo<DiagnosisDto>(this.dataSource.data, diagnostico);
		this.diagnosis.push(diagnostico);
	}

	remove(index: number): void {
		const toRemove = this.dataSource.data[index];
		if (toRemove.id == null) {
			this.dataSource.data = removeFrom<DiagnosisDto>(this.dataSource.data, index);
			this.diagnosis = this.diagnosis.filter(item => toRemove !== item);
		}
	}

}
