import { Injectable } from '@angular/core';
import { ToothDto } from '@api-rest/api-model';
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


	private actionedTeethSubject = new ReplaySubject<ActionedTooth[]>(1);
	actionedTeeth$ = this.actionedTeethSubject.asObservable()

	setActionsTo(actions: ToothAction[], toothCompleteNumber: string, toothDto: ToothDto) {
		const toEmit: ActionedTooth = { actions, toothCompleteNumber, tooth: toothDto };
		this.actionsSubject.next(toEmit);
		const tooth = this.actionedTeeth?.find(s => s.tooth.snomed.sctid === toothDto.snomed.sctid);
		if (!tooth) {
			this.actionedTeeth.push(toEmit);
			this.actionedTeethSubject.next(this.actionedTeeth);
			return;
		}
		tooth.actions = actions;
		this.actionedTeethSubject.next(this.actionedTeeth);
	}

	getActionsFrom(sctid: string): ToothAction[] {
		const toothActioned: ActionedTooth = this.actionedTeeth.find(s => s.tooth.snomed.sctid === sctid);
		return toothActioned ? deepClone(toothActioned.actions) : null;
	}

	existActionedTeeth(): boolean {
		return  !!this.actionedTeeth.find(a => a.actions.length);
	}

	resetOdontogram() {
		this.actionedTeeth.forEach(actionedTooth => {
			this.actionsSubject.next({ actions: [], toothCompleteNumber: actionedTooth.toothCompleteNumber, tooth: actionedTooth.tooth })
		})
		this.actionedTeeth = [];
		this.actionedTeethSubject.next(this.actionedTeeth);
	}

}

export interface ActionedTooth {
	tooth: ToothDto;
	toothCompleteNumber: string;
	actions: ToothAction[];
}
