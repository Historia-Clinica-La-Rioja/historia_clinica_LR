import { OdontologyConceptDto } from '@api-rest/api-model';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { ActionType } from './actions.service';
import { ActionedTooth, OdontogramService } from './odontogram.service';
import { combineLatest } from 'rxjs';
import { SurfacesNamesFacadeService } from './surfaces-names-facade.service';
import { ConceptsFacadeService } from './concepts-facade.service';

export class ActionsNewConsultationService {

	tableData: TableData[] = [];
	columns: ColumnConfig[];
	displayedColumns: string[];

	private actions: OdontologyConceptDto[];

	constructor(
		private readonly odontogramService: OdontogramService,
		private readonly surfacesNamesFacadeService: SurfacesNamesFacadeService,
		private readonly actionType: ActionType.DIAGNOSTIC | ActionType.PROCEDURE,
		private readonly conceptsFacadeService: ConceptsFacadeService,
	) {

		const description = this.actionType === ActionType.DIAGNOSTIC ? 'Diagnóstico' : 'Procedimiento'
		const actions$ = this.actionType === ActionType.DIAGNOSTIC ? this.conceptsFacadeService.getDiagnostics$() : this.conceptsFacadeService.getProcedures$();
		this.columns = [
			{
				def: 'zone',
				header: 'Zona',
				text: (a: TableData) => `Pieza ${a.zone}`
			},
			{
				def: 'action',
				header: description,
				text: (a: TableData) => a.action
			}
		];
		this.displayedColumns = this.columns.map(a => a.def);

		const actionedTeeth$ = this.odontogramService.actionedTeeth$;

		combineLatest([actions$, actionedTeeth$]).subscribe(([actions, actionedTeeth]) => {
			this.actions = actions;
			this.tableData = [];
			actionedTeeth
				.forEach((actionatedTooth: ActionedTooth) => {
					this.tableData = this.tableData.concat(this.mapToothAction(actionatedTooth))
				});
		})


	}

	private mapToothAction(actionedTooth: ActionedTooth): TableData[] {
		return actionedTooth.actions
			.filter(action => action.action.type === this.actionType)
			.map(action => {
				let surface = this.surfacesNamesFacadeService.getToothSurfaceShortName(actionedTooth.tooth.snomed.sctid, action.surfaceId);
				surface = surface ? ` (${surface})` : '';
				return {
					zone: actionedTooth.toothCompleteNumber + surface,
					action: this.actions.find(d => d.snomed.sctid === action.action.sctid).snomed.pt
				}
			});
	}


}

interface TableData {
	zone: string,
	action: string
}
