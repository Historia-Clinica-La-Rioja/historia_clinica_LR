import { BehaviorSubject, Observable } from 'rxjs';
export class ActionsService {


	private actionsOnTeeth: ToothAction[] = [];
	private currentDrawEmitter = new BehaviorSubject<CurrentDraw>({
		whole: null,
		central: null,
		external: null,
		right: null,
		left: null,
		internal: null,
	});
	currentDraw$: Observable<CurrentDraw> = this.currentDrawEmitter.asObservable();

	constructor() { }

	getActions(surfacesId?: string[]): ToothAction[] {
		const filterFuncion = (action: ToothAction) => surfacesId?.length ? surfacesId?.includes(action.surfaceId) : !action.surfaceId;
		return this.actionsOnTeeth.filter(action => filterFuncion(action));
	}

	addProcedure(actionSctid: string, surfacesIds: string[], order: ProcedureOrder) {
		this.execute(actionSctid, surfacesIds, ActionType.PROCEDURE, (setAction: ToothAction) => setAction.wholeProcedureOrder === order && !setAction.surfaceId, order);
	}

	setFinding(actionSctid: string, surfacesIds: string[]) {
		this.execute(actionSctid, surfacesIds, ActionType.DIAGNOSTIC, (setAction: ToothAction) => setAction.type === ActionType.DIAGNOSTIC && !setAction.surfaceId)
	}


	setActions(actions: ToothAction[]) {
		this.actionsOnTeeth = actions || [];
		this.currentDrawEmitter.next(this.getCurrentDraw());
	}

	getAllActions() {
		return this.actionsOnTeeth;
	}

	private execute(actionSctid: string, surfacesIds: string[], type: ActionType, filterFuncion, order?: ProcedureOrder) {
		if (!surfacesIds.length) {
			const toothAction = this.actionsOnTeeth.find(filterFuncion);
			if (toothAction) {
				toothAction.actionSctid = actionSctid;
			} else {
				this.addToothAction(actionSctid, type, order);
			}
		} else {
			surfacesIds.forEach(id => {
				const toBeReplaced = this.toReplace(id, type);
				if (toBeReplaced) {
					toBeReplaced.actionSctid = actionSctid;
				} else {
					this.addSurfaceAction(id, actionSctid, type);
				}
			});
		}

		this.currentDrawEmitter.next(this.getCurrentDraw());
	}

	private toReplace(surfaceId: string, type: ActionType): ToothAction {
		return this.actionsOnTeeth.find(a => a.type === type && a.surfaceId === surfaceId);
	}

	private addToothAction(actionSctid: string, type: ActionType, wholeProcedureOrder?: ProcedureOrder) {
		const toAdd: ToothAction[] = [{ actionSctid, type, wholeProcedureOrder }];
		this.actionsOnTeeth = this.actionsOnTeeth.concat(toAdd);
	}

	private addSurfaceAction = (surfaceId: string, actionSctid: string, type: ActionType) => {
		const toAdd = { actionSctid, surfaceId, type };
		this.actionsOnTeeth = this.actionsOnTeeth.concat(toAdd);
	}

	private getCurrentDraw(): CurrentDraw {

		const currentDraw: CurrentDraw = {
			whole: null,
			central: null,
			external: null,
			right: null,
			left: null,
			internal: null,
		}

		const borrarDibujoCaras = () => {
			currentDraw.whole = null;
			currentDraw.central = null;
			currentDraw.external = null;
			currentDraw.right = null;
			currentDraw.left = null;
			currentDraw.internal = null;
		};

		const actualizarDibujoCara = (toothAction: ToothAction) => {
			currentDraw[toothAction.surfaceId] = { sctid: toothAction.actionSctid, type: toothAction.type }
		}

		this.actionsOnTeeth?.forEach(
			toothAction => {
				if (toothAction.surfaceId) {
					if (toothAction.type === ActionType.DIAGNOSTIC && currentDraw.whole?.type === ActionType.PROCEDURE) {
						return;
					}
					const surfaceHasProcedure = currentDraw[toothAction.surfaceId]?.type === ActionType.PROCEDURE; // Si es false entonces o no tiene nada o tiene hallazgo ==> dibuja
					if ((!surfaceHasProcedure)) {
						actualizarDibujoCara(toothAction);
					}
				} else { // Es pieza
					if (toothAction.type === ActionType.PROCEDURE) {
						borrarDibujoCaras();
						currentDraw.whole = { sctid: toothAction.actionSctid, type: toothAction.type }
					} else if (!currentDraw.whole) {
						currentDraw.whole = { sctid: toothAction.actionSctid, type: toothAction.type }
					}
				}
			}
		)
		return currentDraw;

	}
}


export interface ToothAction {
	surfaceId?: string;
	actionSctid: string;
	type: ActionType;
	wholeProcedureOrder?: ProcedureOrder
};

interface Action {
	sctid: string;
	type: ActionType
}

export interface CurrentDraw {
	whole: Action
	left: Action,
	internal: Action,
	right: Action,
	external: Action,
	central: Action,
}

export enum ProcedureOrder {
	FIRST, SECOND, THIRD
}

export enum ActionType {
	PROCEDURE, DIAGNOSTIC, RECORD
}
