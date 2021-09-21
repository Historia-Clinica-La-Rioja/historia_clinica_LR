import { BehaviorSubject } from 'rxjs';
import { State, SurfaceDrawerService } from '../services/surface-drawer.service';

export class Surface {


	constructor(
		public surfaceDrawerService: SurfaceDrawerService) {

		this.id = this.surfaceDrawerService.node.id;
		this.surfaceDrawerService.isClicked$.subscribe(clicked => {
			this.setIsSelected(clicked);
		});
	}

	id: string;
	node: any;
	isSelected: boolean;
	findingId: string;
	procedureId: string;

	private isSelectedEmitter = new BehaviorSubject(false); // To ToothComponent
	isSelected$ = this.isSelectedEmitter.asObservable();
	setFinding(findingId: string, hasToothProcedure: boolean): void {
		if (findingId) {
			this.findingId = findingId;
			if (!hasToothProcedure && !this.procedureId) {
				this.surfaceDrawerService.setNewDraw(findingId);
			}
		}
	}

	setProcedure(procedureId: string): void {
		this.procedureId = procedureId;
		this.surfaceDrawerService.setNewDraw(procedureId);
	}

	setIsSelected(isSelected: boolean) {
		this.isSelected = isSelected;
		this.isSelectedEmitter.next(isSelected);
	}

	updateOutlineColor(atLeastOneSelected: boolean) {
		let state = State.CLEAN;
		if (atLeastOneSelected) {
			state = this.isSelected ? State.SELECTED : State.NOT_SELECTED;
		}
		this.surfaceDrawerService.updateOutlineColor(state);
	}

	clear() {
		this.setFinding(null,false);
		this.setProcedure(null);
	}

}

export enum ToothSurfaceId {
	WHOLE = 'whole',
	RIGHT = 'right',
	LEFT = 'left',
	EXTERNAL = 'external',
	INTERNAL = 'internal',
	CENTRAL = 'central'
}
