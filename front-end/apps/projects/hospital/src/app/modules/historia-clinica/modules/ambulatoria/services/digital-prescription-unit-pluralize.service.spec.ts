import { TestBed } from '@angular/core/testing';
import { DigitalPrescriptionUnitPluralizeService, PresentationPluralized } from './digital-prescription-unit-pluralize.service';
import { AugmentativeMedicationPresentation } from '../constants/medication-presentation';

describe('DigitalPrescriptionUnitPluralizeService', () => {
    let service: DigitalPrescriptionUnitPluralizeService;

    beforeEach(() => {
        TestBed.configureTestingModule({});
        service = TestBed.inject(DigitalPrescriptionUnitPluralizeService);
    });

    it('should return the correct pluralization for each presentation', () => {
        const testCases: { input: AugmentativeMedicationPresentation, output: string }[] = [
            { input: AugmentativeMedicationPresentation.COMPRIMIDO, output: PresentationPluralized.COMPRIMIDO },
            { input: AugmentativeMedicationPresentation.CAPSULA, output: PresentationPluralized.CAPSULA },
            { input: AugmentativeMedicationPresentation.AMPOLLA, output: PresentationPluralized.AMPOLLA },
			{ input: AugmentativeMedicationPresentation.GOMA_DE_MASCAR, output: PresentationPluralized.GOMA_DE_MASCAR },
            { input: AugmentativeMedicationPresentation.APOSITO, output: PresentationPluralized.APOSITO },
            { input: AugmentativeMedicationPresentation.OVULO_VAGINAL, output: PresentationPluralized.OVULO_VAGINAL },
            { input: AugmentativeMedicationPresentation.PASTILLA, output: PresentationPluralized.PASTILLA },
            { input: AugmentativeMedicationPresentation.SUPOSITORIO, output: PresentationPluralized.SUPOSITORIO },
            { input: AugmentativeMedicationPresentation.PARCHE, output: PresentationPluralized.PARCHE },
            { input: AugmentativeMedicationPresentation.IMPLANTE, output: PresentationPluralized.IMPLANTE },
            { input: AugmentativeMedicationPresentation.APLICADOR, output: PresentationPluralized.APLICADOR },
            { input: AugmentativeMedicationPresentation.COMPRIMIDO_DE_DISOLUCION_ORAL, output: PresentationPluralized.COMPRIMIDO_DE_DISOLUCION_ORAL },
            { input: AugmentativeMedicationPresentation.PILDORA_CHICA, output: PresentationPluralized.PILDORA_CHICA },
            { input: AugmentativeMedicationPresentation.CILINDRO, output: PresentationPluralized.CILINDRO },
            { input: AugmentativeMedicationPresentation.APLICACION, output: PresentationPluralized.APLICACION },
        ];

        testCases.forEach(({ input, output }) => {
            expect(service.run(input)).toEqual(output);
        });
    });

    it('should return the input if the presentation is not found', () => {
        const unknownPresentation = '';
        expect(service.run(unknownPresentation)).toEqual(unknownPresentation);
    });
});