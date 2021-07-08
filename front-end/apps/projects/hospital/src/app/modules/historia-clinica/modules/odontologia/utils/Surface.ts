import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { State, SurfaceDrawerService } from '../services/surface-drawer.service';

export class Surface {


	constructor(private surfaceDrawerService: SurfaceDrawerService) {

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
	/**
	 * Se dibuja en base a si esta seleccionada (info interna) y a si tiene hallazgo o procedimiento ( tambien info interna)
	 * El problema esta en que los dibujos (nodos svgs) tienen que ser hermanos de cada path de la superficie (cara)
	 * La superficie se pinta de un color si hace falta y sino deriva a toothComponent el dibujo que tiene que agregar por ella
	 */
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


}
