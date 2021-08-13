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
		const action: Action = { sctid: actionSctid, type: ActionType.PROCEDURE }
		this.execute(action, surfacesIds, (setAction: ToothAction) => setAction.wholeProcedureOrder === order && !setAction.surfaceId, order);
	}

	setFinding(actionSctid: string, surfacesIds: string[]) {
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
			currentDraw[toothAction.surfaceId] = toothAction.action
		}

		this.actionsOnTeeth?.forEach(
			toothAction => {
				if (toothAction.surfaceId) {
					if (toothAction.action.type === ActionType.DIAGNOSTIC && currentDraw.whole?.type === ActionType.PROCEDURE) {
						return;
					}
					const surfaceHasProcedure = currentDraw[toothAction.surfaceId]?.type === ActionType.PROCEDURE; // Si es false entonces o no tiene nada o tiene hallazgo ==> dibuja
					if ((!surfaceHasProcedure)) {
						actualizarDibujoCara(toothAction);
					}
				} else { // Es pieza
					if (toothAction.action.type === ActionType.PROCEDURE) {
						borrarDibujoCaras();
						currentDraw.whole = toothAction.action
					} else if (!currentDraw.whole) {
						currentDraw.whole = toothAction.action
					}
				}
			}
		)
		return currentDraw;

	}
}


export interface ToothAction {
	surfaceId?: string;
	wholeProcedureOrder?: ProcedureOrder
	action: Action
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
