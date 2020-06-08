import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PublicService } from '@api-rest/services/public.service';
import { map } from 'rxjs/operators';

const FLAVOR_TANDIL = 'tandil';
const FLAVOR_CHACO = 'chaco';
const FLAVOR_HOSPITALES = 'minsal';
const FEATURE_FLAGS = [
	{
		name: 'habilitarEditarPaciente',
		flavorMatch: [FLAVOR_TANDIL, FLAVOR_CHACO]
	},
	{
		name: 'agregarContactoResponsable',
		flavorMatch: [FLAVOR_TANDIL, FLAVOR_CHACO]
	},
	{
		name: 'agregarMedicosAdicionales',
		flavorMatch: [FLAVOR_TANDIL, FLAVOR_CHACO]
	},
	{
		name: 'habilitarServicioRenaper',
		flavorMatch: [FLAVOR_HOSPITALES]
	},
	{
		name: 'habilitarAltaSinEpicrisis',
		flavorMatch: [FLAVOR_TANDIL, FLAVOR_CHACO]
	},
	{
		name: 'responsibleDoctorRequired',
		flavorMatch: [FLAVOR_HOSPITALES]
	},
	{
		name: 'mainDiagnosisRequired',
		flavorMatch: [FLAVOR_HOSPITALES]
	},
	{
		name: 'epicrisisRequired',
		flavorMatch: [FLAVOR_HOSPITALES]
	}
];

@Injectable({
	providedIn: 'root'
})
export class FeatureFlagService {

	constructor(
		private publicService: PublicService
	) {}

	public isOn(ff: string): Observable<boolean> {
		return this.publicService.getInfo().pipe(map(data => {
			const ffObject = this.getIfIsPresent(ff);
			return ffObject && ffObject.flavorMatch.includes(data.flavor);
		}));
	}

	private getIfIsPresent(ff: string) {
		return FEATURE_FLAGS.find(element => element.name === ff);
	}

}
