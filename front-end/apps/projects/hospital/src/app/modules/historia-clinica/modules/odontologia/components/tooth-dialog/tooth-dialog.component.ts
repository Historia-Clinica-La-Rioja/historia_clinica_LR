import {AfterViewInit, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {
	HCEToothRecordDto,
	OdontologyConceptDto,
	ToothDto,
	ToothSurfacesDto
} from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DateFormat, momentFormatDate } from '@core/utils/moment.utils';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {OdontogramService as OdontogramRestService, ToothRecordDto} from '../../api-rest/odontogram.service';
import {ActionType, ProcedureOrder, ToothAction} from '../../services/actions.service';
import {ConceptsFacadeService} from '../../services/concepts-facade.service';
import {ToothTreatment} from '../../services/surface-drawer.service';
import {SurfacesNamesFacadeService, ToothSurfaceNames} from '../../services/surfaces-names-facade.service';
import {getSurfaceShortName} from '../../utils/surfaces';
import {Actions, ToothComponent} from '../tooth/tooth.component';
import {TypeaheadOption} from "@core/components/typeahead/typeahead.component";
import { ScrollableData } from '../hidable-scrollable-data/hidable-scrollable-data.component';
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";

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
	filteredDiagnosticsTypeaheadOptions:  TypeaheadOption<OdontologyConceptDto>[];

	initValueDiagnosticsPieceTypeahead: TypeaheadOption<OdontologyConceptDto>;
	initValueDiagnosticsSurfaceTypeahead: TypeaheadOption<OdontologyConceptDto>;

	private procedures: OdontologyConceptDto[];
	filteredProceduresTypeahead: TypeaheadOption<OdontologyConceptDto>[];

	firstProcedureId: string;
	initValueTypeaheadPieceFirstProcedure: TypeaheadOption<OdontologyConceptDto>;
	initValueTypeaheadSurfaceFirstProcedure: TypeaheadOption<OdontologyConceptDto>;

	secondProcedureId: string;
	initValueTypeaheadProcedureTwo: TypeaheadOption<OdontologyConceptDto>;

	thirdProcedureId: string;
	initValueTypeaheadProcedureThree: TypeaheadOption<OdontologyConceptDto>;

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
				this.setCurrentFinding(this.data.currentActions?.find(currentActions => currentActions.action.type === ActionType.DIAGNOSTIC && !currentActions.surfaceId)?.action.sctid);
			}
		);
		this.conceptsFacadeService.getProcedures$().subscribe(
			procedures => {
				this.procedures = procedures;
				this.setAppropiateProcedures(filterFunction);
				const onlyProcedures = (this.data.currentActions?.filter(currentActions => currentActions.action.type === ActionType.PROCEDURE));
				const firstProcedureId =  onlyProcedures?.find(first => first.wholeProcedureOrder === ProcedureOrder.FIRST)?.action.sctid;
				const secondProcedureId = onlyProcedures?.find(second => second.wholeProcedureOrder === ProcedureOrder.SECOND)?.action.sctid;
				const thirdProcedureId = onlyProcedures?.find(third => third.wholeProcedureOrder === ProcedureOrder.THIRD)?.action.sctid;

				this.setProcedures(firstProcedureId, ProcedureOrder.FIRST);
				this.setProcedures(secondProcedureId, ProcedureOrder.SECOND);
				this.setProcedures(thirdProcedureId, ProcedureOrder.THIRD);

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

	private setAppropiateFindings(filterFuncion): void{
		const filteredDiagnostics = this.diagnostics?.filter(filterFuncion);
		if (filteredDiagnostics){
			this.filteredDiagnosticsTypeaheadOptions = filteredDiagnostics.map(this.toTypeaheadOptions);
		}
	}

	private setAppropiateProcedures(filterFuncion): void{
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

	private toTypeaheadOptions(odontologyConcept: OdontologyConceptDto): TypeaheadOption<OdontologyConceptDto>{
		return {
			compareValue: odontologyConcept?.snomed.pt,
			value: odontologyConcept
		}
	}

	private setCurrentFinding(currentFindingSctid: string): void{
		const typeaheadConcept = this.filteredDiagnosticsTypeaheadOptions?.find(diagnosticsTypeahead => diagnosticsTypeahead.value.snomed.sctid === currentFindingSctid);
		(!this.selectedSurfaces.length) ? this.initValueDiagnosticsPieceTypeahead = typeaheadConcept : this.initValueDiagnosticsSurfaceTypeahead = typeaheadConcept;
	}

	private setProcedures(procedureSctid: string, order: ProcedureOrder){
		const typeaheadConcept = this.filteredProceduresTypeahead?.find(procedureTypeahead => procedureTypeahead.value.snomed.sctid === procedureSctid);
		if (order === ProcedureOrder.FIRST){
			(!this.selectedSurfaces.length) ? this.initValueTypeaheadPieceFirstProcedure = typeaheadConcept : this.initValueTypeaheadSurfaceFirstProcedure = typeaheadConcept;
		}
		else {
			if (order === ProcedureOrder.SECOND){
				this.initValueTypeaheadProcedureTwo = typeaheadConcept;
			}
			else {
				this.initValueTypeaheadProcedureThree = typeaheadConcept;
			}
		}

	}

	public findingChanged(hallazgo: OdontologyConceptDto): void {
		if (hallazgo) {
			this.newHallazgoId = hallazgo.snomed.sctid;
			this.setCurrentFinding(hallazgo.snomed.sctid);
		}
	}

	public firstProcedureChanged(firstProcedure: OdontologyConceptDto): void {
		if (firstProcedure) {
			this.firstProcedureId = firstProcedure.snomed.sctid;
			this.setProcedures(firstProcedure.snomed.sctid, ProcedureOrder.FIRST);
		}
	}

	public secondProcedureChanged(secondProcedure: OdontologyConceptDto): void {
		if (secondProcedure) {
			this.secondProcedureId = secondProcedure.snomed.sctid;
			this.setProcedures(secondProcedure.snomed.sctid, ProcedureOrder.SECOND);
		}
	}

	public thirdProcedureChanged(thirdProcedure: OdontologyConceptDto): void {
		if (thirdProcedure) {
			this.thirdProcedureId = thirdProcedure.snomed.sctid;
			this.setProcedures(thirdProcedure.snomed.sctid, ProcedureOrder.THIRD);
		}
	}
	private setUndefined(initValueTypeahead: TypeaheadOption<OdontologyConceptDto>): void{
		if (initValueTypeahead?.compareValue){
			initValueTypeahead.compareValue = undefined;
			initValueTypeahead.value = undefined;
		}
	}

	private reciveToothCurrentViewActions(actions: Actions): void {
		if (actions?.findingId) {
			this.setCurrentFinding(actions.findingId);
		}
		else {
			this.newHallazgoId = undefined;
			(!this.selectedSurfaces.length) ? this.setUndefined(this.initValueDiagnosticsPieceTypeahead) : this.setUndefined(this.initValueDiagnosticsSurfaceTypeahead);
		}
		if (actions?.procedures.firstProcedureId){
			this.setProcedures(actions.procedures.firstProcedureId, ProcedureOrder.FIRST);
		}
		if (actions?.procedures.secondProcedureId){
			this.setProcedures(actions.procedures.secondProcedureId, ProcedureOrder.SECOND);
		}
		if (actions?.procedures.thirdProcedureId){
			this.setProcedures(actions.procedures.thirdProcedureId, ProcedureOrder.THIRD);
		}
		if (!(actions?.procedures.firstProcedureId) && !(actions?.procedures.secondProcedureId) && !(actions?.procedures.thirdProcedureId)) {
			(!this.selectedSurfaces.length) ? this.setUndefined(this.initValueTypeaheadPieceFirstProcedure) : this.setUndefined(this.initValueTypeaheadSurfaceFirstProcedure);

			this.setUndefined(this.initValueTypeaheadProcedureTwo);
			this.setUndefined(this.initValueTypeaheadProcedureThree);
			this.firstProcedureId = undefined;
			this.secondProcedureId = undefined;
			this.thirdProcedureId = undefined;
		}
	}

	private toScrollableData(toothRecordDtos: HCEToothRecordDto[]): ScrollableData[] {
		const toSingleScrolleableData = (toothRecordDto: HCEToothRecordDto): ScrollableData => {
			const surfaceText = toothRecordDto.surfaceSctid ? ` (${getSurfaceShortName(toothRecordDto.surfaceSctid)})` : ''
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
}

