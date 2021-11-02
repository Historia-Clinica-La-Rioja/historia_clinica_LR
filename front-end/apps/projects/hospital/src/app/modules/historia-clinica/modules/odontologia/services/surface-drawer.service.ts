import { BehaviorSubject } from 'rxjs';
import { Observable } from 'rxjs';
import { ActionAndColor, getDraw, getHtmlName, getPaint } from '../utils/draws';

export class SurfaceDrawerService {

	currentDraw: HTMLElement[];
	currentPaint: ActionAndColor[];

	private isClickedEmitter = new BehaviorSubject(false);
	isClicked$: Observable<boolean> = this.isClickedEmitter.asObservable();

	constructor(public node, private readonly toothTreatment) {
		this.node = node;
		if (this.toothTreatment === ToothTreatment.AS_FRACTIONAL_TOOTH) {
			this.node.funcionDeOver = () => {
				this.updateFillColor(State.OVER);
			};

			this.node.funcionDeOut = () => {
				this.updateFillColor(undefined);
			};

			this.node.funcionDeClick = () => {
				this.isClickedEmitter.next(true);
				this.node.removeEventListener('mouseover', this.node.funcionDeOver);
				this.node.removeEventListener('mouseout', this.node.funcionDeOut);
				this.node.removeEventListener('click', this.node.funcionDeClick);

				this.node.addEventListener('click', this.node.funcionSegundoClick);
			};

			this.node.funcionSegundoClick = () => {
				this.isClickedEmitter.next(false);
				this.node.addEventListener('mouseover', this.node.funcionDeOver);
				this.node.addEventListener('mouseout', this.node.funcionDeOut);
				this.node.addEventListener('click', this.node.funcionDeClick);
				this.node.removeEventListener('click', this.node.funcionSegundoClick);
			};

			this.node.addEventListener('click', this.node.funcionDeClick);
			this.node.addEventListener('mouseover', this.node.funcionDeOver);
			this.node.addEventListener('mouseout', this.node.funcionDeOut);

		}
	}

	updateOutlineColor(state: State) {
		let color = StateOutlineColor[state];

		const currentStrokeAction = this.currentPaint?.find(action => action.action === 'stroke');
		if (currentStrokeAction) {
			color = currentStrokeAction.color;
		}

		this.node.setAttribute('stroke', color);
		if (this.currentPaint || state === State.SELECTED) { // Por el tema de los strokes superpuestos
			const clonedParent = this.node.parentNode;
			clonedParent.removeChild(this.node);
			clonedParent.appendChild(this.node);
		}
		if (this.currentDraw) {
			this.draw(this.currentDraw);
		}
	}

	updateFillColor(state: State) {

		let color = StateFillColor[State.CLEAN];
		if (StateFillColor[state]) {
			color = StateFillColor[state];
		} else {
			if (this.currentPaint) {
				color = this.currentPaint.find(action => action.action === 'fill').color;
			}
		}
		this.node.setAttribute('fill', color);
	}

	setNewDraw(sctid: string) {
		const svgName = getHtmlName(sctid);
		this.removePreviousDraw();
		if (!svgName) {
			const actions: ActionAndColor[] = getPaint(sctid);
			actions?.forEach(action => {
				this.node.setAttribute(action.action, action.color);
			});
			this.currentPaint = actions;
			return;
		}
		const toDraw = getDraw(this.node.id, svgName);
		this.currentDraw = toDraw;
		this.draw(toDraw);
	}

	removePreviousDraw() {
		if (this.currentDraw) {
			this.currentDraw.forEach(node => {
				this.node.parentNode.removeChild(node);
			});
			this.currentDraw = undefined;
		} else {
			if (this.currentPaint) {
				this.currentPaint = undefined;
				this.updateFillColor(State.CLEAN);
				this.updateOutlineColor(State.SELECTED); // Para poder borrar un dibujo tengo que estar parado sobre la cara ==> Selected
			}
		}
	}

	private draw(toDraw: HTMLElement[]) {
		toDraw.forEach(path => {
			this.node.parentNode.appendChild(path);
		});
	}

}

export enum State {
	SELECTED,
	NOT_SELECTED,
	CLEAN,
	OVER,
}

const StateOutlineColor = {
	[State.SELECTED]: '#4B4B4B',
	[State.NOT_SELECTED]: 'grey',
	[State.CLEAN]: '#4B4B4B'
};

const StateFillColor = {
	[State.OVER]: 'grey',
	[State.CLEAN]: 'white'
}

export enum ToothTreatment {
	AS_WHOLE_TOOTH, AS_FRACTIONAL_TOOTH
}
