import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PublicService } from '@api-rest/services/public.service';
import { map } from 'rxjs/operators';
import { AppFeature } from '@api-rest/api-model';
import { MenuItem } from '@core/core-model';

const FLAVOR_TANDIL = 'tandil';
const FLAVOR_CHACO = 'chaco';
const FLAVOR_HOSPITALES = 'minsal';
const FLAVOR_PBA = 'pba';
const FEATURE_FLAGS = [
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
		flavorMatch: [FLAVOR_HOSPITALES, FLAVOR_PBA]
	},
	{
		name: 'mainDiagnosisRequired',
		flavorMatch: [FLAVOR_HOSPITALES, FLAVOR_PBA]
	},
	{
		name: 'epicrisisRequired',
		flavorMatch: [FLAVOR_HOSPITALES, FLAVOR_PBA]
	},
	{
		name: 'habilitarBotonCovidDiagnosticoPresuntivo',
		flavorMatch: [FLAVOR_TANDIL]
	},
	{
		name: 'habilitarToggleFiltroDiagnosticoPrincipal',
		flavorMatch: [FLAVOR_HOSPITALES, FLAVOR_PBA]
	},
	{
		name: 'habilitarPaseCama',
		flavorMatch: [FLAVOR_HOSPITALES, FLAVOR_PBA]
	},
	{
		name: 'restringirDatosEditarPaciente',
		flavorMatch: [FLAVOR_HOSPITALES, FLAVOR_PBA]
	},
	{
		name: 'habilitarEdicionPacienteInternacion',
		flavorMatch: [FLAVOR_HOSPITALES, FLAVOR_PBA]
	},
];

@Injectable({
	providedIn: 'root'
})
export class FeatureFlagService {

	constructor(
		private publicService: PublicService
	) {}

	public isActive(feature: AppFeature): Observable<boolean> {
		return this.publicService.getInfo()
			.pipe(
				map(info => info.features.some(f => f === feature))
			);
	}

	public isOn(ff: string): Observable<boolean> {
		return this.publicService.getInfo().pipe(map(data => {
			const ffObject = this.getIfIsPresent(ff);
			return ffObject && ffObject.flavorMatch.includes(data.flavor);
		}));
	}

	/**
	 * Permite filtrar una lista según los feature flags activos.
	 * La lista debe contener objetos que tengan el atributo featureFlag de tipo AppFeature.
	 */
	public filterItems$(items: MenuItem[]): Observable<MenuItem[]> {
		return this.publicService.getInfo().pipe(map(info => {
			return items.filter(item => {
				if (item.featureFlag) {
					return info.features.some(ffActive => ffActive === item.featureFlag);
				}
				return true;
			});
		}));
	}

	private getIfIsPresent(ff: string) {
		return FEATURE_FLAGS.find(element => element.name === ff);
	}

}
