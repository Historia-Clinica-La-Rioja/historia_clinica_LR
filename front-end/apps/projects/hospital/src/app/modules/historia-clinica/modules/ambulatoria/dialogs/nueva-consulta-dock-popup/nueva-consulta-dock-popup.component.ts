import { Component, Inject, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnomedService } from '../../../../services/snomed.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MotivoNuevaConsultaService } from '../../services/motivo-nueva-consulta.service';
import { Medicacion, MedicacionesNuevaConsultaService } from '../../services/medicaciones-nueva-consulta.service';
import { Problema } from '../../../../services/problemas.service';
import { ProcedimientosService } from '../../../../services/procedimientos.service';
import { DatosAntropometricosNuevaConsultaService } from '../../services/datos-antropometricos-nueva-consulta.service';
import { SignosVitalesNuevaConsultaService } from '../../services/signos-vitales-nueva-consulta.service';
import {
	AntecedenteFamiliar,
	AntecedentesFamiliaresNuevaConsultaService
} from '../../services/antecedentes-familiares-nueva-consulta.service';
import { Alergia, AlergiasNuevaConsultaService } from '../../services/alergias-nueva-consulta.service';
import { DateFormat, dateToMomentTimeZone, momentFormat, newMoment } from '@core/utils/moment.utils';
import { AppFeature, ClinicalSpecialtyDto, CreateOutpatientDto, HealthConditionNewConsultationDto, OutpatientProblemDto, SnvsToReportDto } from '@api-rest/api-model.d';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { OutpatientConsultationService } from '@api-rest/services/outpatient-consultation.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HealthConditionService } from '@api-rest/services/healthcondition.service';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { MatDialog } from '@angular/material/dialog';
import { SuggestedFieldsPopupComponent } from '../../../../../presentation/components/suggested-fields-popup/suggested-fields-popup.component';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { NewConsultationSuggestedFieldsService } from '../../services/new-consultation-suggested-fields.service';
import { TranslateService } from '@ngx-translate/core';
import { MIN_DATE } from "@core/utils/date.utils";
import { AmbulatoryConsultationProblem, AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AmbulatoryConsultationReferenceService, Reference } from '../../services/ambulatory-consultation-reference.service';
import { CareLineService } from '@api-rest/services/care-line.service';

import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { DatePipe } from '@angular/common';
import { PreviousDataComponent } from '../previous-data/previous-data.component';
import { forkJoin, Observable, of } from 'rxjs';
import { ClinicalSpecialtyCareLineService } from '@api-rest/services/clinical-specialty-care-line.service';
import { SnvsMasterDataService } from "@api-rest/services/snvs-masterdata.service";
import { ReferenceFileService } from '@api-rest/services/reference-file.service';
import { SnvsReportsResultComponent } from '../snvs-reports-result/snvs-reports-result.component';
import { HCEPersonalHistory } from '../reference/reference.component';

const TIME_OUT = 5000;

@Component({
	selector: 'app-nueva-consulta-dock-popup',
	templateUrl: './nueva-consulta-dock-popup.component.html',
	styleUrls: ['./nueva-consulta-dock-popup.component.scss']
})
export class NuevaConsultaDockPopupComponent implements OnInit {
	disableConfirmButton = false;
	formEvolucion: FormGroup;
	errores: string[] = [];
	motivoNuevaConsultaService: MotivoNuevaConsultaService;
	medicacionesNuevaConsultaService: MedicacionesNuevaConsultaService;
	ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService;
	procedimientoNuevaConsultaService: ProcedimientosService;
	datosAntropometricosNuevaConsultaService: DatosAntropometricosNuevaConsultaService;
	signosVitalesNuevaConsultaService: SignosVitalesNuevaConsultaService;
	antecedentesFamiliaresNuevaConsultaService: AntecedentesFamiliaresNuevaConsultaService;
	alergiasNuevaConsultaService: AlergiasNuevaConsultaService;
	readOnlyProblema = false;
	apiErrors: string[] = [];
	public today = newMoment();
	fixedSpecialty = true;
	defaultSpecialty: ClinicalSpecialtyDto;
	specialties: ClinicalSpecialtyDto[];
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	public hasError = hasError;
	severityTypes: any[];
	criticalityTypes: any[];
	minDate = MIN_DATE;
	public ffIsOn: boolean;
	ambulatoryConsultationReferenceService: AmbulatoryConsultationReferenceService;

	constructor(
		@Inject(OVERLAY_DATA) public data: NuevaConsultaData,
		public dockPopupRef: DockPopupRef,
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly outpatientConsultationService: OutpatientConsultationService,
		private readonly snackBarService: SnackBarService,
		private readonly healthConditionService: HealthConditionService,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly dialog: MatDialog,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly translateService: TranslateService,
		private readonly snvsMasterDataService: SnvsMasterDataService,
		private readonly datePipe: DatePipe,
		private readonly featureFlagService: FeatureFlagService,
		private readonly careLineService: CareLineService,
		private readonly clinicalSpecialtyCareLine: ClinicalSpecialtyCareLineService,
		private readonly referenceFileService: ReferenceFileService,
	) {
		this.motivoNuevaConsultaService = new MotivoNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.medicacionesNuevaConsultaService = new MedicacionesNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.ambulatoryConsultationProblemsService = new AmbulatoryConsultationProblemsService(formBuilder, this.snomedService, this.snackBarService, this.snvsMasterDataService, this.dialog);
		this.procedimientoNuevaConsultaService = new ProcedimientosService(formBuilder, this.snomedService, this.snackBarService);
		this.datosAntropometricosNuevaConsultaService =
			new DatosAntropometricosNuevaConsultaService(formBuilder, this.hceGeneralStateService, this.data.idPaciente, this.internacionMasterDataService, this.datePipe);
		this.signosVitalesNuevaConsultaService = new SignosVitalesNuevaConsultaService(formBuilder, this.hceGeneralStateService, this.data.idPaciente, this.datePipe);
		this.antecedentesFamiliaresNuevaConsultaService = new AntecedentesFamiliaresNuevaConsultaService(formBuilder, this.snomedService);
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService);
		this.ambulatoryConsultationReferenceService = new AmbulatoryConsultationReferenceService(this.dialog, this.data, this.ambulatoryConsultationProblemsService, this.clinicalSpecialtyCareLine, this.careLineService);
	}

	setProfessionalSpecialties() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe(specialties => {
			this.setSpecialtyFields(specialties, false);
		});
	}

	setSpecialtyFields(specialtyArray, fixedSpecialty) {
		this.specialties = specialtyArray;
		this.fixedSpecialty = fixedSpecialty;
		this.defaultSpecialty = specialtyArray[0];
		this.formEvolucion.get('clinicalSpecialty').setValue(this.defaultSpecialty);
		this.formEvolucion.controls['clinicalSpecialty'].markAsTouched();
	}

	ngOnInit(): void {

		this.setProfessionalSpecialties();

		if (this.data.idProblema) {
			this.readOnlyProblema = true;
			this.healthConditionService.getHealthCondition(this.data.idProblema).subscribe(p => {
				this.ambulatoryConsultationProblemsService.addProblemToList(this.buildProblema(p));
			});
		}

		this.formEvolucion = this.formBuilder.group({
			evolucion: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			clinicalSpecialty: [null, [Validators.required]],
		});

		this.datosAntropometricosNuevaConsultaService.setPreviousAnthropometricData();

		this.signosVitalesNuevaConsultaService.setPreviousVitalSignsData();

		this.motivoNuevaConsultaService.error$.subscribe(motivoError => {
			this.errores[0] = motivoError;
		});
		this.ambulatoryConsultationProblemsService.error$.subscribe(problemasError => {
			this.errores[1] = problemasError;
		});
		this.datosAntropometricosNuevaConsultaService.heightError$.subscribe(tallaError => {
			this.errores[2] = tallaError;
		});
		this.datosAntropometricosNuevaConsultaService.weightError$.subscribe(pesoError => {
			this.errores[3] = pesoError;
		});
		this.signosVitalesNuevaConsultaService.heartRateError$.subscribe(frecuenciaCardiacaError => {
			this.errores[4] = frecuenciaCardiacaError;
		});
		this.signosVitalesNuevaConsultaService.respiratoryRateError$.subscribe(frecuenciaRespiratoriaError => {
			this.errores[5] = frecuenciaRespiratoriaError;
		});
		this.signosVitalesNuevaConsultaService.temperatureError$.subscribe(temperaturaCorporalError => {
			this.errores[6] = temperaturaCorporalError;
		});
		this.signosVitalesNuevaConsultaService.bloodOxygenSaturationError$.subscribe(saturacionOxigenoError => {
			this.errores[7] = saturacionOxigenoError;
		});
		this.signosVitalesNuevaConsultaService.systolicBloodPressureError$.subscribe(presionSistolicaError => {
			this.errores[8] = presionSistolicaError;
		});
		this.signosVitalesNuevaConsultaService.diastolicBloodPressureError$.subscribe(presionDiastolicaError => {
			this.errores[9] = presionDiastolicaError;
		});

		this.internacionMasterDataService.getHealthSeverity().subscribe(healthConditionSeverities => {
			this.severityTypes = healthConditionSeverities;
			this.ambulatoryConsultationProblemsService.setSeverityTypes(healthConditionSeverities);
		});

		this.internacionMasterDataService.getAllergyCriticality().subscribe(allergyCriticalities => {
			this.criticalityTypes = allergyCriticalities;
			this.alergiasNuevaConsultaService.setCriticalityTypes(allergyCriticalities);
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_REPORTE_EPIDEMIOLOGICO).subscribe(isOn => this.ffIsOn = isOn);
	}


	previousDataIsConfirmed(): Observable<boolean> {
		if ((this.signosVitalesNuevaConsultaService.getShowPreloadedVitalSignsData()) ||
			(this.datosAntropometricosNuevaConsultaService.getShowPreloadedAnthropometricData())) {
			const dialogRef = this.dialog.open(PreviousDataComponent,
				{
					data: {
						signosVitalesNuevaConsultaService: this.signosVitalesNuevaConsultaService,
						datosAntropometricosNuevaConsultaService: this.datosAntropometricosNuevaConsultaService
					},
					disableClose: true,
					autoFocus: false
				});
			return dialogRef.afterClosed();
		}
		else return of(true);
	}

	save(): void {
		this.previousDataIsConfirmed().subscribe((answerPreviousData: boolean) => {

			const nuevaConsulta: CreateOutpatientDto = this.buildCreateOutpatientDto();
			const fieldsService = new NewConsultationSuggestedFieldsService(nuevaConsulta, this.translateService);

			this.apiErrors = [];
			this.addErrorMessage(nuevaConsulta);

			if (answerPreviousData) {
				if ((this.isValidConsultation()) && (this.formEvolucion.valid)) {
					if (!fieldsService.nonCompletedFields.length) {
						this.uploadReferencesFileAndCreateConsultation(nuevaConsulta);
					}
					else {
						this.openDialog(fieldsService.nonCompletedFields, fieldsService.presentFields, nuevaConsulta);
					}
				} else {
					this.disableConfirmButton = false;
				}
			}

		});
	}

	private openDialog(nonCompletedFields: string[], presentFields: string[], nuevaConsulta: CreateOutpatientDto): void {
		const dialogRef = this.dialog.open(SuggestedFieldsPopupComponent, {
			data: {
				nonCompletedFields,
				presentFields
			}
		});
		dialogRef.afterClosed().subscribe(confirmado => {
			if (confirmado) {
				this.uploadReferencesFileAndCreateConsultation(nuevaConsulta);
			}
		});
	}

	private uploadReferencesFileAndCreateConsultation(nuevaConsulta: CreateOutpatientDto) {
		let references: Reference[] = this.ambulatoryConsultationReferenceService.getReferences();
		if (!references.length) {
			this.goToCreateConsultation(nuevaConsulta);
			return;
		}

		const filesToUpdate: Observable<number>[] = [];

		references.forEach(reference => {
			reference.referenceFiles.forEach(file => {
				const obs = this.referenceFileService.uploadReferenceFiles(this.data.idPaciente, file);
				filesToUpdate.push(obs);
			})
		});

		if (filesToUpdate.length) {

			forkJoin(filesToUpdate).subscribe((referenceFileId: number[]) => {
				let indiceRefFilesIds = 0;
				references.forEach(reference => {

					const filesLength = reference.referenceFiles.length;
					for (let a = indiceRefFilesIds; a < indiceRefFilesIds + filesLength; a++)
						this.ambulatoryConsultationReferenceService.addFileIdAt(reference.referenceNumber, referenceFileId[a]);
					indiceRefFilesIds += filesLength;
				});
				this.goToCreateConsultation(nuevaConsulta);
			}, _ => {
				this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR_TO_UPLOAD_FILES');
				this.errorToUploadReferenceFiles();
			});
		}
		else
			this.goToCreateConsultation(nuevaConsulta);
	}

	private createConsultation(nuevaConsulta: CreateOutpatientDto) {
		const problemsToUpdate = (!nuevaConsulta.references.length) ? nuevaConsulta.problems : this.problemsToUpdate(nuevaConsulta);

		if (nuevaConsulta.references.length) {
			nuevaConsulta.problems = problemsToUpdate;
		}

		this.outpatientConsultationService.createOutpatientConsultation(nuevaConsulta, this.data.idPaciente).subscribe(
			_ => {
				this.snackBarService.showSuccess('ambulatoria.paciente.nueva-consulta.messages.SUCCESS', { duration: TIME_OUT });
				this.dockPopupRef.close(mapToFieldsToUpdate(nuevaConsulta));
				if (this.thereAreProblemsToSnvsReport()) {
					const toReport = this.createSnvsToReportList();
					setTimeout(() => {
						this.dialog.open(SnvsReportsResultComponent, {
							disableClose: true,
							autoFocus: false,
							data: {
								toReportList: toReport,
								patientId: this.data.idPaciente,
								snvsEvent: this.ambulatoryConsultationProblemsService.getSnvsEventsInformation()
							}
						});
					}, TIME_OUT);
				}
			},
			response => {
				this.disableConfirmButton = false;
				if (response.errors)
					response.errors.forEach(val => {
						this.apiErrors.push(val);
					});
				this.snackBarService.showError('ambulatoria.paciente.nueva-consulta.messages.ERROR');
				const filesToDelete = nuevaConsulta.references.filter(reference => reference.fileIds.length > 0);
				if (filesToDelete.length){
					this.errorToUploadReferenceFiles();
				}
			}
		);

		function mapToFieldsToUpdate(nuevaConsultaDto: CreateOutpatientDto) {
			return {
				allergies: !!nuevaConsultaDto.allergies?.length,
				personalHistories: !!nuevaConsultaDto.problems?.length,
				familyHistories: !!nuevaConsultaDto.familyHistories?.length,
				vitalSigns: !!nuevaConsultaDto.vitalSigns,
				medications: !!nuevaConsultaDto.medications?.length,
				anthropometricData: !!nuevaConsultaDto.anthropometricData,
				problems: !!problemsToUpdate.length,
			};
		}
	}

	public isValidConsultation(): boolean {
		return (this.errores.find(elem =>
			elem !== undefined
		) === undefined);
	}

	private addErrorMessage(consulta: CreateOutpatientDto): void {
		if (parseInt(consulta.anthropometricData?.height?.value, 10) < 0) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_MIN');
		} else if (parseInt(consulta.anthropometricData?.height?.value, 10) > 1000) {
			this.datosAntropometricosNuevaConsultaService.setHeightError('ambulatoria.paciente.nueva-consulta.errors.TALLA_MAX');
		}

		if (parseInt(consulta.anthropometricData?.weight?.value, 10) < 0) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_MIN');
		} else if (parseInt(consulta.anthropometricData?.weight?.value, 10) > 1000) {
			this.datosAntropometricosNuevaConsultaService.setWeightError('ambulatoria.paciente.nueva-consulta.errors.PESO_MAX');
		}

		if (parseInt(consulta.vitalSigns.heartRate?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setHeartRateError('ambulatoria.paciente.nueva-consulta.errors.FRECUENCIA_CARDIACA_MIN');
		}

		if (parseInt(consulta.vitalSigns.respiratoryRate?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setRespiratoryRateError('ambulatoria.paciente.nueva-consulta.errors.FRECUENCIA_RESPIRATORIA_MIN');
		}

		if (parseInt(consulta.vitalSigns.temperature?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setTemperatureError('ambulatoria.paciente.nueva-consulta.errors.TEMPERATURA_CORPORAL_MIN');
		}

		if (parseInt(consulta.vitalSigns.bloodOxygenSaturation?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setBloodOxygenSaturationError('ambulatoria.paciente.nueva-consulta.errors.SATURACION_OXIGENO_MIN');
		}

		if (parseInt(consulta.vitalSigns.diastolicBloodPressure?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setDiastolicBloodPressureError('ambulatoria.paciente.nueva-consulta.errors.TENSION_DIASTOLICA_MIN');
		}

		if (parseInt(consulta.vitalSigns?.systolicBloodPressure?.value, 10) < 0) {
			this.signosVitalesNuevaConsultaService.setSystolicBloodPressureError('ambulatoria.paciente.nueva-consulta.errors.TENSION_SISTOLICA_MIN');
		}

		hasError(this.formEvolucion, 'maxlength', 'evolucion') ?
			this.errores[10] = 'ambulatoria.paciente.nueva-consulta.errors.MAX_LENGTH_NOTA'
			: this.errores[10] = undefined;
	}

	private buildProblema(p: HealthConditionNewConsultationDto) {
		const problema: Problema = {
			snomed: p.snomed,
			codigoSeveridad: p.severity,
			cronico: p.isChronic,
			fechaInicio: p.startDate ? dateToMomentTimeZone(p.startDate) : newMoment(),
			fechaFin: p.inactivationDate ? dateToMomentTimeZone(p.inactivationDate) : undefined
		};
		return problema;
	}

	private buildCreateOutpatientDto(): CreateOutpatientDto {
		return {
			allergies: this.alergiasNuevaConsultaService.getAlergias().map((alergia: Alergia) => {
				return {
					categoryId: null,
					snomed: alergia.snomed,
					startDate: null,
					statusId: null,
					verificationId: null,
					criticalityId: alergia.criticalityId,
				};
			}),
			anthropometricData: this.datosAntropometricosNuevaConsultaService.getDatosAntropometricos(),
			evolutionNote: this.formEvolucion.value?.evolucion,
			familyHistories: this.antecedentesFamiliaresNuevaConsultaService.getAntecedentesFamiliares().map((antecedente: AntecedenteFamiliar) => {
				return {
					snomed: antecedente.snomed,
					startDate: antecedente.fecha ? momentFormat(antecedente.fecha, DateFormat.API_DATE) : undefined
				};
			}),
			medications: this.medicacionesNuevaConsultaService.getMedicaciones().map((medicacion: Medicacion) => {
				return {
					note: medicacion.observaciones,
					snomed: medicacion.snomed,
					suspended: medicacion.suspendido,
				};
			}
			),
			problems: this.ambulatoryConsultationProblemsService.getProblemas().map(
				(problema: Problema) => {
					return {
						severity: problema.codigoSeveridad,
						chronic: problema.cronico,
						endDate: problema.fechaFin ? momentFormat(problema.fechaFin, DateFormat.API_DATE) : undefined,
						snomed: problema.snomed,
						startDate: problema.fechaInicio ? momentFormat(problema.fechaInicio, DateFormat.API_DATE) : undefined
					};
				}
			),
			procedures: this.procedimientoNuevaConsultaService.getProcedimientos(),
			reasons: this.motivoNuevaConsultaService.getMotivosConsulta(),
			vitalSigns: this.signosVitalesNuevaConsultaService.getSignosVitales(),
			clinicalSpecialtyId: this.defaultSpecialty?.id,
			references: this.ambulatoryConsultationReferenceService.getOutpatientReferences(),
		};
	}

	editProblema() {
		if (this.ambulatoryConsultationProblemsService.editProblem()) {
			this.readOnlyProblema = false;
		}
	}

	setDefaultSpecialty() {
		this.defaultSpecialty = this.formEvolucion.controls.clinicalSpecialty.value;
	}


	errorToUploadReferenceFiles() {
		const filesToDelete = this.ambulatoryConsultationReferenceService.getReferenceFilesIds();
		this.referenceFileService.deleteReferenceFiles(filesToDelete);
		this.ambulatoryConsultationReferenceService.setReferenceFilesIds([]);
	}

	goToCreateConsultation(nuevaConsulta: CreateOutpatientDto) {
		nuevaConsulta.references = this.ambulatoryConsultationReferenceService.getOutpatientReferences();
		this.createConsultation(nuevaConsulta);
		this.disableConfirmButton = true;
	}

	private thereAreProblemsToSnvsReport(): boolean {
		const problems = this.ambulatoryConsultationProblemsService.getProblemas();
		const firstProblemToReport = problems.find((problem: AmbulatoryConsultationProblem) =>
			(problem.isReportable && problem.epidemiologicalManualClassifications?.length)
		);
		if (firstProblemToReport)
			return true;
		return false;
	}

	private createSnvsToReportList(): SnvsToReportDto[] {
		const reportableProblems = this.ambulatoryConsultationProblemsService.getProblemas().filter((problem: AmbulatoryConsultationProblem) =>
			(problem.isReportable && problem.epidemiologicalManualClassifications?.length)
		);

		const result: SnvsToReportDto[] = [];

		reportableProblems.forEach((problem: AmbulatoryConsultationProblem) => {
			problem.snvsReports.forEach(report => {
				const toReport: SnvsToReportDto = {
					eventId: report.eventId,
					groupEventId: report.groupEventId,
					manualClassificationId: report.manualClassificationId,
					problem: {
						pt: problem.snomed.pt,
						sctid: problem.snomed.sctid
					}
				};
				result.push(toReport);
			});
		});

		return result;
	}
	private mapToOutpatientProblemDto(problem: HCEPersonalHistory): OutpatientProblemDto {
		return {
			chronic: problem.chronic,
			severity: problem.hcePersonalHistoryDto.severity,
			snomed: problem.hcePersonalHistoryDto.snomed,
			startDate: problem.hcePersonalHistoryDto.startDate,
			statusId: problem.hcePersonalHistoryDto.statusId,
		}
	}

	private problemsToUpdate(nuevaConsultaDto: CreateOutpatientDto): OutpatientProblemDto[] {
		const outpatientProblemDto: OutpatientProblemDto[] = [];

		nuevaConsultaDto.problems?.forEach(problem => outpatientProblemDto.push(problem));

		const references: Reference[] = this.ambulatoryConsultationReferenceService.getReferences();

		references.forEach(reference => {
			const referenceProblems = this.ambulatoryConsultationReferenceService.getReferenceProblems(reference.referenceNumber);
			referenceProblems.forEach(referenceProblem => {
				const outProblemDto = this.mapToOutpatientProblemDto(referenceProblem);
				const existProblem = outpatientProblemDto.find(problem => problem.snomed.sctid === outProblemDto.snomed.sctid);
				if (!existProblem) {
					outpatientProblemDto.push(outProblemDto);
				}
			});
		});

		return outpatientProblemDto;
	}
}

export interface NuevaConsultaData {
	idPaciente: number;
	idProblema?: number;
}
