import { BehaviorSubject, Observable } from 'rxjs';
import { ToothSurfaceId } from '../utils/Surface';
export class ActionsService {


	private actionsOnTeeth: ToothAction[] = [];
	private records: ToothAction[] = [];

	private currentDrawEmitter = new BehaviorSubject<CurrentDraw>({
		[ToothSurfaceId.WHOLE]: null,
		[ToothSurfaceId.CENTRAL]: null,
		[ToothSurfaceId.EXTERNAL]: null,
		[ToothSurfaceId.RIGHT]: null,
		[ToothSurfaceId.LEFT]: null,
		[ToothSurfaceId.INTERNAL]: null,
	});
	currentDraw$: Observable<CurrentDraw> = this.currentDrawEmitter.asObservable();

	constructor() { }

	getActions(surfacesId?: string[]): ToothAction[] {
		const filterFuncion = (action: ToothAction) => surfacesId?.length ? surfacesId?.includes(action.surfaceId) : !action.surfaceId;
		return this.actionsOnTeeth.filter(action => filterFuncion(action));
	}

	addProcedure(actionSctid: string, surfacesIds: string[], order: ProcedureOrder) {
		const action: Action = { sctid: actionSctid, type: ActionType.PROCEDURE }
		this.execute(action, surfacesIds, (setAction: ToothAction) => setAction.wholeProcedureOrder === order && !setAction.surfaceId, order);
	}

	addFinding(actionSctid: string, surfacesIds: string[]) {
		const action: Action = { sctid: actionSctid, type: ActionType.DIAGNOSTIC }
		this.execute(action, surfacesIds, (setAction: ToothAction) => setAction.action.type === ActionType.DIAGNOSTIC && !setAction.surfaceId)
	}


	setActions(actions: ToothAction[]) {
		this.actionsOnTeeth = actions || [];
		this.currentDrawEmitter.next(this.getCurrentDraw());
	}

	getAllActions() {
		return this.actionsOnTeeth;
	}

	private execute(action: Action, surfacesIds: string[], filterFuncion, order?: ProcedureOrder) {
		if (!surfacesIds.length) {
			const toothAction = this.actionsOnTeeth.find(filterFuncion);
			if (toothAction) {
				toothAction.action = action;
			} else {
				this.addToothAction(action, order);
			}
		} else {
			surfacesIds.forEach(id => {
				const toBeReplaced = this.toReplace(id, action.type);
				if (toBeReplaced) {
					toBeReplaced.action.sctid = action.sctid;
				} else {
					this.addSurfaceAction(id, action);
				}
			});
		}

		this.currentDrawEmitter.next(this.getCurrentDraw());
	}

	private toReplace(surfaceId: string, type: ActionType): ToothAction {
		return this.actionsOnTeeth.find(a => a.action.type === type && a.surfaceId === surfaceId);
	}

	private addToothAction(action: Action, wholeProcedureOrder?: ProcedureOrder) {
		const toAdd: ToothAction[] = [{ action, wholeProcedureOrder }];
		this.actionsOnTeeth = this.actionsOnTeeth.concat(toAdd);
	}

	private addSurfaceAction = (surfaceId: string, action: Action) => {
		const toAdd = { action, surfaceId };
		this.actionsOnTeeth = this.actionsOnTeeth.concat(toAdd);
	}

	setRecords(records: ToothAction[]) {
		this.records = records || [];
		if (records?.length) {
			this.currentDrawEmitter.next(this.getCurrentDraw());
		}
	}


	private getCurrentDraw(): CurrentDraw {

		const getInitialDraw = () => {
			const whole = this.records.find(r => !r.surfaceId)?.action
			const central = this.records.find(r => r.surfaceId === ToothSurfaceId.CENTRAL)?.action
			const external = this.records.find(r => r.surfaceId === ToothSurfaceId.EXTERNAL)?.action
			const right = this.records.find(r => r.surfaceId === ToothSurfaceId.RIGHT)?.action
			const left = this.records.find(r => r.surfaceId === ToothSurfaceId.LEFT)?.action
			const internal = this.records.find(r => r.surfaceId === ToothSurfaceId.INTERNAL)?.action

			return { whole, central, external, right, left, internal };
		}

		const currentDraw: CurrentDraw = getInitialDraw();

		const borrarDibujoCaras = () => {
			currentDraw[ToothSurfaceId.WHOLE] = null;
			currentDraw[ToothSurfaceId.CENTRAL] = null;
			currentDraw[ToothSurfaceId.EXTERNAL] = null;
			currentDraw[ToothSurfaceId.RIGHT] = null;
			currentDraw[ToothSurfaceId.LEFT] = null;
			currentDraw[ToothSurfaceId.INTERNAL] = null;
		};

		const actualizarDibujoCara = (toothAction: ToothAction) => {
			currentDraw[toothAction.surfaceId] = toothAction.action
		}


		const removeRecordActions = () => {
			Object.keys(currentDraw).forEach(
				key => {
					if (currentDraw[key]?.type === ActionType.RECORD) {
						currentDraw[key] = null;
					}
				}
			);
		}

		this.actionsOnTeeth?.forEach(
			toothAction => {
				if (toothAction.action.type !== ActionType.RECORD) {
					removeRecordActions();
				}
				if (toothAction.surfaceId) {
					if (toothAction.action.type === ActionType.DIAGNOSTIC && currentDraw[ToothSurfaceId.WHOLE]?.type === ActionType.PROCEDURE) {
						return;
					}
					const surfaceHasProcedure = currentDraw[toothAction.surfaceId]?.type === ActionType.PROCEDURE; // Si es false entonces o no tiene nada o tiene hallazgo ==> dibuja
					if ((!surfaceHasProcedure)) {
						actualizarDibujoCara(toothAction);
					}
				} else { // Es pieza
					if (toothAction.action.type === ActionType.PROCEDURE) {
						borrarDibujoCaras();
						currentDraw[ToothSurfaceId.WHOLE] = toothAction.action
					} else if (currentDraw[ToothSurfaceId.WHOLE]?.type !== ActionType.PROCEDURE) {
						currentDraw[ToothSurfaceId.WHOLE] = toothAction.action
					}
				}
			}
		)
		return currentDraw;

	}

	deleteAction (surfaceId: string, sctid: string, actionTypeToDelete: ActionType, order: ProcedureOrder){
		const elementToDelete: ToothAction = this.actionsOnTeeth.find(
			actionSurface =>
				actionSurface.surfaceId === surfaceId && actionSurface.action.sctid === sctid &&
				actionSurface.action.type === actionTypeToDelete &&
				actionSurface.wholeProcedureOrder === order
		);

		const index = this.actionsOnTeeth.indexOf(elementToDelete);

		this.actionsOnTeeth.splice(index, 1);

		this.setActions(this.actionsOnTeeth);
	}
}


export interface ToothAction {
	surfaceId?: string;
	wholeProcedureOrder?: ProcedureOrder
	action: Action
};

export interface Action {
	sctid: string;
	type: ActionType
}

export interface CurrentDraw {
	[ToothSurfaceId.WHOLE]: Action
	[ToothSurfaceId.LEFT]: Action,
	[ToothSurfaceId.INTERNAL]: Action,
	[ToothSurfaceId.RIGHT]: Action,
	[ToothSurfaceId.EXTERNAL]: Action,
	[ToothSurfaceId.CENTRAL]: Action,
}

export enum ProcedureOrder {
	FIRST, SECOND, THIRD
}

export enum ActionType {
	PROCEDURE, DIAGNOSTIC, RECORD
}
