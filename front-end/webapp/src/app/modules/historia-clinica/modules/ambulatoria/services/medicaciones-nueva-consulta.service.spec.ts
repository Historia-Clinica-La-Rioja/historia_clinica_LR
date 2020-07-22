import { TestBed } from '@angular/core/testing';

import { MedicacionesNuevaConsultaService } from './medicaciones-nueva-consulta.service';

describe('MedicacionesNuevaConsultaService', () => {
	let service: MedicacionesNuevaConsultaService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(MedicacionesNuevaConsultaService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
