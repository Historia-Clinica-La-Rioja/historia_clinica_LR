import { Injectable } from '@angular/core';
import { AugmentativeMedicationPresentation } from '../constants/medication-presentation';

@Injectable({
  	providedIn: 'root'
})
export class DigitalPrescriptionUnitPluralizeService {

	run = (presentation: string): string | undefined => {
		const mappings: { [key in AugmentativeMedicationPresentation]?: PresentationPluralized } = {
			[AugmentativeMedicationPresentation.COMPRIMIDO]: PresentationPluralized.COMPRIMIDO,
			[AugmentativeMedicationPresentation.CAPSULA]: PresentationPluralized.CAPSULA,
			[AugmentativeMedicationPresentation.AMPOLLA]: PresentationPluralized.AMPOLLA,
			[AugmentativeMedicationPresentation.GOMA_DE_MASCAR]: PresentationPluralized.GOMA_DE_MASCAR,
			[AugmentativeMedicationPresentation.APOSITO]: PresentationPluralized.APOSITO,
			[AugmentativeMedicationPresentation.OVULO_VAGINAL]: PresentationPluralized.OVULO_VAGINAL,
			[AugmentativeMedicationPresentation.PASTILLA]: PresentationPluralized.PASTILLA,
			[AugmentativeMedicationPresentation.SUPOSITORIO]: PresentationPluralized.SUPOSITORIO,
			[AugmentativeMedicationPresentation.PARCHE]: PresentationPluralized.PARCHE,
			[AugmentativeMedicationPresentation.IMPLANTE]: PresentationPluralized.IMPLANTE,
			[AugmentativeMedicationPresentation.APLICADOR]: PresentationPluralized.APLICADOR,
			[AugmentativeMedicationPresentation.COMPRIMIDO_DE_DISOLUCION_ORAL]: PresentationPluralized.COMPRIMIDO_DE_DISOLUCION_ORAL,
			[AugmentativeMedicationPresentation.PILDORA_CHICA]: PresentationPluralized.PILDORA_CHICA,
			[AugmentativeMedicationPresentation.CILINDRO]: PresentationPluralized.CILINDRO,
			[AugmentativeMedicationPresentation.APLICACION]: PresentationPluralized.APLICACION,
		};
	
		return mappings[presentation] || presentation;
	};
}

export enum PresentationPluralized {
	COMPRIMIDO = 'comprimido/s',
	CAPSULA = 'cápsula/s',
	AMPOLLA = 'ampolla/s',
	GOMA_DE_MASCAR = 'goma/s de mascar',
	APOSITO = 'apósito/s',
	OVULO_VAGINAL = 'óvulo/s vaginal/es',
	PASTILLA = 'pastilla/s',
	SUPOSITORIO = 'supositorio/s',
	PARCHE = 'parche/s',
	IMPLANTE = 'implante/s',
	APLICADOR = 'aplicador/es',
	COMPRIMIDO_DE_DISOLUCION_ORAL = 'comprimido/s',
	PILDORA_CHICA = 'píldora/s chica/s',
	CILINDRO = 'cilindro/s',
	APLICACION = 'aplicación/es'
} 