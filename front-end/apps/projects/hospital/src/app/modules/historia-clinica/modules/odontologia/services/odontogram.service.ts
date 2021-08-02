import { Injectable } from '@angular/core';
import { ToothAction } from './actions-service.service';
@Injectable({
	providedIn: 'root'
})
export class OdontogramService {

	constructor() { }


	actionedTeeth: {
		sctid: string;
		actions: ToothAction[];
	}[] = [];

	setActionsTo(actions: ToothAction[], sctid: string) {

		const tooth = this.actionedTeeth?.find(s => s.sctid === sctid);
		if (!tooth) {
			this.actionedTeeth.push({ sctid, actions });
			return;
		}
		tooth.actions = actions;
	}

	getActionsFrom(sctid: string): ToothAction[] {
		const toothActioned = this.actionedTeeth.find(s => s.sctid === sctid);
		return toothActioned ? toothActioned.actions : null;
	}

	resetOdontogram() {
		this.actionedTeeth = [];
	}
}
