import { Injectable } from '@angular/core';
import { deepClone } from '@core/utils/core.utils';
import { ReplaySubject } from 'rxjs';
import { ToothAction } from './actions.service';
@Injectable({
	providedIn: 'root'
})
export class OdontogramService {

	constructor() { }

	actionedTeeth: ActionedTooth[] = [];

	private actionsSubject = new ReplaySubject<ActionedTooth>(1);
	actionsSubject$ = this.actionsSubject.asObservable()


	setActionsTo(actions: ToothAction[], sctid: string, toothCompleteNumber: string) {
		const toEmit = { sctid, actions, toothCompleteNumber };
		this.actionsSubject.next(toEmit);
		const tooth = this.actionedTeeth?.find(s => s.sctid === sctid);
		if (!tooth) {
			this.actionedTeeth.push({ sctid, actions, toothCompleteNumber });
			return;
		}
		tooth.actions = actions;
	}

	getActionsFrom(sctid: string): ToothAction[] {
		const toothActioned = this.actionedTeeth.find(s => s.sctid === sctid);
		return toothActioned ? deepClone(toothActioned.actions): null;
	}

	resetOdontogram() {
		this.actionedTeeth.forEach(actionedTooth => {
			this.actionsSubject.next({ sctid: actionedTooth.sctid, actions: [], toothCompleteNumber: null })
		})
		this.actionedTeeth = [];
	}

}

export interface ActionedTooth {
	sctid: string;
	toothCompleteNumber: string;
	actions: ToothAction[];
}
