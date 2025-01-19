import {
	MainDiagnosisDto,
	HealthConditionDto,
	SnomedDto,
} from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { MainDiagnosesService } from '@api-rest/services/main-diagnoses.service';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { UntypedFormGroup, UntypedFormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { MatSelectionList } from '@angular/material/list';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnomedService, SnomedSemanticSearch } from '@historia-clinica/services/snomed.service';
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { OVERLAY_DATA } from "@presentation/presentation-model";
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

@Component({
	selector: 'app-change-main-diagnosis-dock-popup',
	templateUrl: './change-main-diagnosis-dock-popup.component.html',
	styleUrls: ['./change-main-diagnosis-dock-popup.component.scss']
})
export class ChangeMainDiagnosisDockPopupComponent implements OnInit {

	@ViewChild(MatSelectionList) selection: MatSelectionList;

	newMainDiagnosis: HealthConditionDto;

	panelOpenState = true;
	form: UntypedFormGroup;
	currentMainDiagnosis: HealthConditionDto;
	diagnostics$: Observable<HealthConditionDto[]>;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly internmentStateService: InternmentStateService,
		private readonly snomedService: SnomedService,
		private readonly mainDiagnosesService: MainDiagnosesService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
	) { }

	ngOnInit(): void {

		this.internmentStateService.getMainDiagnosis(this.data.internmentEpisodeId).subscribe(
			mainDiagnosis => this.currentMainDiagnosis = mainDiagnosis
		);
		this.diagnostics$ = this.internmentStateService.getActiveAlternativeDiagnosesGeneralState(this.data.internmentEpisodeId);
		this.form = this.formBuilder.group({
			currentIllnessNote: [],
			physicalExamNote: [],
			studiesSummaryNote: [],
			evolutionNote: [],
			clinicalImpressionNote: [],
			otherNote: [],
		});
	}

	save(): void {
		if (this.form.valid && this.newMainDiagnosis) {
			this.openConfirmDialog().subscribe(accept => {
				if (accept) {
					const mainDiagnosisDto: MainDiagnosisDto = {
						mainDiagnosis: this.newMainDiagnosis,
						notes: this.form.value
					};
					this.mainDiagnosesService.addMainDiagnosis(this.data.internmentEpisodeId, mainDiagnosisDto).subscribe(
						_ => {
							this.snackBarService.showSuccess('internaciones.clinical-assessment-diagnosis.messages.SUCCESS');
							this.dockPopupRef.close(setFieldsToUpdate(mainDiagnosisDto));
						},
						_ => this.snackBarService.showError('internaciones.clinical-assessment-diagnosis.messages.ERROR')
					);
				}
			});
		}
		else {
			this.snackBarService.showError('internaciones.clinical-assessment-diagnosis.messages.WITHOUT_NEW_DIAGNOSIS');
		}

		function setFieldsToUpdate(mainDiagnosisDto: MainDiagnosisDto): InternmentFields {
			return {
				mainDiagnosis: !!mainDiagnosisDto.mainDiagnosis,
				diagnosis: !!mainDiagnosisDto.mainDiagnosis,
				evolutionClinical: !!mainDiagnosisDto.notes,
			}
		}
	}

	openConfirmDialog(): Observable<any> {
		const previousDiagnosisHTML = `<strong>${this.currentMainDiagnosis.snomed.pt}</strong>`;
		const newDiagnosisHTML = `<strong>${this.newMainDiagnosis.snomed.pt}</strong>`;
		const dialogRef = this.dialog.open(ConfirmDialogComponent, {
			data: {
				// TODO pasar a archivo de traducción
				title: 'Confirmar operación',
				content: `Se va a cambiar el diagnóstico principal ${previousDiagnosisHTML} por ${newDiagnosisHTML}`,
				okButtonLabel: 'Aceptar'
			}
		});

		return dialogRef.afterClosed();
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.DIAGNOSIS
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => {
					if (selectedConcept) {
						this.newMainDiagnosis = {
							snomed: selectedConcept
						};
					}
				});
		}
	}

	setMainDiagnosisFromList(): void {
		this.newMainDiagnosis = this.selection.selectedOptions.selected[0].value;
	}

	deleteMainDiagnosis(): void {
		delete this.newMainDiagnosis;
		if (this.selection) {
			this.selection.deselectAll();
		}
	}
}
