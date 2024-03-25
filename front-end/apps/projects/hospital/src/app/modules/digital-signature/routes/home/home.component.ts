import { Component, OnInit } from '@angular/core';
import { AllergyConditionDto, AnthropometricDataDto, AppFeature, DiagnosisDto, DigitalSignatureDocumentDto, DocumentDto, DocumentObservationsDto, HealthConditionDto, HealthHistoryConditionDto, LoggedUserDto, MedicationDto, PageDto, PersonalHistoryDto, ProcedureDto, ReasonDto, RiskFactorDto } from '@api-rest/api-model.d';
import { DigitalSignatureService } from '@api-rest/services/digital-signature.service';
import { ItemListCard, ItemListOption, SelectableCardIds } from '@presentation/components/selectable-card/selectable-card.component';
import { DocumentService } from '@api-rest/services/document.service';
import { finalize } from 'rxjs';
import { AccountService } from '@api-rest/services/account.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { DetailedInformation } from '@presentation/components/detailed-information/detailed-information.component';
import { HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { getDocumentType } from '@core/constants/summaries';
import { URL_DOCUMENTS_SIGNATURE } from '../../../documents-signature/routes/home/home.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    documents: ItemListCard[] = [];
    digitalSignatureDocuments: DigitalSignatureDocumentDto[] = [];
    isLoading: boolean = true;
    selectedDocumentsId: number[] = [];
    hasProfessionalCuil: boolean = false;
    ROUTE_PREFIX: string;
    detailedInformation: DetailedInformation;
	readonly PAGE_SIZE = 5;
	elementsAmount: number;
    buttonBack = false;

    constructor(private readonly digitalSignature: DigitalSignatureService,
                private readonly documentService: DocumentService,
                private readonly account: AccountService,
                private readonly dialog: MatDialog,
                private readonly router: Router,
                private readonly contextService: ContextService,
                private readonly featureFlagService: FeatureFlagService) {
                    this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_DIGITAL).subscribe(isEnabledDigital =>{
                        this.featureFlagService.isActive(AppFeature.HABILITAR_FIRMA_CONJUNTA).subscribe(isEnabledConjunta =>{
                          if(isEnabledConjunta && isEnabledDigital){
                            this.buttonBack = true;
                          }
                        }) 
                      })
                }

    ngOnInit(): void {
        this.ROUTE_PREFIX = `institucion/${this.contextService.institutionId}/`;
        this.account.getInfo().subscribe((result: LoggedUserDto) => {
            if (result.cuil)
                return this.setDocuments();

            this.openDiscardWarningDialog(this.buildTextOption('digital-signature.dialogs.cuil.NO_CUIL', 'digital-signature.dialogs.cuil.CONTENT', 'buttons.ACCEPT'))
                .afterClosed()
                .subscribe((_) => {
                    this.router.navigate([this.ROUTE_PREFIX]);
            });
        });
    }

    setDocuments() {
		const INITIAL_PAGE = 0;
        this.hasProfessionalCuil = true;
        this.fetchData(INITIAL_PAGE);
    }

	handlePageEvent(event) {
		this.isLoading = true;
		this.fetchData(event.pageIndex);
	}

	private fetchData(pageIndex: number): void {
		this.digitalSignature.getPendingDocumentsByUser(pageIndex)
            .pipe(finalize(() => this.isLoading = false))
            .subscribe((documents: PageDto<DigitalSignatureDocumentDto>) => {
				if (!this.elementsAmount)
					this.elementsAmount = documents.totalElementsAmount;
                this.digitalSignatureDocuments = documents.content;
                this.buildItemListCard();
            });
	}

    selectedIds(ids: number[]) {
        this.selectedDocumentsId = ids;
    }

    downloadPdf() {
        this.selectedDocumentsId.forEach(selectedId => this.download({id: selectedId}));
    }

    sign() {
        if (!this.selectedDocumentsId.length) return;

        this.openDiscardWarningDialog(this.buildTextOption('digital-signature.dialogs.sign.TITLE', 'digital-signature.dialogs.sign.CONTENT', 'buttons.CONFIRM', 'buttons.CANCEL'))
            .afterClosed()
            .subscribe((result: boolean) => {
                if (result) {
                    this.digitalSignature.sign(this.selectedDocumentsId)
                        .subscribe((url: string) => window.open(url, "_blank"));
                }
            })

    }

    download(ids: SelectableCardIds) {
        this.documentService.downloadUnnamedFile(ids.id);
    }

    seeDetails(ids: SelectableCardIds) {
        this.documentService.getDocumentInfo(ids.id)
            .subscribe((document: DocumentDto) => this.buildDetailedInformation(document));
    }

    goToBackDocumentsSignature(){
        this.router.navigate([`${this.ROUTE_PREFIX}${URL_DOCUMENTS_SIGNATURE}`]);
      }

    private buildDetailedInformation(document: DocumentDto) {
        this.detailedInformation = {
            id: document.id,
            title: this.digitalSignatureDocuments.find(item => item.documentId === document.id).sourceTypeDto.description.toLocaleUpperCase(),
            oneColumn: [
                {
                    icon: 'medical_services',
                    title: 'digital-signature.detailed-documents.SPECIALTY',
                    value: document.clinicalSpecialtyName ? [document.clinicalSpecialtyName] : []
                },
                {
                    icon: 'sms_failed',
                    title: 'digital-signature.detailed-documents.REASON',
                    value: this.buildReasons(document.reasons)
                },
                {
                    icon: 'chat',
                    title: 'digital-signature.detailed-documents.CLINICAL_EVALUATION',
                    value: this.buildObservations(document.notes)
                },
                {
                    icon: 'error_outlined',
                    title: 'digital-signature.detailed-documents.DIAGNOSIS',
                    value: this.buildDiagnosis(document.diagnosis, document.mainDiagnosis)
                }
            ],
            twoColumns: [
                {
                    icon: 'cancel',
                    title: 'digital-signature.detailed-documents.ALLERGIES',
                    value: this.buildAllergies(document.allergies)
                },
                {
                    icon: 'report',
                    title: 'digital-signature.detailed-documents.FAMILY_HISTORIES',
                    value: this.buildFamilyHistories(document.familyHistories)
                },
                {
                    icon: 'event_available',
                    title: 'digital-signature.detailed-documents.MEDICATION',
                    value: this.buildMedications(document.medications)
                },
                {
                    icon: 'report_outlined',
                    title: 'digital-signature.detailed-documents.PERSONAL_HISTORIES',
                    value: this.buildPersonalHistories(document.personalHistories)
                },
                {
                    icon: 'library_add',
                    title: 'digital-signature.detailed-documents.PROCEDURES',
                    value: this.buildProcedures(document.procedures)
                },
                {
                    icon: 'favorite_outlined',
                    title: 'digital-signature.detailed-documents.RISK_FACTORS',
                    value: this.buildRiskFactors(document.riskFactors)
                },
            ],
            threeColumns: [
                {
                    icon: 'accessibility',
                    title: 'digital-signature.detailed-documents.ANTHROPOMETRIC_DATA',
                    value: this.buildAnthropometricData(document.anthropometricData)
                }
            ]
        }
    }

    private buildRiskFactors(riskFactors: RiskFactorDto) {
        const riskFactorsFiltered: string[] = [];
        if (riskFactors?.bloodGlucose)
            riskFactorsFiltered.push(`Glucemia: ${riskFactors.bloodGlucose.value}mg/dl`);
        if (riskFactors?.bloodOxygenSaturation)
            riskFactorsFiltered.push(`Saturación de oxígeno: ${riskFactors.bloodOxygenSaturation.value}%`);
        if (riskFactors?.cardiovascularRisk)
            riskFactorsFiltered.push(`Riesgo cardivascular: ${riskFactors.cardiovascularRisk.value}%`);
        if (riskFactors?.diastolicBloodPressure)
            riskFactorsFiltered.push(`Tension arterial diastólica: ${riskFactors.diastolicBloodPressure.value}mm`);
        if (riskFactors?.glycosylatedHemoglobin)
            riskFactorsFiltered.push(`Hemoglobina glicosilada: ${riskFactors.glycosylatedHemoglobin.value}%`);
        if (riskFactors?.heartRate)
            riskFactorsFiltered.push(`Frecuencia cardíaca: ${riskFactors.heartRate.value}/min`);
        if (riskFactors?.respiratoryRate)
            riskFactorsFiltered.push(`Frecuencia respiratoria: ${riskFactors.respiratoryRate.value}/min`);
        if (riskFactors?.systolicBloodPressure)
            riskFactorsFiltered.push(`Tension arterial sistólica: ${riskFactors.systolicBloodPressure.value}mm`);
        if (riskFactors?.temperature)
            riskFactorsFiltered.push(`Temperatura: ${riskFactors.temperature.value}°`);
        return riskFactorsFiltered;
    }

    private buildProcedures(procedures: ProcedureDto[]): string[] {
        const proceduresFiltered: string[] = [];
        procedures.forEach(p => {
            proceduresFiltered.push(p.snomed.pt);
            proceduresFiltered.push(new Date(p.performedDate).toLocaleDateString());
        })
        return proceduresFiltered;
    }

    private buildDiagnosis(diagnosis: DiagnosisDto[], mainDiagnosis: HealthConditionDto): string[] {
        const diagnosisFiltered: string[] = [];
        if (mainDiagnosis)
            diagnosisFiltered.push(`${mainDiagnosis.snomed.pt} - ${this.getVerification(mainDiagnosis.verificationId)} (Principal)`);
        diagnosis.forEach(d => {
            diagnosisFiltered.push(`${d.snomed.pt} - ${this.getVerification(d.verificationId)}`);
        })
        return diagnosisFiltered;
    }

    private getVerification(verificationId: string): string {
        let verification = 'Descartado';
        if (verificationId === HEALTH_VERIFICATIONS.CONFIRMADO)
            verification = 'Confirmado'
        if (verificationId === HEALTH_VERIFICATIONS.PRESUNTIVO)
            verification = 'Presuntivo';
        return verification;
    }

    private buildReasons(reasons: ReasonDto[]): string[] {
        const reasonsFiltered: string[] = reasons.map(r => r.snomed.pt);
        return reasonsFiltered;
    }

    private buildPersonalHistories(personalHistories: PersonalHistoryDto[]): string[] {
        const personalHistoriesFiltered: string[] = [];
        personalHistories.forEach(ph => {
            personalHistoriesFiltered.push(ph.snomed.pt);
            personalHistoriesFiltered.push(`Desde ${new Date(ph.startDate).toLocaleDateString()}`);
        })
        return personalHistoriesFiltered;
    }

    private buildObservations(observation: DocumentObservationsDto) {
        const observationsFiltered: string[] = [];
        if (observation?.clinicalImpressionNote)
            observationsFiltered.push(`Impresión clínica y plan: ${observation.clinicalImpressionNote}`);
        if (observation?.currentIllnessNote)
            observationsFiltered.push(`Enfermedad actual: ${observation.clinicalImpressionNote}`);
        if (observation?.evolutionNote)
            observationsFiltered.push(`Evolución: ${observation.evolutionNote}`);
        if (observation?.indicationsNote)
            observationsFiltered.push(`Indicaciones: ${observation.indicationsNote}`);
        if (observation?.otherNote)
            observationsFiltered.push(`Otras observaciones: ${observation.otherNote}`);
        if (observation?.physicalExamNote)
            observationsFiltered.push(`Examen físico: ${observation.physicalExamNote}`);
        if (observation?.studiesSummaryNote)
            observationsFiltered.push(`Resumen de estudios y procedimientos realizados: ${observation.studiesSummaryNote}`);
        return observationsFiltered;
    }

    private buildMedications(medications: MedicationDto[]): string[] {
        const medicationsFiltered: string[] = [];
        medications.forEach(m => {
            medicationsFiltered.push(m.snomed.pt);
            medicationsFiltered.push(m.note);
        })
        return medicationsFiltered;
    }

    private buildFamilyHistories(familyHistories: HealthHistoryConditionDto[]): string[] {
        const familyHistoriesFiltered: string[] = [];
        familyHistories.forEach(fh => {
            familyHistoriesFiltered.push(fh.snomed.pt);
            familyHistoriesFiltered.push(`Desde ${new Date(fh.startDate).toLocaleDateString()}`);
        })
        return familyHistoriesFiltered;
    }

    private buildAnthropometricData(anthropometricData: AnthropometricDataDto): string[] {
        const anthropometricDataFiltered: string[] = [];
        if (anthropometricData?.bloodType)
            anthropometricDataFiltered.push(`Grupo sanguineo: ${anthropometricData.bloodType.value}`);
        if (anthropometricData?.height)
            anthropometricDataFiltered.push(`Altura: ${anthropometricData.height.value}`);
        if (anthropometricData?.weight)
            anthropometricDataFiltered.push(`Peso: ${anthropometricData.weight.value}`);
        return anthropometricDataFiltered;
    }

    private buildAllergies(allergies: AllergyConditionDto[]): string[] {
        const allergiesFiltered: string[] = allergies.map(data => data.snomed.pt);
        return allergiesFiltered;
    }

    private buildTextOption(title: string, content: string, okButtonLabel: string, cancelButtonLabel?: string): TextDialog {
        return {
            title,
            content,
            cancelButtonLabel,
            okButtonLabel,
        }
    }


    private buildItemListCard() {
        this.documents = this.digitalSignatureDocuments.map(document => {
            return {
                id: document.documentId,
                icon: getDocumentType(document.documentTypeDto.id).icon,
                title: document.documentTypeDto.description,
                options: this.buildItemListOption(document)
            }
        })
    }

    private buildItemListOption(document: DigitalSignatureDocumentDto): ItemListOption[] {
        return [
            {
                title: 'digital-signature.card-information.PROBLEM',
                value: document.snomedConcepts.length ? this.buildValues(document) : ['digital-signature.card-information.NO_SNOMED_CONCEPT']
            },
            {
                title: 'digital-signature.card-information.CREATED',
                value: [`${new Date(document.createdOn).toLocaleString()}`],
            },
            {
                title: 'digital-signature.card-information.PROFESSIONAL',
                value: [document.professionalFullName]
            },
            {
                title: 'digital-signature.card-information.PATIENT',
                value: [document.patientFullName],
            },
            {
                title:  'digital-signature.card-information.ATTENTION_TYPE',
                value: [document.sourceTypeDto.description]
            },
        ]
    }

    private buildValues(document: DigitalSignatureDocumentDto) {
        return document.snomedConcepts.map(sc => {
            if (sc.isMainHealthCondition)
                return ` ${sc.pt}` + ' (Principal)'
            return ` ${sc.pt}`
        })
    }

    private openDiscardWarningDialog(options: TextDialog): MatDialogRef<DiscardWarningComponent> {
        return this.dialog.open(DiscardWarningComponent, {
            data: {
                title: options.title,
                content: options.content,
                okButtonLabel: options.okButtonLabel,
                cancelButtonLabel: options.cancelButtonLabel
            }
        })
    }
}

interface TextDialog {
    title: string,
    content: string,
    cancelButtonLabel?: string,
    okButtonLabel: string,
}
