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
		this.execute(actionSctid, surfacesIds, true, (setAction: ToothAction) => setAction.wholeProcedureOrder === order && !setAction.surfaceId, order);
	}

	setFinding(actionSctid: string, surfacesIds: string[]) {
		this.execute(actionSctid, surfacesIds, false, (setAction: ToothAction) => !setAction.isProcedure && !setAction.surfaceId)
	}


	setActions(actions: ToothAction[]) {
		this.actionsOnTeeth = actions || [];
		this.currentDrawEmitter.next(this.getCurrentDraw());
	}

	getAllActions() {
		return this.actionsOnTeeth;
	}

	private execute(actionSctid: string, surfacesIds: string[], isProcedure: boolean, filterFuncion, order?: ProcedureOrder) {
		if (!surfacesIds.length) {
			const toothAction = this.actionsOnTeeth.find(filterFuncion);
			if (toothAction) {
				toothAction.actionSctid = actionSctid;
			} else {
				this.addToothAction(actionSctid, isProcedure, order);
			}
		} else {
			surfacesIds.forEach(id => {
				const toBeReplaced = this.toReplace(id, isProcedure);
				if (toBeReplaced) {
					toBeReplaced.actionSctid = actionSctid;
				} else {
					this.addSurfaceAction(id, actionSctid, isProcedure);
				}
			});
		}

		this.currentDrawEmitter.next(this.getCurrentDraw());
	}

	private toReplace(surfaceId: string, isProcedure: boolean): ToothAction {
		return this.actionsOnTeeth.find(a => a.isProcedure === isProcedure && a.surfaceId === surfaceId);
	}

	private addToothAction(actionSctid: string, isProcedure: boolean, wholeProcedureOrder?: ProcedureOrder) {
		const toAdd: ToothAction[] = [{ actionSctid, isProcedure, wholeProcedureOrder }];
		this.actionsOnTeeth = this.actionsOnTeeth.concat(toAdd);
	}


	private addSurfaceAction = (surfaceId: string, actionSctid: string, isProcedure: boolean) => {
		const toAdd = { actionSctid, surfaceId, isProcedure };
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

		const actualizarDibujoCara = (toothAction) => {
			currentDraw[toothAction.surfaceId] = { sctid: toothAction.actionSctid, isProcedure: toothAction.isProcedure }
		}

		this.actionsOnTeeth?.forEach(
			toothAction => {
				if (toothAction.surfaceId) {
					if (!toothAction.isProcedure && currentDraw.whole?.isProcedure) {
						return;
					}
					const surfaceHasProcedure = currentDraw[toothAction.surfaceId]?.isProcedure; // Si es false entonces o no tiene nada o tiene hallazgo ==> dibuja
					if ((!surfaceHasProcedure)) {
						actualizarDibujoCara(toothAction);
					}
				} else { // Es pieza
					if (toothAction.isProcedure) {
						borrarDibujoCaras();
						currentDraw.whole = { sctid: toothAction.actionSctid, isProcedure: toothAction.isProcedure }
					} else if (!currentDraw.whole) {
						currentDraw.whole = { sctid: toothAction.actionSctid, isProcedure: toothAction.isProcedure }
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
	isProcedure: boolean;
	wholeProcedureOrder?: ProcedureOrder
};

interface Action {
	sctid: string;
	isProcedure: boolean
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
