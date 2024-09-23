import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { HealthConditionDto, DiagnosisDto, InternmentEpisodeProcessDto, AnamnesisSummaryDto, EpicrisisSummaryDto, EvaluationNoteSummaryDto, DocumentHistoricDto, DocumentSearchDto, ResponseEmergencyCareDto, AppFeature, AnestheticReportSummaryDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { ReferenceService } from '@api-rest/services/reference.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { ReferenceNotificationService } from '@historia-clinica/services/reference-notification.service';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { combineLatest, Observable, take } from 'rxjs';
import { REFERENCE_CONSULTATION_TYPE } from '../../constants/reference-masterdata';
import { NuevaConsultaDockPopupEnfermeriaComponent } from '../../dialogs/nueva-consulta-dock-popup-enfermeria/nueva-consulta-dock-popup-enfermeria.component';
import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { HEALTH_VERIFICATIONS } from '../../modules/internacion/constants/ids';
import { InternmentActionsService } from '../../modules/internacion/services/internment-actions.service';
import { InternmentFields, InternmentSummaryFacadeService } from '../../modules/internacion/services/internment-summary-facade.service';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { HistoricalProblemsFacadeService } from '../../services/historical-problems-facade.service';
import { MedicacionesService } from '../../services/medicaciones.service';
import { PatientAllergiesService } from '../../services/patient-allergies.service';
import { DocumentActionsService } from '../../modules/internacion/services/document-actions.service';
import { DeleteDocumentActionService } from '../../modules/internacion/services/delete-document-action.service';
import { EditDocumentActionService } from '../../modules/internacion/services/edit-document-action.service';
import { TriageDefinitionsService } from '@historia-clinica/modules/guardia/services/triage-definitions.service';
import { EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { PatientType } from '@historia-clinica/constants/summaries';
import { NotaDeEvolucionDockPopupComponent } from '@historia-clinica/components/nota-de-evolucion-dock-popup/nota-de-evolucion-dock-popup.component';
import { EmergencyCareStateChangedService } from '../../services/emergency-care-state-changed.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-clinical-history-actions',
	templateUrl: './clinical-history-actions.component.html',
	styleUrls: ['./clinical-history-actions.component.scss'],
	providers: [DocumentActionsService, DeleteDocumentActionService, EditDocumentActionService, TriageDefinitionsService]
})
export class ClinicalHistoryActionsComponent implements OnInit {

	readonly EstadosEpisodio = EstadosEpisodio;

	dialogRef: DockPopupRef;
	notaDeEvolucionDialogRef: DockPopupRef;
	mainDiagnosis: HealthConditionDto;
	diagnosticos: DiagnosisDto[];
	referenceNotificationService: ReferenceNotificationService;
	hasNewConsultationEnabled$: Observable<boolean>;
	currentUserIsAllowedToMakeBothConsultation = false;
	PRESUMPTIVE = HEALTH_VERIFICATIONS.PRESUNTIVO;
	anamnesisDoc: AnamnesisSummaryDto;
	epicrisisDoc: EpicrisisSummaryDto;
	anestheticDoc: AnestheticReportSummaryDto;
	lastEvolutionNoteDoc: EvaluationNoteSummaryDto;
	hasMedicalDischarge: boolean;
	hasInternmentEpisodeInThisInstitution = false;
	hasMedicalRole = false;
	hasAdministrativeRole = false;
	hasRoleAbleToSeeTriage: boolean;
	hasInternmentActionsToDo = true;
	internmentEpisode: InternmentEpisodeProcessDto;
	documentEpicrisisDraft: DocumentSearchDto;
	documentAnestheticPartDraft: DocumentSearchDto;
	enableReports = false

	isEmergencyCareTemporaryPatient = false;
	isAnestheticReportEnabledFF: boolean;
	isAdministrativeAndHasTriageFFInFalse: boolean;

	@Input() patientId: number;
	@Input()
	set internmentEpisodeProcess(episode: InternmentEpisodeProcessDto) {
		this.internmentEpisode = episode;
		if (episode?.inProgress) {
			this.hasInternmentEpisodeInThisInstitution = episode.inProgress && !!episode.id;
			if (this.hasInternmentEpisodeInThisInstitution)
				this.setInternmentInformation(episode.id);
		}
		else
			this.hasInternmentActionsToDo = false;
	}
	triageComponent;
	episode: ResponseEmergencyCareDto;
	anyEmergencyCareAction = false;
	@Input() set emergencyCareEpisode(emergencyCareEpisode: ResponseEmergencyCareDto) {
		if (emergencyCareEpisode) {
			this.episode = emergencyCareEpisode;
			this.emergencyCareStateChangedService.emergencyCareStateChanged$.subscribe(state => {
				this.episode.emergencyCareState.id = state;
			});
			this.emergencyCareStateChangedService.emergencyCareStateChanged(this.episode.emergencyCareState.id);
			this.isEmergencyCareTemporaryPatient = emergencyCareEpisode.patient.typeId === PatientType.EMERGENCY_CARE_TEMPORARY;
			this.anyEmergencyCareAction = emergencyCareEpisode?.emergencyCareState?.id !== EstadosEpisodio.CON_ALTA_MEDICA;
			this.triageDefinitionsService.getTriagePath(emergencyCareEpisode.emergencyCareType?.id)
				.subscribe(
					({ component }) => {
						this.triageComponent = component
					}
				)

		}
	}

	@Output() popUpOpen = new EventEmitter<DockPopupRef>();

	constructor(
		private readonly dockPopupService: DockPopupService,
		private readonly dialog: MatDialog,
		private readonly patientAllergies: PatientAllergiesService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly referenceService: ReferenceService,
		readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly medicacionesService: MedicacionesService,
		private readonly permissionsService: PermissionsService,
		private readonly internmentStateService: InternmentStateService,
		private readonly historialProblemsFacadeService: HistoricalProblemsFacadeService,
		readonly internmentActions: InternmentActionsService,
		private readonly documentActions: DocumentActionsService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly emergencyCareStateChangedService: EmergencyCareStateChangedService,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_PARTE_ANESTESICO_EN_DESARROLLO).subscribe(isEnabled =>
			this.isAnestheticReportEnabledFF = isEnabled
		);
	}

	ngOnInit(): void {

		this.hasNewConsultationEnabled$ = this.ambulatoriaSummaryFacadeService.hasNewConsultationEnabled$;

		this.setRoles();

		const refNotificationInfo = { patientId: this.patientId, consultationType: REFERENCE_CONSULTATION_TYPE.AMBULATORY };
		this.referenceNotificationService = new ReferenceNotificationService(refNotificationInfo, this.referenceService, this.dialog, this.clinicalSpecialtyService, this.medicacionesService, this.ambulatoriaSummaryFacadeService, this.dockPopupService);

		this.referenceNotificationService.getOpenConsultation().subscribe(type => {
			if (type === REFERENCE_CONSULTATION_TYPE.AMBULATORY)
				this.openNuevaConsulta();
		});

		this.internmentActions.medicalDischarge$.subscribe(medicalDischarge => {
			if (medicalDischarge)
				this.internmentSummaryFacadeService.updateInternmentEpisode();
		});

		this.internmentActions.dialogRef$.subscribe(dialogRef => this.popUpOpen.next(dialogRef));

		this.checkAdministrativeFF();
	}

	private checkAdministrativeFF() {
		this.featureFlagService.isActive(AppFeature.HABILITAR_TRIAGE_PARA_ADMINISTRATIVO).subscribe(isEnabled =>
			this.isAdministrativeAndHasTriageFFInFalse = this.hasRoleAbleToSeeTriage ? false : (!isEnabled && this.hasAdministrativeRole)
		)
	}

	setInternmentInformation(internmentId: number) {
		this.internmentSummaryFacadeService.setInternmentEpisodeInformation(internmentId, false, true);
		this.internmentSummaryFacadeService.clearInternment();
		const anamnesis$ = this.internmentSummaryFacadeService.anamnesis$;
		const evolutionNote$ = this.internmentSummaryFacadeService.evolutionNote$;
		const epicrisis$ = this.internmentSummaryFacadeService.epicrisis$;
		const anestheticPart$ = this.internmentSummaryFacadeService.anestheticPart$;
		const hasMedicalDischarge$ = this.internmentSummaryFacadeService.hasMedicalDischarge$;

		combineLatest([anamnesis$, evolutionNote$, epicrisis$, hasMedicalDischarge$, anestheticPart$]).subscribe(
			([anamnesis, evolutionNote, epicrisis, medicalDischarge, anestheticPart]) => {
				this.anamnesisDoc = anamnesis;
				this.lastEvolutionNoteDoc = evolutionNote;
				this.epicrisisDoc = epicrisis;
				this.anestheticDoc = anestheticPart
				this.hasMedicalDischarge = medicalDischarge;
				this.hasInternmentActionsToDo = true;
				this.enableReports = false;
				this.hasToDoInternmentAction();
				if (this.epicrisisDoc?.confirmed === false)
					this.getEpicrisisDraft();
				if (this.anestheticDoc?.confirmed === false)
					this.getAnestheticPartDraft()
			});
	}

	setRoles() {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.currentUserIsAllowedToMakeBothConsultation = (anyMatch<ERole>(userRoles, [ERole.ENFERMERO]) &&
				(anyMatch<ERole>(userRoles, [ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO])))
			this.hasMedicalRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO]);
			this.hasAdministrativeRole = anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES]);
			const proffesionalRoles: ERole[] = [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
			this.hasRoleAbleToSeeTriage = userRoles.some(role => proffesionalRoles.includes(role));
		});
	}

	openNotaDeEvolucion() {
		if (!this.notaDeEvolucionDialogRef) {
			this.notaDeEvolucionDialogRef = this.dockPopupService.open(NotaDeEvolucionDockPopupComponent, { patientId: this.patientId, episodeId: this.episode.id });
			this.popUpOpen.next(this.notaDeEvolucionDialogRef);
			this.notaDeEvolucionDialogRef.afterClosed().subscribe(_ => {
				delete this.notaDeEvolucionDialogRef;
				this.popUpOpen.next(this.notaDeEvolucionDialogRef);
			})
		} else {
			if (this.notaDeEvolucionDialogRef.isMinimized())
				this.notaDeEvolucionDialogRef.maximize();
		}
	}

	openNuevaConsulta() {
		if (!this.dialogRef) {
			this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(true);
			this.dialogRef = this.dockPopupService.open(NuevaConsultaDockPopupComponent, { idPaciente: this.patientId });
			this.popUpOpen.next(this.dialogRef);
			this.dialogRef.afterClosed().pipe(take(1)).subscribe(fieldsToUpdate => {
				delete this.dialogRef;
				this.popUpOpen.next(this.dialogRef);

				if (fieldsToUpdate) {
					this.medicacionesService.updateMedication();
					this.historialProblemsFacadeService.loadEvolutionSummaryList(this.patientId);
					this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
				if (this.internmentEpisode?.inProgress && this.internmentEpisode?.id) {
					if (fieldsToUpdate?.allergies)
						this.internmentSummaryFacadeService.unifyAllergies(this.patientId);
					if (fieldsToUpdate?.familyHistories)
						this.internmentSummaryFacadeService.unifyFamilyHistories(this.patientId);
				}
				if (fieldsToUpdate?.allergies)
					this.patientAllergies.updateCriticalAllergies(this.patientId);
				this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(false);
			});
		} else {
			if (this.dialogRef.isMinimized())
				this.dialogRef.maximize();
		}
	}

	openNuevaConsultaEnfermeria() {
		if (!this.dialogRef) {
			this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(true);
			this.dialogRef = this.dockPopupService.open(NuevaConsultaDockPopupEnfermeriaComponent, { idPaciente: this.patientId });
			this.popUpOpen.next(this.dialogRef);
			this.dialogRef.afterClosed().pipe(take(1)).subscribe(fieldsToUpdate => {
				delete this.dialogRef;
				this.popUpOpen.next(this.dialogRef);
				if (fieldsToUpdate) {
					this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
					this.medicacionesService.updateMedication();
					this.historialProblemsFacadeService.loadEvolutionSummaryList(this.patientId);
				}
				this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(false);
			});
		} else {
			if (this.dialogRef.isMinimized())
				this.dialogRef.maximize();
		}
	}

	openAnamnesis() {
		this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisode.id).subscribe(diagnoses => {
			diagnoses.forEach(modifiedDiagnosis => modifiedDiagnosis.presumptive = modifiedDiagnosis.verificationId === this.PRESUMPTIVE);
			this.internmentActions.mainDiagnosis = diagnoses.filter(diagnosis => diagnosis.main)[0];
			if (this.internmentActions.mainDiagnosis)
				this.internmentActions.mainDiagnosis.isAdded = true;
			this.internmentActions.diagnosticos = diagnoses.filter(diagnosis => !diagnosis.main);
			this.internmentActions.openAnamnesis();
			this.internmentActions.anamnesis$.subscribe(fieldsToUpdate => {
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		})
	}

	openEvolutionNote() {
		this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisode.id).subscribe(diagnoses => {
			diagnoses.forEach(modifiedDiagnosis => modifiedDiagnosis.presumptive = modifiedDiagnosis.verificationId === this.PRESUMPTIVE);
			this.internmentActions.mainDiagnosis = diagnoses.filter(diagnosis => diagnosis.main)[0];
			if (this.internmentActions.mainDiagnosis)
				this.internmentActions.mainDiagnosis.isAdded = true;
			this.internmentActions.diagnosticos = diagnoses.filter(diagnosis => !diagnosis.main);
			this.internmentActions.openEvolutionNote();
			this.internmentActions.evolutionNote$.subscribe(fieldsToUpdate => {
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		})
	}

	openEpicrisis() {
		this.internmentActions.openEpicrisis();
		this.internmentActions.epicrisis$.subscribe(fieldsToUpdate => {
			if (fieldsToUpdate)
				this.updateInternmentSummary(fieldsToUpdate);
		});
	}

	openMedicalDischarge() {
		this.internmentActions.openMedicalDischarge();
	}

	openEpicrisisDraft() {
		this.documentActions.editEpicrisisDraft(this.documentEpicrisisDraft);
	}

	openAnestheticReportDraft() {
		this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisode.id).subscribe(diagnoses => {
			diagnoses.forEach(modifiedDiagnosis => modifiedDiagnosis.presumptive = modifiedDiagnosis.verificationId === this.PRESUMPTIVE);
			this.internmentActions.mainDiagnosis = diagnoses.filter(diagnosis => diagnosis.main)[0];
			if (this.internmentActions.mainDiagnosis)
				this.internmentActions.mainDiagnosis.isAdded = true;
			this.internmentActions.diagnosticos = diagnoses.filter(diagnosis => !diagnosis.main);
			this.documentActions.editAnestheticPartDraft(this.documentAnestheticPartDraft)
			this.internmentActions.anestheticReport$.subscribe(fieldsToUpdate => {
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		})
	}

	openAnestheticReport() {
		this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisode.id).subscribe(diagnoses => {
			diagnoses.forEach(modifiedDiagnosis => modifiedDiagnosis.presumptive = modifiedDiagnosis.verificationId === this.PRESUMPTIVE);
			this.internmentActions.mainDiagnosis = diagnoses.filter(diagnosis => diagnosis.main)[0];
			if (this.internmentActions.mainDiagnosis)
				this.internmentActions.mainDiagnosis.isAdded = true;
			this.internmentActions.diagnosticos = diagnoses.filter(diagnosis => !diagnosis.main);
			this.internmentActions.openAnestheticReport()
			this.internmentActions.anestheticReport$.subscribe(fieldsToUpdate => {
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		})
	}

	openSurgicalReport() {
		this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisode.id).subscribe(diagnoses => {
			diagnoses.forEach(modifiedDiagnosis => modifiedDiagnosis.presumptive = modifiedDiagnosis.verificationId === this.PRESUMPTIVE);
			this.internmentActions.mainDiagnosis = diagnoses.filter(diagnosis => diagnosis.main)[0];
			if (this.internmentActions.mainDiagnosis)
				this.internmentActions.mainDiagnosis.isAdded = true;
			this.internmentActions.diagnosticos = diagnoses.filter(diagnosis => !diagnosis.main);
			this.internmentActions.openSurgicalReport();
			this.internmentActions.surgicalReport$.subscribe(fieldsToUpdate => {
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		})
	}

	newTriage() {
		this.dialog.open(this.triageComponent, { autoFocus: false, disableClose: true, data: this.episode.id })
	}

	openEvolutionNoteByNurse() {
		//to do
	}

	private hasToDoInternmentAction() {
		if (this.hasMedicalDischarge) {
			this.hasInternmentActionsToDo = false;
			this.enableReports = true
			return;
		}
		if (this.epicrisisDoc?.confirmed && !this.hasMedicalRole) {
			this.hasInternmentActionsToDo = false;
			return;
		}
		if (!this.epicrisisDoc?.confirmed && !this.hasMedicalRole) {
			this.hasInternmentActionsToDo = true;
			return;
		}
		if (this.anestheticDoc?.confirmed && !this.hasMedicalRole) {
			this.hasInternmentActionsToDo = false;
			return;
		}
		if (!this.anestheticDoc?.confirmed && !this.hasMedicalRole) {
			this.hasInternmentActionsToDo = true;
			return;
		}
		this.hasInternmentActionsToDo = true;
	}

	private updateInternmentSummary(fieldsToUpdate: InternmentFields): void {
		const fields = {
			personalHistories: fieldsToUpdate?.personalHistories,
			riskFactors: fieldsToUpdate?.riskFactors,
			medications: fieldsToUpdate?.medications,
			heightAndWeight: fieldsToUpdate?.heightAndWeight,
			bloodType: fieldsToUpdate?.bloodType,
			immunizations: fieldsToUpdate?.immunizations,
			mainDiagnosis: fieldsToUpdate?.mainDiagnosis,
			diagnosis: fieldsToUpdate?.diagnosis,
			evolutionClinical: fieldsToUpdate?.evolutionClinical
		}
		this.internmentSummaryFacadeService.setFieldsToUpdate(fields);
		if (fieldsToUpdate?.familyHistories)
			this.internmentSummaryFacadeService.unifyFamilyHistories(this.patientId);
		if (fieldsToUpdate?.allergies) {
			this.patientAllergies.updateCriticalAllergies(this.patientId);
			this.internmentSummaryFacadeService.unifyAllergies(this.patientId);
		}
		this.internmentSummaryFacadeService.updateInternmentEpisode();
	}

	private getEpicrisisDraft() {
		this.internmentSummaryFacadeService.clinicalEvaluation$.subscribe((documentHistoric: DocumentHistoricDto) => {
			if (documentHistoric?.documents?.length)
				this.documentEpicrisisDraft = documentHistoric.documents.find(document => document.documentType === "Epicrisis" && !document.confirmed);
		});
	}

	private getAnestheticPartDraft() {
		this.internmentSummaryFacadeService.clinicalEvaluation$.subscribe((documentHistoric: DocumentHistoricDto) => {
			if (documentHistoric?.documents?.length)
				this.documentAnestheticPartDraft = documentHistoric.documents.find(document => document.documentType === "Parte anestésico" && !document.confirmed);
		});
	}


}
