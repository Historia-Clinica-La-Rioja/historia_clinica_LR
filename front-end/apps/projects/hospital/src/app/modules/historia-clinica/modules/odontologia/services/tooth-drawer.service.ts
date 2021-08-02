import { getDraw, getHtmlName } from '../utils/draws';
import { SurfaceDrawerService } from './surface-drawer.service';

export class ToothDrawerService {

	constructor(public toothNode: ChildNode) { }
	private currentDraw;

	updateDraw(sctid: string) {
		this.removePreviousDraw();

		if (sctid) {
			const svgName = getHtmlName(sctid);
			if (!svgName) {
				// Que hacer con acciones que no tienen dibujo para pieza
				return;
			}
			const newDrawNodes: HTMLElement[] = getDraw('central', svgName);
			this.draw(newDrawNodes);
		}
	}

	private draw(toDraw: any[]) {
		this.currentDraw = toDraw;
		toDraw?.forEach(path => {
			this.toothNode.appendChild(path);
		});
	}

	private removePreviousDraw() {
		if (this.currentDraw) {
			this.currentDraw.forEach(node => {
				this.toothNode.removeChild(node);
			});
			this.currentDraw = undefined;
		}
	}
}
