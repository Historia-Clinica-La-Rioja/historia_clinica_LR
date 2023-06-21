import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { AppFeature, InformerObservationDto, ProblemDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { SnvsMasterDataService } from '@api-rest/services/snvs-masterdata.service';
import { StudyAppointmentReportService } from '@api-rest/services/study-appointment-report.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { NewConsultationAddProblemFormComponent } from '@historia-clinica/dialogs/new-consultation-add-problem-form/new-consultation-add-problem-form.component';
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { map, Observable, of, take } from 'rxjs';
import { StudyAppointment } from '../../models/models';
import { toStudyAppointment } from '../../utils/mapper.utils';

@Component({
	selector: 'app-report-study',
	templateUrl: './report-study.component.html',
	styleUrls: ['./report-study.component.scss']
})
export class ReportStudyComponent implements OnInit {

	form: FormGroup;
	submitted = false;
	enabledEditing = true;
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	searchConceptsLocallyFFIsOn = false;
	problems: AmbulatoryConsultationProblem[];
	study: StudyAppointment;
	appointmentId: number;
	disableContinueEditing = false;
	severityTypes: any[];
	@Output() update = new EventEmitter<Observable<StudyAppointment>>;

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

		this.ambulatoryConsultationProblemsService.problems$.subscribe(p => this.problems = p);

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.ambulatoryConsultationProblemsService.setSeverityTypes(healthConditionSeverities);
		});
	}

	addProblem() {
		this.dialog.open(NewConsultationAddProblemFormComponent, {
			data: {
				ambulatoryConsultationProblemsService: this.ambulatoryConsultationProblemsService,
				severityTypes: this.severityTypes,
				searchConceptsLocallyFF: this.searchConceptsLocallyFFIsOn,
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	saveDraft() {
		this.submitted = true;
		if (this.form.valid && this.problems.length) {
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
		if (this.form.valid && this.problems.length) {
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

	private getInformerObservationsDto(confirmed: boolean): InformerObservationDto {
		return {
			id: this.study.info.informerObservations?.id,
			evolutionNote: this.replaceTagBr(this.form.value.observations),
			problems: this.ambulatoryConsultationProblemsService.getProblemas().map(
				(p: ProblemDto) => {
					return { severity: p.severity, chronic: p.chronic, endDate: p.endDate, snomed: p.snomed, startDate: p.startDate };
				}),
			createdBy: null,
			actionTime: null,
			confirmed,
		}
	}

	private replaceTagBr(observations: string): string {
		return observations.replace(new RegExp("<br>", "g"), "<br></br>");
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
		this.setConcepts(obs.problems);
	}

	private setConcepts(problems: ProblemDto[]) {
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

}
