import { AfterViewInit, Component, ElementRef, Input, Output, ViewChild } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ActionsService, CurrentDraw, ProcedureOrder, ToothAction, ActionType } from '../../services/actions.service';
import { SurfaceDrawerService, ToothTreatment } from '../../services/surface-drawer.service';
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

	private actionsService = new ActionsService();
	private surfacesHandler: Surface[] = [];
	private svgGroup: ChildNode;

	private toothDrawerService: ToothDrawerService;

	@Input()
	set records(records: ToothAction[]) {
		this.setRecords(records);
	}

	setRecords(records: ToothAction[]) {
		this.actionsService.setRecords(records);
	}

	@Input()
	set findingsAndProcedures(actions: ToothAction[]) {
		this.setFindingsAndProcedures(actions);
	}

	@Input()
	set newFinding(newFinding: string) {
		if (newFinding) {
			const selectedSurfacesIds: string[] = this.surfacesHandler.filter(node => node.isSelected).map(s => s.id);
			this.actionsService.addFinding(newFinding, selectedSurfacesIds);
		}
	}

	@Input()
	set setFirstProcedure(sctid: string) {
		if (sctid) {
			this.setProcedureInPosition(sctid, ProcedureOrder.FIRST);
		}
	}

	@Input()
	set setSecondProcedure(sctid: string) {
		if (sctid) {
			this.setProcedureInPosition(sctid, ProcedureOrder.SECOND);
		}
	}

	@Input()
	set setThirdProcedure(sctid: string) {
		if (sctid) {
			this.setProcedureInPosition(sctid, ProcedureOrder.THIRD);
		}
	}

	@Input() toothTreatment: ToothTreatment = ToothTreatment.AS_FRACTIONAL_TOOTH;

	@Output() selectedSurfacesEmitter = new BehaviorSubject<any[]>([]);
	private actionsSubject = new BehaviorSubject<Actions>(null);
	actionsSubject$ = this.actionsSubject.asObservable();

	ngAfterViewInit(): void {
		this.svgGroup = this.svg.nativeElement.firstChild;
		this.toothDrawerService = new ToothDrawerService(this.svgGroup);
		const svgSurfaces: any[] = Array.from(this.svgGroup.firstChild.childNodes);
		svgSurfaces
			.forEach(SVGSurface => {
				const surfaceDrawer = new SurfaceDrawerService(SVGSurface, this.toothTreatment);
				const surf = new Surface(surfaceDrawer);
				this.surfacesHandler.push(surf);

				if (this.toothTreatment === ToothTreatment.AS_FRACTIONAL_TOOTH) {
					surf.isSelected$.subscribe(isSelectedChanged => {
						this.surfacesHandler.forEach(surface => {
							const atLeastOneSurfaceSelected = this.surfacesHandler.filter(s => s.isSelected).length > 0;
							surface.updateOutlineColor(atLeastOneSurfaceSelected);
						});

						const selectedSurfaces = this.surfacesHandler.filter(s => s.isSelected).map(s => s.id);
						this.selectedSurfacesEmitter.next(selectedSurfaces);

						this.emitFindingAndProcedures();
					});
				} else {
					this.svg.nativeElement.addEventListener('mouseover', () => this.svg.nativeElement.style.backgroundColor = Colors.TEETH_OVER);
					this.svg.nativeElement.addEventListener('mouseout', () => this.svg.nativeElement.style.backgroundColor = Colors.TEETH_MOUSE_OUT);
				}
			});

		this.actionsService.currentDraw$.subscribe((currentDraw: CurrentDraw) => {
			this.toothDrawerService.updateDraw(currentDraw?.whole?.sctid);
			Object.keys(currentDraw).forEach(key => {
				this.surfacesHandler.find(s => s.id === key)?.surfaceDrawerService.removePreviousDraw();
				if (currentDraw[key]) {
					this.surfacesHandler.find(s => s.id === key)?.surfaceDrawerService.setNewDraw(currentDraw[key].sctid);
				}
			})
		});

		this.emitFindingAndProcedures();
	}

	getFindingsAndProcedures(): ToothAction[] {
		return this.actionsService.getAllActions();
	}

	setFindingsAndProcedures(actions: ToothAction[]) {
		this.actionsService.setActions(actions);
		this.emitFindingAndProcedures();
	}

	private emitFindingAndProcedures() {
		const result = this.findToothCurrentViewActions();
		this.actionsSubject.next(result);
	}

	private findToothCurrentViewActions(): Actions {

		const selectedSurfacesIds: string[] = this.surfacesHandler.filter(node => node.isSelected).map(surface => surface.id);

		if (!selectedSurfacesIds.length) {

			const actions = this.actionsService.getActions();
			return {
				findingId: actions.find(a => a.action.type === ActionType.DIAGNOSTIC)?.action.sctid,
				procedures: {
					firstProcedureId: actions.find(a => a.wholeProcedureOrder === ProcedureOrder.FIRST)?.action.sctid,
					secondProcedureId: actions.find(a => a.wholeProcedureOrder === ProcedureOrder.SECOND)?.action.sctid,
					thirdProcedureId: actions.find(a => a.wholeProcedureOrder === ProcedureOrder.THIRD)?.action.sctid
				}
			}
		}


		const actions: ToothAction[] = this.actionsService.getActions(selectedSurfacesIds);
		const findingsSctids: string[] = actions.filter(a => a.action.type === ActionType.DIAGNOSTIC).map(a => a.action.sctid);
		const proceduresSctids: string[] = actions.filter(a => a.action.type === ActionType.PROCEDURE).map(a => a.action.sctid);

		const count = (array: string[]) =>
			array.reduce((a, b) => ({
				...a,
				[b]: (a[b] || 0) + 1
			}), {});

		const inCommon = dict =>
			Object.keys(dict).filter((a) => dict[a] === selectedSurfacesIds.length);
		return {
			findingId: inCommon(count(findingsSctids)).pop(),
			procedures: {
				firstProcedureId: inCommon(count(proceduresSctids)).pop()
			}
		};
	}

	private setProcedureInPosition(sctid: string, order: ProcedureOrder) {
		if (sctid) {
			const selectedSurfacesIds: string[] = this.surfacesHandler.filter(node => node.isSelected).map(s => s.id);
			this.actionsService.addProcedure(sctid, selectedSurfacesIds, order);
		}
	}

	public deleteAction(sctid: string, selectedSurfaces: string[], actionType: ActionType, order: ProcedureOrder){
		if (!selectedSurfaces.length){
			this.actionsService.deleteAction(undefined, sctid, actionType, order);
		}
		else {
			selectedSurfaces.forEach(
				surface => {
					this.actionsService.deleteAction(surface, sctid, actionType, order);
				}
			);
		}
	}
}

export interface Actions {
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
