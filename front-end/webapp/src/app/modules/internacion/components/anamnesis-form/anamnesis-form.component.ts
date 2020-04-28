import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClinicalObservationDto, HealthHistoryConditionDto, MasterDataInterface } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { ANTHROPOMETRIC_DATA_COLUMNS, VITAL_SIGNS_COLUMNS } from '../../constants/anamnesis';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { removeFrom } from '@core/utils/array.utils';
import { MatTab } from '@angular/material/tabs';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	public form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];

	datosAntropometricos = [{
		bloodType: {id: 3, description: 'A'},
		height: '180',
		weight: '70'
	},
		{
			bloodType: {id: 4, description: 'A+'},
			height: '180',
			weight: '70'
		},
		{
			bloodType: {id: 5, description: 'B'},
			height: '180',
			weight: '70'
		}];
	vitalSigns = [{
		heartRate: {id: 10, description: 'a'},
		respiratoryRate: {id: 2, description: 'a'},
		temperature: {id: 3, description: 'a'},
		bloodOxygenSaturation: {id: 4, description: 'a'},
		systolicBloodPressure: {id: 5, description: 'a'},
		diastolicBloodPressure: {id: 6, description: 'a'}
	}];

	datosAntropometricosTable: {
		columns: any[],
		displayedColumns: string[],
		dataSource: MatTableDataSource<any>
	};

	vitalSignsTable: {
		columns: any[],
		displayedColumns: string[],
		dataSource: MatTableDataSource<any>
	};


	constructor(
		private formBuilder: FormBuilder,
		private internacionMasterDataService: InternacionMasterDataService
	) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			anthropometricData: this.formBuilder.group({
				bloodType: [null, Validators.required],
				height: [null, Validators.required],
				weight: [null, Validators.required],
			}),
			vitalSigns: this.formBuilder.group({
				heartRate: [null, Validators.required],
				respiratoryRate: [null, Validators.required],
				temperature: [null, Validators.required],
				bloodOxigenSaturation: [null, Validators.required],
				systolicBloodPressure: [null, Validators.required],
				diastolicBloodPressure: [null, Validators.required],
			}),
			observations: this.formBuilder.group ({
				current_disease: [null, Validators.required],
				physical_examination: [null, Validators.required],
				studies_procedures: [null, Validators.required],
				patient_progress: [null, Validators.required],
				clinical_impression: [null, Validators.required],
				others: [null]
			})
		});

		this.vitalSignsTable = {
			columns: VITAL_SIGNS_COLUMNS,
			displayedColumns: VITAL_SIGNS_COLUMNS?.map(c => c.def),
			dataSource: new MatTableDataSource<any>(this.vitalSigns)
		};
		this.vitalSignsTable.displayedColumns = this.vitalSignsTable.columns?.map(c => c.def).concat(['remove']);

		this.datosAntropometricosTable = {
			columns: ANTHROPOMETRIC_DATA_COLUMNS,
			displayedColumns: ANTHROPOMETRIC_DATA_COLUMNS?.map(c => c.def),
			dataSource: new MatTableDataSource<any>(this.datosAntropometricos)
		};
		this.datosAntropometricosTable.displayedColumns = this.datosAntropometricosTable.columns?.map(c => c.def).concat(['remove']);

		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => {
			this.bloodTypes = bloodTypes;
		});
	}

	remove(data: any[], tableDataSource: MatTableDataSource<any>, index: number): void {
		const toRemove = tableDataSource.data[index];
		data.find(item => item === toRemove).deleted = true;
		tableDataSource.data = data.filter(item => !item.deleted);
	}

	save(): void {
		console.log('form: ', this.form);
	}

	back(): void {
		window.history.back();
	}

}
