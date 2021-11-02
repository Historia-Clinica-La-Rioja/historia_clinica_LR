import { ProcedureOrder } from "@historia-clinica/modules/odontologia/services/actions.service";

export class ShowActionsService {

	public showSecondProcedure: boolean;
	public showThirdProcedure: boolean;
	public isNotPreviousProcedureSet = true;


	constructor(secondProcedure, thirdProcedure, isNotPreviousProcedureSet) {
		this.showSecondProcedure = secondProcedure ? true : false;
		this.showThirdProcedure = thirdProcedure ? true : false;
		this.isNotPreviousProcedureSet = (isNotPreviousProcedureSet === -1 || isNotPreviousProcedureSet === 2) ? true : false;
	}

	public getSecondProcedure(): boolean {
		return this.showSecondProcedure;
	}

	public getThirdProcedure(): boolean {
		return this.showThirdProcedure;
	}

	public getIsNotPreviousProcedureSet(): boolean {
		return this.isNotPreviousProcedureSet;
	}

	public setSecondProcedure(showSecondProcedure: boolean): void {
		this.showSecondProcedure = showSecondProcedure;
	}

	public setThirdProcedure(showThirdProcedure: boolean): void {
		this.showThirdProcedure = showThirdProcedure;
	}

	public setIsNotPreviousProcedureSet(isNotPreviousProcedure: boolean): void {
		this.isNotPreviousProcedureSet = isNotPreviousProcedure;
	}

	public showProcedures(order: number, isANewProcedure: boolean): void {
		if (isANewProcedure) {
			switch (order) {
				case ProcedureOrder.SECOND:
					this.showSecondProcedure = true;
					this.showThirdProcedure = true;
					break;
				case ProcedureOrder.FIRST:
					this.showSecondProcedure = true;
					break;
			}
		} else {
			switch (order) {
				case ProcedureOrder.SECOND:
					this.showThirdProcedure = false;
					break;
				default :
					this.showSecondProcedure = false;
					this.showThirdProcedure = false;
					break;
			}
		}
	}
}

