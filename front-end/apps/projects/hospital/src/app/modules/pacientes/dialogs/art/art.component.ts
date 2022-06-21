import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormGroupDirective, Validators } from "@angular/forms";
import { ARTCoverageDto, CoverageDto } from "@api-rest/api-model";
import { ArtCoverageService } from "@api-rest/services/art-coverage.service";
import {
	EMedicalCoverageType,
	MedicalCoverage,
	PatientMedicalCoverage
} from "@pacientes/dialogs/medical-coverage/medical-coverage.component";
import { MatOptionSelectionChange } from "@angular/material/core";
import { MatDialogRef } from "@angular/material/dialog";

@Component({
	selector: 'app-art',
	templateUrl: './art.component.html',
	styleUrls: ['./art.component.scss']
})
export class ArtComponent implements OnInit {

	artCoverageForm: FormGroup;
	artCoverageFilteredMasterData: ARTCoverageDto[];

	private artCoverageMasterData: ARTCoverageDto[];
	private artCoverageToAdd: MedicalCoverage;

	constructor(private formBuilder: FormBuilder,
				public dialogRef: MatDialogRef<ArtComponent>,
				public readonly artCoverageService: ArtCoverageService,) {
	}

	ngOnInit(): void {
		this.artCoverageService.getAll().subscribe((values: ARTCoverageDto[]) => {
			this.artCoverageFilteredMasterData = values;
			this.artCoverageMasterData = values;
		});

		this.artCoverageForm = this.formBuilder.group({
			coverage: [null, Validators.required],
			cuit: []
		});
		this.artCoverageForm.controls.cuit.disable();

		this.artCoverageForm.controls.coverage.valueChanges.subscribe((newValue: string) => {
			if (newValue) {
				this.artCoverageFilteredMasterData =
					this.artCoverageMasterData
						.filter(data => data.name.toLowerCase().includes(newValue.toLowerCase()));
			} else {
				this.artCoverageFilteredMasterData = this.artCoverageMasterData;
			}
		});
	}

	selectARTCoverage(event: MatOptionSelectionChange, coverage: CoverageDto): void {
		if (event.isUserInput) {
			this.artCoverageToAdd = this.fromARTCoverageMasterDataToMedicalCoverage(coverage);
			this.artCoverageForm.controls.cuit.setValue(this.artCoverageToAdd.cuit);
		}
	}

	private fromARTCoverageMasterDataToMedicalCoverage(coverage: CoverageDto): MedicalCoverage {
		const coverageId = this.artCoverageFilteredMasterData
			.filter((s: CoverageDto) => s.id === coverage.id)
			.map(s => s.id)[0];
		return new MedicalCoverage(coverageId, coverage.name, EMedicalCoverageType.ART, coverage.cuit);
	}

	addARTCoverage(): void {
		if (this.artCoverageToAdd && this.artCoverageForm.valid) {
			const art = this.getARTCoverageToAdd();
			this.dialogRef.close(art);
		}
	}

	private getARTCoverageToAdd(): PatientMedicalCoverage {
		const toAdd: PatientMedicalCoverage = {
			medicalCoverage: this.artCoverageToAdd,
			active: true
		};
		return toAdd;
	}

	closeModal(): void {
		this.dialogRef.close();
	}

}
