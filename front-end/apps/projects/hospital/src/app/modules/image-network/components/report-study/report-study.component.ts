import { Component, EventEmitter, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { AppFeature, ConclusionDto, InformerObservationDto, LoggedUserDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { SnvsMasterDataService } from '@api-rest/services/snvs-masterdata.service';
import { StudyAppointmentReportService } from '@api-rest/services/study-appointment-report.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { map, Observable, of, Subscription, switchMap, take } from 'rxjs';
import { StudyAppointment } from '../../models/models';
import { toStudyAppointment } from '../../utils/mapper.utils';
import { AddConclusionFormComponent } from '../../dialogs/add-conclusion-form/add-conclusion-form.component';
import { ConceptTypeaheadSearchComponent } from 'projects/hospital/src/app/modules/hsi-components/concept-typeahead-search/concept-typeahead-search.component';
import { SaveTemplateComponent, TemplateData } from '../../dialogs/save-template/save-template.component';
import { AccountService } from '@api-rest/services/account.service';
import { getParam } from '@historia-clinica/modules/ambulatoria/modules/estudio/utils/utils';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { ImportTemplateComponent } from '../../dialogs/import-template/import-template.component';
import { TemplateManagementService } from '../../services/template-management.service';
import { DeleteTemplateComponent } from '../../dialogs/delete-template/delete-template.component';

@Component({
	selector: 'app-report-study',
	templateUrl: './report-study.component.html',
	styleUrls: ['./report-study.component.scss']
})
export class ReportStudyComponent implements OnInit, OnDestroy {

	@ViewChild(ConceptTypeaheadSearchComponent) child:ConceptTypeaheadSearchComponent;
	form: FormGroup;
	submitted = false;
	enabledEditing = true;
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	searchConceptsLocallyFFIsOn = false;
	problems: AmbulatoryConsultationProblem[] = [];
	study: StudyAppointment;
	appointmentId: number;
	disableContinueEditing = false;
	severityTypes: any[];
	textEditorLength = 0;
	@Output() update = new EventEmitter<Observable<StudyAppointment>>;
	userInfo: LoggedUserDto
	institutionId = Number(getParam(this.route.snapshot,'id'))

	disableImportFiles = true
	subscriptionTemplate: Subscription

	constructor(
		private readonly route: ActivatedRoute,
		private readonly studyAppointmentReportService: StudyAppointmentReportService,
		private readonly formBuilder: FormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly snvsMasterDataService: SnvsMasterDataService,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly accountService: AccountService,
		private readonly templateManagementService: TemplateManagementService,
	) {
		this.ambulatoryConsultationProblemsService = new AmbulatoryConsultationProblemsService(formBuilder, this.snomedService, this.snackBarService, this.snvsMasterDataService, this.dialog);
	}

	ngOnInit() {
		this.route.paramMap.pipe(take(1)).subscribe((params) => {
			this.appointmentId = Number(params.get('id'));
			this.getStudy();
		});

		this.form = this.formBuilder.group({
			observations: [null, Validators.required]
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
			this.ambulatoryConsultationProblemsService.setSearchConceptsLocallyFF(isOn);
		});

		this.ambulatoryConsultationProblemsService.setShowInitialDate(false);
		this.ambulatoryConsultationProblemsService.problems$.subscribe(p => this.problems = p);

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.ambulatoryConsultationProblemsService.setSeverityTypes(healthConditionSeverities);
		});

		this.accountService.getInfo().pipe(take(1)).subscribe(user => this.userInfo = user);

		this.subscriptionTemplate = this.templateManagementService.existsImports()
		.subscribe(existsTemplates => this.disableImportFiles = !existsTemplates)


		this.form.controls.observations.valueChanges.subscribe(
			template => {
				if (template)
					this.setTextEditorLength(template)
			}
		)


	}

	addProblem() {
		this.dialog.open(AddConclusionFormComponent, {
			data: {
				ambulatoryConsultationProblemsService: this.ambulatoryConsultationProblemsService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
			restoreFocus: false
		});
	}

	addTypeaheadConclusion(event): void {
		if (event) {
			this.ambulatoryConsultationProblemsService.setConcept(event)
			this.ambulatoryConsultationProblemsService.addToList(null, true);
			this.ambulatoryConsultationProblemsService.getProblemas();
			this.child.clear();
		}
	  }

	updateEditing(){
		this.enabledEditing = true
	}

	saveDraft() {
		this.submitted = true;
		if (this.form.valid && this.textEditorLength) {
			this.setTextEditorLength(this.form.controls.observations.value)
			this.disableContinueEditing = true;
			this.enabledEditing = false;
			if (this.study.info.informerObservations?.id) {
				this.studyAppointmentReportService.updateDraftReport(this.appointmentId, this.getInformerObservationsDto(false)).subscribe({
					next: () => this.success(),
					error: () => this.error()
				});
			}
			else {
				this.studyAppointmentReportService.createDraftReport(this.appointmentId, this.getInformerObservationsDto(false)).subscribe({
					next: () => this.success(),
					error: () => this.error()
				});
			}
		}
	}

	save() {
		this.submitted = true;
		if (this.form.valid && this.textEditorLength) {
			this.setTextEditorLength(this.form.controls.observations.value)
			this.disableContinueEditing = true;
			this.enabledEditing = false;
			if (this.study.info.informerObservations?.id) {
				this.studyAppointmentReportService.closeDraftReport(this.appointmentId, this.getInformerObservationsDto(true)).subscribe({
					next: () => this.success(),
					error: () => this.error()
				});
			}
			else {
				this.studyAppointmentReportService.saveReport(this.appointmentId, this.getInformerObservationsDto(true)).subscribe({
					next: () => this.success(),
					error: () => this.error(),
				});
			}
		}
	}

	private setTextEditorLength(text: string) {
		this.textEditorLength = text.replace(/<[^>]*>/g, "").replace(/\s/g, "").length;
	}

	private getInformerObservationsDto(confirmed: boolean): InformerObservationDto {
		return {
			id: this.study.info.informerObservations?.id,
			evolutionNote: this.replaceTagBr(this.form.value.observations, confirmed),
			conclusions: this.ambulatoryConsultationProblemsService.getProblemas().map(
				(p: ConclusionDto) => {
					return { snomed: { pt: this.uppercaseFirstLetter(p.snomed.pt), sctid: p.snomed.sctid } };
				}),
			createdBy: null,
			actionTime: null,
			confirmed,
		}
	}

	private uppercaseFirstLetter(word: string) {
		return word.charAt(0).toUpperCase() + word.slice(1);
	}

	private replaceTagBr(observations: string, confirmed: boolean): string {
		return confirmed ? observations.replace(new RegExp("<br>", "g"), "<br></br>") : observations;
	}

	private getStudy() {
		this.studyAppointmentReportService.getStudyByAppointmentId(this.appointmentId)
			.pipe(map(study => toStudyAppointment(study)))
			.subscribe(s => {
				if (s.info.informerObservations)
					this.setPreviousInformation(s.info.informerObservations);
				this.study = s;
				if (s.info.informerObservations?.confirmed)
					this.update.emit(of(s));
			});
	}

	private setPreviousInformation(obs: InformerObservationDto) {
		this.enabledEditing = false;
		this.form.controls.observations.setValue(obs.evolutionNote);
		this.setConcepts(obs.conclusions);
	}

	private setConcepts(problems: ConclusionDto[]) {
		problems.forEach(p => this.ambulatoryConsultationProblemsService.addProblemToList({ snomed: p.snomed }))
	}

	private success() {
		this.snackBarService.showSuccess('image-network.worklist.details_study.SNACKBAR_SUCCESS');
		this.getStudy();
		this.disableContinueEditing = false;
	}

	private error() {
		this.snackBarService.showError('image-network.worklist.details_study.SNACKBAR_ERROR');
		this.disableContinueEditing = false;
	}

	openDeleteTemplate() {
		this.dialog.open(DeleteTemplateComponent, {
			autoFocus: false,
			width: '45%',
			disableClose: true,
			restoreFocus: false,
			height: '45%'
		}).afterClosed().subscribe(
			sucess => {
				if (sucess)
					this.snackBarService.showSuccess('image-network.worklist.details_study.SNACKBAR_SUCCESS_DELETE_TEMPLATE')
			}
		);
	}

	openSaveTemplate() {
		const data: TemplateData = {
			textReportInformer: this.form.get('observations').value,
		}
		this.dialog.open(SaveTemplateComponent, {
			data,
			autoFocus: false,
			width: '35%',
			disableClose: true,
			restoreFocus: false
		});
	}


	openTemplateManagementByForm() {
		let finalDialogRef: Observable<string>
		finalDialogRef = this.form.valid ? this.openImportTemplateWithWarning() : this.openDialogTemplateImportList()
		finalDialogRef.subscribe(
			template => {
				if (template) {
					this.snackBarService.showSuccess('image-network.worklist.details_study.SNACKBAR_SUCCESS_IMPORT');
					this.form.controls.observations.setValue(template)
				}
			})
	}

	private openImportTemplateWithWarning(): Observable<string>  {
			const dialogRefConfirmation = this.dialog.open(DiscardWarningComponent, {
				data: {
					title: 'image-network.worklist.details_study.TEMPLATE_CONFIRM_TITLE',
					content: 'image-network.worklist.details_study.TEMPLATE_CONFIRM_CONTENT',
					okButtonLabel: 'image-network.worklist.details_study.TEMPLATE_CONFIRM_BUTTON_OK',
					cancelButtonLabel: 'image-network.worklist.details_study.TEMPLATE_CONFIRM_BUTTON_CANCEL',
				},
				autoFocus: false,
				width: '35%',
				disableClose: true,
				restoreFocus: false
			})
			return dialogRefConfirmation.afterClosed()
				.pipe(switchMap(confirm => confirm ? this.openDialogTemplateImportList() : of(null)))
	}

	private openDialogTemplateImportList(): Observable<string> {
		const dialogReference: Observable<string> = this.dialog.open(ImportTemplateComponent, {
			autoFocus: false,
			width: '45%',
			disableClose: true,
			restoreFocus: false,
			height: '45%'
		}).afterClosed()
		return dialogReference
	}

	ngOnDestroy(): void {
		this.templateManagementService.clearTemplateManagement()
		this.subscriptionTemplate.unsubscribe()
	}

}
