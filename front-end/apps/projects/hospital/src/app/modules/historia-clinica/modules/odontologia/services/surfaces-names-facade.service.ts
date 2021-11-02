import { Injectable } from '@angular/core';
import { ToothSurfacesDto } from '@api-rest/api-model';
import { BehaviorSubject } from 'rxjs';
import { OdontogramService as OdontogramRestService } from '../api-rest/odontogram.service';
import { getSurfaceShortName } from '../utils/surfaces';

@Injectable({
	providedIn: 'root'
})
export class SurfacesNamesFacadeService {

	private surfacesNames: ToothSurfaceNames[] = [];

	private namesSubject = new BehaviorSubject<ToothSurfaceNames>(null);
	toothSurfaceNames$ = this.namesSubject.asObservable();

	constructor(
		private odontogramRestService: OdontogramRestService
	) { }

	loadSurfaceNameOf(toothSctid: string) {

		const toothSurfaceNames: ToothSurfaceNames = this.surfacesNames.find(a => a.toothSctid === toothSctid);
		if (toothSurfaceNames) {
			this.namesSubject.next(toothSurfaceNames);
			return;
		}

		this.odontogramRestService.getToothSurfaces(toothSctid)
			.subscribe(dto => {
				this.surfacesNames.push({ toothSctid, names: dto });
				this.namesSubject.next({ toothSctid, names: dto });
			});

	}

	getToothSurfaceShortName(toothSctid: string, surfaceId: string): string {
		if (!surfaceId) {
			return undefined;
		}
		const a = this.surfacesNames.find(t => t.toothSctid === toothSctid);
		const surfaceSctid: string = a.names[surfaceId].sctid;
		return getSurfaceShortName(surfaceSctid);
	}
}

export interface ToothSurfaceNames {
	toothSctid: string;
	names: ToothSurfacesDto
}
