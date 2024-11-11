export enum DiminutiveMedicationPresentation {
	COMPRIMIDO = 'comp.',
	CAPSULA = 'cáps.',
	AMPOLLA = 'amp.',
	GOMA_DE_MASCAR = 'gom.',
	APOSITO = 'após.',
	OVULO_VAGINAL = 'óv. vag.',
	PASTILLA = 'past.',
	SUPOSITORIO = 'sup.',
	PARCHE = 'parch.',
	ML = 'ml',
	GOTAS = 'gts',
	U = 'U',
	MG = 'mg',
	MCG = 'mcg',
	APLICACION = 'aplic.',
	IMPLANTE = 'impl.',
	APLICADOR = 'aplic.',
	COMPRIMIDO_DE_DISOLUCION_ORAL = 'comp.',
	PILDORA_CHICA = 'píld.',
	CILINDRO = 'cil.'
}

export enum AugmentativeMedicationPresentation {
    COMPRIMIDO = 'comprimido',
    CAPSULA = 'cápsula',
    AMPOLLA = 'ampolla',
    GOMA_DE_MASCAR = 'goma de mascar',
    APOSITO = 'apósito',
    OVULO_VAGINAL = 'óvulo vaginal',
    PASTILLA = 'pastilla',
    SUPOSITORIO = 'supositorio',
    PARCHE = 'parche',
    IMPLANTE = 'implante',
    APLICADOR = 'aplicador',
    COMPRIMIDO_DE_DISOLUCION_ORAL = 'comprimido de disolución oral',
    PILDORA_CHICA = 'píldora pequeña',
    CILINDRO = 'cilindro',
    JERINGA = 'jeringa',
    BOLSA = 'bolsa',
    BOLSA_CHICA = 'bolsa pequeña',
    PIPETA = 'pipeta',
    SACHET = 'sachet',
    VASO = 'vaso',
    ENVASE = 'envase',
    FRASCO = 'frasco',
    GOTAS = 'gotas',
    CARTUCHO = 'cartucho',
    VIAL = 'vial',
    LAPICERA = 'lapicera',
    LIOFILIZADO = 'liofilizado',
    TUBO = 'tubo',
    DISPARO = 'disparo',
    INHALADOR = 'inhalador',
    SISTEMA = 'sistema',
    TAMPON = 'tampón',
    APLICACION = 'aplicación'
}

export enum PresentationGroup {
    GROUP_ONE = 1,
    GROUP_TWO = 2,
    GROUP_THREE = 3,
}

const map = new Map();
map.set(AugmentativeMedicationPresentation.COMPRIMIDO, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.CAPSULA, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.AMPOLLA, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.GOMA_DE_MASCAR, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.APOSITO, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.OVULO_VAGINAL, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.PASTILLA, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.SUPOSITORIO, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.PARCHE, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.IMPLANTE, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.APLICADOR, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.COMPRIMIDO_DE_DISOLUCION_ORAL, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.PILDORA_CHICA, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.CILINDRO, PresentationGroup.GROUP_ONE);
map.set(AugmentativeMedicationPresentation.JERINGA, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.BOLSA, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.BOLSA_CHICA, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.PIPETA, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.SACHET, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.VASO, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.ENVASE, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.FRASCO, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.CARTUCHO, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.VIAL, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.LAPICERA, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.LIOFILIZADO, PresentationGroup.GROUP_TWO);
map.set(DiminutiveMedicationPresentation.ML, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.GOTAS, PresentationGroup.GROUP_TWO);
map.set(DiminutiveMedicationPresentation.U, PresentationGroup.GROUP_TWO);
map.set(DiminutiveMedicationPresentation.MG, PresentationGroup.GROUP_TWO);
map.set(DiminutiveMedicationPresentation.MCG, PresentationGroup.GROUP_TWO);
map.set(AugmentativeMedicationPresentation.TUBO, PresentationGroup.GROUP_THREE);
map.set(AugmentativeMedicationPresentation.DISPARO, PresentationGroup.GROUP_THREE);
map.set(AugmentativeMedicationPresentation.INHALADOR, PresentationGroup.GROUP_THREE);
map.set(AugmentativeMedicationPresentation.SISTEMA, PresentationGroup.GROUP_THREE);
map.set(AugmentativeMedicationPresentation.TAMPON, PresentationGroup.GROUP_THREE);
map.set(AugmentativeMedicationPresentation.APLICACION, PresentationGroup.GROUP_THREE);

export const getPresentationGroup = (presentation: string): number => {
    return map.get(presentation);
}

export const mappings: { [key in AugmentativeMedicationPresentation]?: DiminutiveMedicationPresentation[] } = {
    [AugmentativeMedicationPresentation.COMPRIMIDO]: [DiminutiveMedicationPresentation.COMPRIMIDO],
    [AugmentativeMedicationPresentation.CAPSULA]: [DiminutiveMedicationPresentation.CAPSULA],
    [AugmentativeMedicationPresentation.AMPOLLA]: [DiminutiveMedicationPresentation.AMPOLLA],
    [AugmentativeMedicationPresentation.GOMA_DE_MASCAR]: [DiminutiveMedicationPresentation.GOMA_DE_MASCAR],
    [AugmentativeMedicationPresentation.APOSITO]: [DiminutiveMedicationPresentation.APOSITO],
    [AugmentativeMedicationPresentation.OVULO_VAGINAL]: [DiminutiveMedicationPresentation.OVULO_VAGINAL],
    [AugmentativeMedicationPresentation.PASTILLA]: [DiminutiveMedicationPresentation.PASTILLA],
    [AugmentativeMedicationPresentation.SUPOSITORIO]: [DiminutiveMedicationPresentation.SUPOSITORIO],
    [AugmentativeMedicationPresentation.PARCHE]: [DiminutiveMedicationPresentation.PARCHE],
    [AugmentativeMedicationPresentation.IMPLANTE]: [DiminutiveMedicationPresentation.IMPLANTE],
    [AugmentativeMedicationPresentation.APLICADOR]: [DiminutiveMedicationPresentation.APLICADOR],
    [AugmentativeMedicationPresentation.COMPRIMIDO_DE_DISOLUCION_ORAL]: [DiminutiveMedicationPresentation.COMPRIMIDO_DE_DISOLUCION_ORAL],
    [AugmentativeMedicationPresentation.PILDORA_CHICA]: [DiminutiveMedicationPresentation.PILDORA_CHICA],
    [AugmentativeMedicationPresentation.CILINDRO]: [DiminutiveMedicationPresentation.CILINDRO],
    [AugmentativeMedicationPresentation.JERINGA]: [DiminutiveMedicationPresentation.ML],
    [AugmentativeMedicationPresentation.BOLSA]: [DiminutiveMedicationPresentation.ML],
    [AugmentativeMedicationPresentation.BOLSA_CHICA]: [DiminutiveMedicationPresentation.ML],
    [AugmentativeMedicationPresentation.PIPETA]: [DiminutiveMedicationPresentation.ML],
    [AugmentativeMedicationPresentation.SACHET]: [DiminutiveMedicationPresentation.ML],
    [AugmentativeMedicationPresentation.VASO]: [DiminutiveMedicationPresentation.ML],
    [AugmentativeMedicationPresentation.ENVASE]: [DiminutiveMedicationPresentation.ML],
    [AugmentativeMedicationPresentation.FRASCO]: [DiminutiveMedicationPresentation.ML, DiminutiveMedicationPresentation.GOTAS],
    [AugmentativeMedicationPresentation.CARTUCHO]: [DiminutiveMedicationPresentation.U, DiminutiveMedicationPresentation.ML],
    [AugmentativeMedicationPresentation.VIAL]: [DiminutiveMedicationPresentation.ML, DiminutiveMedicationPresentation.MG],
    [AugmentativeMedicationPresentation.LAPICERA]: [DiminutiveMedicationPresentation.U],
    [AugmentativeMedicationPresentation.LIOFILIZADO]: [DiminutiveMedicationPresentation.MG, DiminutiveMedicationPresentation.MCG],
    [AugmentativeMedicationPresentation.TUBO]: [DiminutiveMedicationPresentation.APLICACION],
    [AugmentativeMedicationPresentation.DISPARO]: [DiminutiveMedicationPresentation.APLICACION],
    [AugmentativeMedicationPresentation.INHALADOR]: [DiminutiveMedicationPresentation.APLICACION],
    [AugmentativeMedicationPresentation.SISTEMA]: [DiminutiveMedicationPresentation.APLICACION],
    [AugmentativeMedicationPresentation.TAMPON]: [DiminutiveMedicationPresentation.APLICACION],
};