import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MasterDataInterface, HealthHistoryConditionDto, SnomedDto } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { AnamnesisFormService } from '../../services/anamnesis-form.service';

@Component({
	selector: 'app-diagnosticos',
	templateUrl: './diagnosticos.component.html',
	styleUrls: ['./diagnosticos.component.scss']
})
export class DiagnosticosComponent implements OnInit {

	current: any = {};
	form: FormGroup;
	verifications: MasterDataInterface<string>[] = [{id: 'a', description: 'description a'}, {id: 'b', description: 'description b'}, {id: 'c', description: 'description c'}];
	clinicalStatus: MasterDataInterface<string>[] = [{id: 'a', description: 'description a'}, {id: 'b', description: 'description b'}, {id: 'c', description: 'description c'}];

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
		private anamnesisFormService: AnamnesisFormService
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
	}

	addToList() {
		this.anamnesisFormService.changeSubmitted(true);
		if (this.form.valid) {
			this.add(this.form.value);
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
