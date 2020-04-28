import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MasterDataInterface } from '@api-rest/api-model';
import { MatTableDataSource } from '@angular/material/table';
import { ANTHROPOMETRIC_DATA_COLUMNS, VITAL_SIGNS_COLUMNS } from '../../constants/anamnesis';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	public form: FormGroup;

	bloodTypes: MasterDataInterface<string>[];

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
			dataSource: new MatTableDataSource<any>([{
				heartRate: 10,
				respiratoryRate: 20,
				temperature: 3,
				bloodOxygenSaturation: 4,
				systolicBloodPressure: 5,
				diastolicBloodPressure: 6
			}])
		};
		this.datosAntropometricosTable = {
			columns: ANTHROPOMETRIC_DATA_COLUMNS,
			displayedColumns: ANTHROPOMETRIC_DATA_COLUMNS?.map(c => c.def),
			dataSource: new MatTableDataSource<any>([{
				bloodType: 'A+',
				height: '180',
				weight: '70',
				IMC: '30'
			}])
		};

		this.internacionMasterDataService.getBloodTypes().subscribe(bloodTypes => {
			this.bloodTypes = bloodTypes;
		});
	}

	save(): void {
		console.log('form: ', this.form);
	}

	back(): void {
		window.history.back();
	}

}
