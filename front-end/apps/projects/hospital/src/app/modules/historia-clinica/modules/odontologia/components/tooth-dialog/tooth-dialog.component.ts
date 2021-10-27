import { AfterViewInit, Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { DateFormat, momentFormatDate } from '@core/utils/moment.utils';

import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

import { HCEToothRecordDto, OdontologyConceptDto, ToothDto, ToothSurfacesDto } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';

import { ShowActionsService } from '@historia-clinica/modules/odontologia/services/show-actions.service';

import { OdontogramService as OdontogramRestService } from '../../api-rest/odontogram.service';
import { ActionType, ProcedureOrder, ToothAction } from '../../services/actions.service';
import { ConceptsFacadeService } from '../../services/concepts-facade.service';
import { ToothTreatment } from '../../services/surface-drawer.service';
import { SurfacesNamesFacadeService, ToothSurfaceNames } from '../../services/surfaces-names-facade.service';
import { getSurfaceShortName } from '../../utils/surfaces';
import { Actions, ToothComponent } from '../tooth/tooth.component';
import { ScrollableData } from '../hidable-scrollable-data/hidable-scrollable-data.component';

@Component({
	selector: 'app-tooth-dialog',
	templateUrl: './tooth-dialog.component.html',
	styleUrls: ['./tooth-dialog.component.scss'],
})
export class ToothDialogComponent implements OnInit, AfterViewInit {

	@ViewChild('tooth') toothComponent: ToothComponent;

	constructor(
		private readonly odontogramRestService: OdontogramRestService,
		private readonly hceGeneralStateService: HceGeneralStateService,
		@Inject(MAT_DIALOG_DATA) public data: { tooth: ToothDto, quadrantCode: number, currentActions: ToothAction[], records: ToothAction[], patientId: string },
		private dialogRef: MatDialogRef<ToothDialogComponent>,
		private conceptsFacadeService: ConceptsFacadeService,
		private surfacesNamesFacadeService: SurfacesNamesFacadeService
	) {
	}
	showOlderRecords = false;
	readonly shortNameOf = getSurfaceShortName;

	readonly toothTreatment = ToothTreatment.AS_FRACTIONAL_TOOTH;

	historicalRecords$: Observable<ScrollableData[]>

	newHallazgoId: string;

	selectedSurfacesText: string;

	selectedSurfaces: string[] = [];

	private surfacesDto: ToothSurfacesDto;

	private diagnostics: OdontologyConceptDto[];
	filteredDiagnosticsTypeaheadOptions: TypeaheadOption<OdontologyConceptDto>[];

	initValueTypeaheadDiagnostics: TypeaheadOption<OdontologyConceptDto> = null;

	private procedures: OdontologyConceptDto[];
	filteredProceduresTypeahead: TypeaheadOption<OdontologyConceptDto>[];

	firstProcedureId: string;
	initValueTypeaheadFirstProcedure: TypeaheadOption<OdontologyConceptDto> = null;

	secondProcedureId: string;
	initValueTypeaheadSecondProcedure: TypeaheadOption<OdontologyConceptDto> = null;

	thirdProcedureId: string;
	initValueTypeaheadThirdProcedure: TypeaheadOption<OdontologyConceptDto> = null;

	lastProcedureAdded = -1;

	showActionsService;

	ngAfterViewInit(): void {
		this.toothComponent.setFindingsAndProcedures(this.data.currentActions);
		this.toothComponent.actionsSubject$.subscribe(actionsSctids => {
			this.reciveToothCurrentViewActions(actionsSctids)
		});
		if (this.data.records.length) {
			this.toothComponent.setRecords(this.data.records);
		}
	}

	ngOnInit(): void {

		this.loadToothSurfaceName();

		this.odontogramRestService.getToothSurfaces(this.data.tooth.snomed.sctid).subscribe(surfaces => this.surfacesDto = surfaces);
		const filterFunction = this.getFilterFuction(false);
		this.conceptsFacadeService.getDiagnostics$().subscribe(
			diagnostics => {
				this.diagnostics = diagnostics;
				this.setAppropiateFindings(filterFunction);
				const hallazgoId = this.data.currentActions?.find(currentActions => currentActions.action.type === ActionType.DIAGNOSTIC && !currentActions.surfaceId)?.action.sctid
				if (hallazgoId) {
					this.setTypeaheadCurrentFinding(hallazgoId);
				}
			}
		);
		this.conceptsFacadeService.getProcedures$().subscribe(
			procedures => {
				this.procedures = procedures;
				this.setAppropiateProcedures(filterFunction);
				const onlyProcedures = (this.data.currentActions?.filter(currentActions => currentActions.action.type === ActionType.PROCEDURE));

				const firstProcedureId = onlyProcedures?.find(first => first.wholeProcedureOrder === ProcedureOrder.FIRST)?.action.sctid;
				if (firstProcedureId) {
					this.setTypeaheadProcedures(firstProcedureId, ProcedureOrder.FIRST);
					this.lastProcedureAdded = 0;
				}
				const secondProcedureId = onlyProcedures?.find(second => second.wholeProcedureOrder === ProcedureOrder.SECOND)?.action.sctid;
				if (secondProcedureId) {
					this.setTypeaheadProcedures(secondProcedureId, ProcedureOrder.SECOND);
					this.lastProcedureAdded = 1;
				}
				const thirdProcedureId = onlyProcedures?.find(third => third.wholeProcedureOrder === ProcedureOrder.THIRD)?.action.sctid;
				if (thirdProcedureId) {
					this.setTypeaheadProcedures(thirdProcedureId, ProcedureOrder.THIRD);
					this.lastProcedureAdded = 2;
				}
				this.showActionsService = new ShowActionsService(secondProcedureId, thirdProcedureId, this.lastProcedureAdded);
			}
		);

		this.historicalRecords$ =
			this.hceGeneralStateService.getToothRecords(parseInt(this.data.patientId), this.data.tooth.snomed.sctid)
				.pipe(
					map(this.toScrollableData)
				);
	}

	public confirm(): void {
		this.dialogRef.close(this.toothComponent.getFindingsAndProcedures());
	}

	public reciveSelectedSurfaces(surfaces: string[]): void {
		this.selectedSurfaces = surfaces;
		this.concatNames();
		this.setAppropiateActions(surfaces.length != 0)
	}

	private setAppropiateActions(surfacesSelected: boolean): void {

		const filterFuncion = this.getFilterFuction(surfacesSelected);

		this.setAppropiateFindings(filterFuncion);
		this.setAppropiateProcedures(filterFuncion);
	}

	private getFilterFuction(surfacesSelected: boolean): Function {
		let filterFuncion = (diagnostic: OdontologyConceptDto) => { return diagnostic.applicableToTooth }
		if (surfacesSelected) {
			filterFuncion = (diagnostic: OdontologyConceptDto) => { return diagnostic.applicableToSurface }
		}
		return filterFuncion;
	}

	private setAppropiateFindings(filterFuncion): void {
		const filteredDiagnostics = this.diagnostics?.filter(filterFuncion);
		if (filteredDiagnostics) {
			this.filteredDiagnosticsTypeaheadOptions = filteredDiagnostics.map(this.toTypeaheadOptions);
		}
	}

	private setAppropiateProcedures(filterFuncion): void {
		const filteredProcedures = this.procedures?.filter(filterFuncion);
		if (filteredProcedures) {
			this.filteredProceduresTypeahead = filteredProcedures.map(this.toTypeaheadOptions);
		}
	}

	private concatNames(): void {
		this.selectedSurfacesText = '';
		if (this.selectedSurfaces.length) {
			this.selectedSurfacesText = this.selectedSurfaces.length === 1 ? 'Cara ' : 'Caras ';
			const mappedNames = this.selectedSurfaces.map(surface => this.findSuitableName(surface));
			this.selectedSurfacesText += mappedNames.filter(Boolean).join(', ');
		}
	}

	public findSuitableName(surface: string): string {
		const sctid = this.surfacesDto[surface]?.sctid;
		return getSurfaceShortName(sctid);
	}

	private toTypeaheadOptions(odontologyConcept: OdontologyConceptDto): TypeaheadOption<OdontologyConceptDto> {
		return {
			compareValue: odontologyConcept?.snomed.pt,
			value: odontologyConcept
		}
	}

	private setTypeaheadCurrentFinding(currentFindingSctid: string): void {
		const typeaheadConcept = this.filteredDiagnosticsTypeaheadOptions?.find(diagnosticsTypeahead => diagnosticsTypeahead.value.snomed.sctid === currentFindingSctid);
		this.initValueTypeaheadDiagnostics = typeaheadConcept;
	}

	private setTypeaheadProcedures(procedureSctid: string, order: ProcedureOrder) {
		const typeaheadConcept = this.filteredProceduresTypeahead?.find(procedureTypeahead => procedureTypeahead.value.snomed.sctid === procedureSctid);
		if (order === ProcedureOrder.FIRST) {
			this.initValueTypeaheadFirstProcedure = typeaheadConcept;
		}
		else {
			if (order === ProcedureOrder.SECOND) {
				this.initValueTypeaheadSecondProcedure = typeaheadConcept;
			}
			else {
				this.initValueTypeaheadThirdProcedure = typeaheadConcept;
			}
		}
	}

	public findingChanged(hallazgo: OdontologyConceptDto): void {
		if (hallazgo) {
			this.newHallazgoId = hallazgo.snomed.sctid;
			this.setTypeaheadCurrentFinding(hallazgo.snomed.sctid);
		}
		else {
			if (this.initValueTypeaheadDiagnostics?.compareValue) {
				this.reorganizeAndDeleteActions(this.initValueTypeaheadDiagnostics, ActionType.DIAGNOSTIC, undefined);
			}
		}
	}

	public firstProcedureChanged(firstProcedure: OdontologyConceptDto): void {
		if (firstProcedure) {
			this.firstProcedureId = firstProcedure.snomed.sctid;
			this.setTypeaheadProcedures(firstProcedure.snomed.sctid, ProcedureOrder.FIRST);
			if (!this.selectedSurfaces.length) {
				this.lastProcedureAdded = 0;
				if (this.showActionsService.getThirdProcedure) {
					this.showActionsService.setIsNotPreviousProcedureSet(false);
				}
				else {
					this.showActionsService.setIsNotPreviousProcedureSet(true);
				}
			}
		}
		else {
			if (this.initValueTypeaheadFirstProcedure?.compareValue) {
				this.reorganizeAndDeleteActions(this.initValueTypeaheadFirstProcedure, ActionType.PROCEDURE, ProcedureOrder.FIRST);
			}
		}
	}

	public secondProcedureChanged(secondProcedure: OdontologyConceptDto): void {
		if (secondProcedure) {
			this.secondProcedureId = secondProcedure.snomed.sctid;
			this.setTypeaheadProcedures(secondProcedure.snomed.sctid, ProcedureOrder.SECOND);
			this.lastProcedureAdded = 1;
			this.showActionsService.getThirdProcedure ? this.showActionsService.setIsNotPreviousProcedureSet(false) : this.showActionsService.setIsNotPreviousProcedureSet(true);
		}
		else {
			if (this.initValueTypeaheadSecondProcedure?.compareValue) {
				this.reorganizeAndDeleteActions(this.initValueTypeaheadSecondProcedure, ActionType.PROCEDURE, ProcedureOrder.SECOND);
			}
		}
	}

	public thirdProcedureChanged(thirdProcedure: OdontologyConceptDto): void {
		if (thirdProcedure) {
			this.thirdProcedureId = thirdProcedure.snomed.sctid;
			this.setTypeaheadProcedures(thirdProcedure.snomed.sctid, ProcedureOrder.THIRD);
			this.lastProcedureAdded = 2;
		}
		else {
			if (this.initValueTypeaheadThirdProcedure?.compareValue) {
				this.reorganizeAndDeleteActions(this.initValueTypeaheadThirdProcedure, ActionType.PROCEDURE, ProcedureOrder.THIRD);
			}
		}
	}

	private reciveToothCurrentViewActions(actions: Actions): void {
		if (actions?.findingId) {
			this.setTypeaheadCurrentFinding(actions.findingId);
		}
		else {
			this.newHallazgoId = undefined;
			this.initValueTypeaheadDiagnostics = null;
		}
		if (actions?.procedures.firstProcedureId) {
			this.setTypeaheadProcedures(actions.procedures.firstProcedureId, ProcedureOrder.FIRST);
		}
		if (actions?.procedures.secondProcedureId) {
			this.setTypeaheadProcedures(actions.procedures.secondProcedureId, ProcedureOrder.SECOND);
		}
		if (actions?.procedures.thirdProcedureId) {
			this.setTypeaheadProcedures(actions.procedures.thirdProcedureId, ProcedureOrder.THIRD);
		}
		if (!(actions?.procedures.firstProcedureId) && !(actions?.procedures.secondProcedureId) && !(actions?.procedures.thirdProcedureId)) {
			this.initValueTypeaheadFirstProcedure = null;

			this.initValueTypeaheadSecondProcedure = null;
			this.initValueTypeaheadThirdProcedure = null;
			this.firstProcedureId = undefined;
			this.secondProcedureId = undefined;
			this.thirdProcedureId = undefined;
		}
	}

	private reorganizeAndDeleteActions(elementToDelete: TypeaheadOption<OdontologyConceptDto>, actionType: ActionType, order: ProcedureOrder) {
		this.toothComponent.deleteAction(elementToDelete?.value?.snomed.sctid, this.selectedSurfaces, actionType, order);
		switch (order) {
			case ProcedureOrder.FIRST:
				this.initValueTypeaheadFirstProcedure = null;
				this.firstProcedureId = null;
				this.lastProcedureAdded = -1;
				if (this.initValueTypeaheadSecondProcedure?.compareValue) {
					this.organizeTypeaheadProcedures(ProcedureOrder.SECOND);
					this.initValueTypeaheadSecondProcedure = null;
				}
				if (this.initValueTypeaheadThirdProcedure?.compareValue) {
					this.organizeTypeaheadProcedures(ProcedureOrder.THIRD);
					this.initValueTypeaheadThirdProcedure = null;
				}
				this.showActionsService.showProcedures(this.lastProcedureAdded, false);
				(this.initValueTypeaheadFirstProcedure?.compareValue) ? this.showActionsService.setIsNotPreviousProcedureSet(false) : this.showActionsService.setIsNotPreviousProcedureSet(true);
				break;
			case ProcedureOrder.SECOND:
				this.initValueTypeaheadSecondProcedure = null;
				this.secondProcedureId = null;
				this.lastProcedureAdded = 0;
				if (this.initValueTypeaheadThirdProcedure?.compareValue) {
					this.organizeTypeaheadProcedures(ProcedureOrder.THIRD);
					this.initValueTypeaheadThirdProcedure = null;
				}
				this.showActionsService.showProcedures(this.lastProcedureAdded, false);
				this.showActionsService.setIsNotPreviousProcedureSet(false);
				break;
			case ProcedureOrder.THIRD:
				this.initValueTypeaheadThirdProcedure = null;
				this.thirdProcedureId = null;
				this.lastProcedureAdded = 1;
				this.showActionsService.setIsNotPreviousProcedureSet(false);
				this.showActionsService.showProcedures(this.lastProcedureAdded, false);
				break;
			default:
				this.initValueTypeaheadDiagnostics = null;
				this.newHallazgoId = null;
				break;
		}
	}

	private toScrollableData(toothRecordDtos: HCEToothRecordDto[]): ScrollableData[] {
		const toSingleScrolleableData = (toothRecordDto: HCEToothRecordDto): ScrollableData => {
			const surfaceText = toothRecordDto.surfaceSctid ? ` ( ${getSurfaceShortName(toothRecordDto.surfaceSctid)} )` : ''
			return {
				firstElement: momentFormatDate(dateDtoToDate(toothRecordDto.date), DateFormat.VIEW_DATE),
				secondElement: toothRecordDto.snomed.pt + surfaceText
			}
		}
		return toothRecordDtos.map(toSingleScrolleableData);
	}

	private loadToothSurfaceName() {
		this.surfacesNamesFacadeService.loadSurfaceNameOf(this.data.tooth.snomed.sctid);
		this.surfacesNamesFacadeService.toothSurfaceNames$
			.subscribe(
				(toothSurfaceNames: ToothSurfaceNames) => {
					if (toothSurfaceNames?.toothSctid === this.data.tooth.snomed.sctid) {
						this.surfacesDto = toothSurfaceNames.names
					}
				}
			);
	}

	public addTypeaheadProcedure() {
		if (!this.selectedSurfaces.length) {
			this.showActionsService.showProcedures(this.lastProcedureAdded, true);
			this.showActionsService.setIsNotPreviousProcedureSet(true);
		}
	}

	private organizeTypeaheadProcedures(order) {
		if (order === ProcedureOrder.SECOND) {
			this.initValueTypeaheadFirstProcedure = this.initValueTypeaheadSecondProcedure;
			this.toothComponent.deleteAction(this.initValueTypeaheadSecondProcedure.value?.snomed.sctid, this.selectedSurfaces, ActionType.PROCEDURE, ProcedureOrder.SECOND);
			this.firstProcedureId = this.secondProcedureId;
			this.secondProcedureId = null;
			this.lastProcedureAdded = 0;
		}
		if (order === ProcedureOrder.THIRD) {
			this.initValueTypeaheadSecondProcedure = this.initValueTypeaheadThirdProcedure;
			this.toothComponent.deleteAction(this.initValueTypeaheadThirdProcedure.value?.snomed.sctid, this.selectedSurfaces, ActionType.PROCEDURE, ProcedureOrder.THIRD);
			this.secondProcedureId = this.thirdProcedureId;
			this.thirdProcedureId = null;
			this.lastProcedureAdded = 1;
		}
	}

}
