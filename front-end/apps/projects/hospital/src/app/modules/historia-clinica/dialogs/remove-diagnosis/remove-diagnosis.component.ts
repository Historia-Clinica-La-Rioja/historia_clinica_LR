import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ConceptsSearchDialogComponent } from '../concepts-search-dialog/concepts-search-dialog.component';
import { HealthConditionDto, MasterDataInterface, EvolutionNoteDto, ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import {
	HEALTH_CLINICAL_STATUS,
	HEALTH_VERIFICATIONS
} from "@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids";

@Component({
	selector: 'app-remove-diagnosis',
	templateUrl: './remove-diagnosis.component.html',
	styleUrls: ['./remove-diagnosis.component.scss']
})
export class RemoveDiagnosisComponent implements OnInit {

	DESCARTADO = HEALTH_VERIFICATIONS.DESCARTADO;

	loading = false;
	removeForm: FormGroup;
	diagnosis: HealthConditionDto;

	verifications: MasterDataInterface<string>[];
	clinicalStatus: MasterDataInterface<string>[];

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { diagnosis: HealthConditionDto, internmentEpisodeId: number },
		public dialogRef: MatDialogRef<ConceptsSearchDialogComponent>,
		private internacionMasterDataService: InternacionMasterDataService,
		private evolutionNoteService: EvolutionNoteService,
		private snackBarService: SnackBarService,
		private formBuilder: FormBuilder,
	) {
		this.diagnosis = this.data.diagnosis;
	}

	ngOnInit(): void {
		this.removeForm = this.formBuilder.group({
			verificationId: [null, Validators.required],
			statusId: [null]
		});

		this.removeForm.controls.verificationId.valueChanges.subscribe(
			value => {
				if (value === this.DESCARTADO) {
					this.removeForm.controls.statusId.setValidators(Validators.required);
				} else {
					this.removeForm.controls.statusId.setValidators(null);
					this.removeForm.controls.statusId.setValue(HEALTH_CLINICAL_STATUS.INACTIVO);
				}
			}
		);

		this.internacionMasterDataService.getHealthClinicalDown().subscribe(healthClinical => {
			this.clinicalStatus = healthClinical;
		});

		this.internacionMasterDataService.getHealthVerificationDown().subscribe(healthVerification => {
			this.verifications = healthVerification;
		});
	}

	createEvolutionNote() {
		if (this.removeForm.valid) {
			this.loading = true;
			this.diagnosis.verificationId = this.removeForm.controls.verificationId.value;
			this.diagnosis.statusId = this.removeForm.controls.statusId.value;
			const evolutionNote: EvolutionNoteDto = {
				confirmed: true,
				diagnosis: [this.diagnosis],
			};
			this.evolutionNoteService.createDocument(evolutionNote, this.data.internmentEpisodeId).subscribe(
				(evolutionNoteResponse: ResponseEvolutionNoteDto) => {
					this.snackBarService.showSuccess('internaciones.nota-evolucion.messages.SUCCESS');
					this.dialogRef.close();
				},
				() => {
					this.loading = false;
					this.snackBarService.showError('internaciones.nota-evolucion.messages.ERROR');
				}
			);
		}
	}

}
