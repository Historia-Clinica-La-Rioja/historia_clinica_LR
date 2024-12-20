import { AugmentativeMedicationPresentation, DiminutiveMedicationPresentation } from "../constants/medication-presentation";
import { MedicationPresentationPipe } from "./medication-presentation.pipe";

describe('MedicationPresentationPipe', () => {
    let pipe: MedicationPresentationPipe;
  
    beforeEach(() => {
    	pipe = new MedicationPresentationPipe();
    });
  
    it('to be [comp.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.COMPRIMIDO);
		expect(result).toEqual([DiminutiveMedicationPresentation.COMPRIMIDO]);
    });

	it('to be [cáps.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.CAPSULA);
		expect(result).toEqual([DiminutiveMedicationPresentation.CAPSULA]);
    });

	it('to be [amp.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.AMPOLLA);
		expect(result).toEqual([DiminutiveMedicationPresentation.AMPOLLA]);
    });

	it('to be [gom.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.GOMA_DE_MASCAR);
		expect(result).toEqual([DiminutiveMedicationPresentation.GOMA_DE_MASCAR]);
    });

	it('to be [após]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.APOSITO);
		expect(result).toEqual([DiminutiveMedicationPresentation.APOSITO]);
    });

	it('to be [óv. vag.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.OVULO_VAGINAL);
		expect(result).toEqual([DiminutiveMedicationPresentation.OVULO_VAGINAL]);
    });

	it('to be [past.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.PASTILLA);
		expect(result).toEqual([DiminutiveMedicationPresentation.PASTILLA]);
    });

	it('to be [sup.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.SUPOSITORIO);
		expect(result).toEqual([DiminutiveMedicationPresentation.SUPOSITORIO]);
    });

	it('to be [parch.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.PARCHE);
		expect(result).toEqual([DiminutiveMedicationPresentation.PARCHE]);
    });

	it('to be [impl.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.IMPLANTE);
		expect(result).toEqual([DiminutiveMedicationPresentation.IMPLANTE]);
    });

	it('to be [aplic.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.APLICADOR);
		expect(result).toEqual([DiminutiveMedicationPresentation.APLICADOR]);
    });
	
	it('to be [comp.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.COMPRIMIDO_DE_DISOLUCION_ORAL);
		expect(result).toEqual([DiminutiveMedicationPresentation.COMPRIMIDO_DE_DISOLUCION_ORAL]);
    });
	
	it('to be [píld.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.PILDORA_CHICA);
		expect(result).toEqual([DiminutiveMedicationPresentation.PILDORA_CHICA]);
    });
	
	it('to be [cil.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.CILINDRO);
		expect(result).toEqual([DiminutiveMedicationPresentation.CILINDRO]);
    });
	
	it('to be [ml]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.JERINGA);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML]);
    });
	
	it('to be [ml]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.BOLSA);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML]);
    });

	it('to be [ml]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.BOLSA_CHICA);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML]);
    });

	it('to be [ml]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.PIPETA);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML]);
    });

	it('to be [ml]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.SACHET);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML]);
    });

	it('to be [ml]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.VASO);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML]);
    });

	it('to be [ml]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.ENVASE);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML]);
    });

	it('to be [ml, gts]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.FRASCO);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML, DiminutiveMedicationPresentation.GOTAS]);
    });

	it('to be [U, ml]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.CARTUCHO);
		expect(result).toEqual([DiminutiveMedicationPresentation.U, DiminutiveMedicationPresentation.ML]);
    });

	it('to be [ml, mg]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.VIAL);
		expect(result).toEqual([DiminutiveMedicationPresentation.ML, DiminutiveMedicationPresentation.MG]);
    });

	it('to be [U]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.LAPICERA);
		expect(result).toEqual([DiminutiveMedicationPresentation.U]);
    });

	it('to be [mg, mcg]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.LIOFILIZADO);
		expect(result).toEqual([DiminutiveMedicationPresentation.MG, DiminutiveMedicationPresentation.MCG]);
    });

	it('to be [aplic.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.TUBO);
		expect(result).toEqual([DiminutiveMedicationPresentation.APLICACION]);
    });

	it('to be [aplic.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.DISPARO);
		expect(result).toEqual([DiminutiveMedicationPresentation.APLICACION]);
    });

	it('to be [aplic.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.INHALADOR);
		expect(result).toEqual([DiminutiveMedicationPresentation.APLICACION]);
    });

	it('to be [aplic.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.SISTEMA);
		expect(result).toEqual([DiminutiveMedicationPresentation.APLICACION]);
    });

	it('to be [aplic.]', () => {
		const result = pipe.transform(AugmentativeMedicationPresentation.TAMPON);
		expect(result).toEqual([DiminutiveMedicationPresentation.APLICACION]);
    });
  });