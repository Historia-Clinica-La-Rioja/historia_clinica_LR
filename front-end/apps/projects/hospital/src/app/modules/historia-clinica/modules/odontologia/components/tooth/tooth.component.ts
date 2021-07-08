import { AfterViewInit, Component, ElementRef, Input, Output, ViewChild } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { SurfaceDrawerService } from '../../services/surface-drawer.service';
import { ToothDrawerService } from '../../services/tooth-drawer.service';
import { Surface } from '../../utils/Surface';

@Component({
	selector: 'app-tooth',
	templateUrl: './tooth.component.html',
	styleUrls: ['./tooth.component.scss']
})
export class ToothComponent implements AfterViewInit {

	constructor() { }

	@ViewChild('svg') svg: ElementRef<HTMLElement>;

	private surfacesHandler: Surface[] = [];
	private svgGroup: ChildNode;
	private wholeToothActions: {
		findingId: string;
		procedures: { firstProcedureId: string, secondProcedureId?: string, thirdProcedureId?: string }
	} = {
			findingId: undefined,
			procedures: undefined
		};

	private toothDrawerService: ToothDrawerService;

	@Input()
	set newFinding(newFinding: string) {
		if (newFinding) {
			const selectedSurfaces = this.surfacesHandler.filter(node => node.isSelected);
			if (!selectedSurfaces.length) {
				this.wholeToothActions.findingId = newFinding;
				if (!this.wholeToothActions.procedures) {
					this.toothDrawerService.updateDraw(newFinding, false);
				}
			} else {
				selectedSurfaces.forEach(surface => {
					surface.setFinding(newFinding, this.hasToothProcedure());
				});
			}
		}
	}

	@Input()
	set setProcedure(procedures: { firstProcedureId: string, secondProcedureId?: string, thirdProcedureId?: string }) {
		if (procedures) {
			const selectedSurfaces = this.surfacesHandler.filter(node => node.isSelected);
			if (!selectedSurfaces?.length) {
				this.wholeToothActions.procedures = procedures;
				this.toothDrawerService.updateDraw(findProcedureIdToBeDraw(), true);
			} else {
				selectedSurfaces.forEach(surface => {
					surface.setProcedure(procedures.firstProcedureId);
				});
			}
		}

		function findProcedureIdToBeDraw(): string {
			if (procedures.thirdProcedureId) {
				return procedures.thirdProcedureId;
			}
			if (procedures.secondProcedureId) {
				return procedures.secondProcedureId;
			}
			return procedures.firstProcedureId;
		}
	}

	@Input() toothTreatment: ToothTreatment = ToothTreatment.AS_FRACTIONAL_TOOTH;

	@Output() selectedSurfacesEmitter = new BehaviorSubject<any[]>([]);
	@Output() commonActionsEmitter = new BehaviorSubject<CommonActions>(null);

	ngAfterViewInit(): void {
		this.svgGroup = this.svg.nativeElement.firstChild;
		this.toothDrawerService = new ToothDrawerService(this.svgGroup);
		const svgSurfaces: any[] = Array.from(this.svgGroup.firstChild.childNodes);
		if (this.toothTreatment === ToothTreatment.AS_FRACTIONAL_TOOTH) {
			svgSurfaces
				.forEach(SVGSurface => {

					const surfaceDrawer = new SurfaceDrawerService(SVGSurface);
					this.toothDrawerService.addSurfaceDrawer(surfaceDrawer);
					const surf = new Surface(surfaceDrawer);
					this.surfacesHandler.push(surf);

					surf.isSelected$.subscribe(isSelectedChanged => {
						this.surfacesHandler.forEach(surface => {
							const atLeastOneSurfaceSelected = this.surfacesHandler.filter(s => s.isSelected).length > 0;
							surface.updateOutlineColor(atLeastOneSurfaceSelected);
						});

						const selectedSurfaces = this.surfacesHandler.filter(s => s.isSelected).map(s => s.id);
						this.selectedSurfacesEmitter.next(selectedSurfaces);

						this.emitFindingAndProcedures();

					});
				});
		} else {
			this.svg.nativeElement.addEventListener('mouseover', () => this.svg.nativeElement.style.backgroundColor = Colors.TEETH_OVER);
			this.svg.nativeElement.addEventListener('mouseout', () => this.svg.nativeElement.style.backgroundColor = Colors.TEETH_MOUSE_OUT);
		}
	}

	private emitFindingAndProcedures() {

		const result = this.findCommonActions();
		this.commonActionsEmitter.next(result);
	}


	private findCommonActions(): CommonActions {

		const selectedSurfacesIds: string[] = this.surfacesHandler.filter(node => node.isSelected).map(surface => surface.id);

		if (!selectedSurfacesIds.length) {
			return this.wholeToothActions;
		}

		const findingsIds: string[] = this.surfacesHandler.filter(s => s.isSelected).map(surface => surface.findingId);
		const proceduresIds: string[] = this.surfacesHandler.filter(s => s.isSelected).map(surface => surface.procedureId);
		const count = (array: string[]) =>
			array.reduce((a, b) => ({
				...a,
				[b]: (a[b] || 0) + 1
			}), {});

		const inCommon = dict =>
			Object.keys(dict).filter((a) => dict[a] === selectedSurfacesIds.length);

		return {
			findingId: inCommon(count(findingsIds)).pop(),
			procedures: {
				firstProcedureId: inCommon(count(proceduresIds)).pop()
			}
		};
	}


	private hasToothProcedure(): boolean {
		if (this.wholeToothActions?.procedures?.firstProcedureId || this.wholeToothActions?.procedures?.secondProcedureId
			|| this.wholeToothActions?.procedures?.thirdProcedureId) {
			return true;
		}
		return false;
	}
}

export enum ToothTreatment {
	AS_WHOLE_TOOTH, AS_FRACTIONAL_TOOTH
}

export interface CommonActions {
	findingId: string;
	procedures: {
		firstProcedureId: string,
		secondProcedureId?: string,
		thirdProcedureId?: string
	};
}

enum Colors {
	TEETH_OVER = '#F8F8F8',
	TEETH_MOUSE_OUT = 'transparent',
}
