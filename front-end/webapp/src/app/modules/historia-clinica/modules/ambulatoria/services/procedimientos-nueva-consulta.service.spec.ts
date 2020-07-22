import { TestBed } from '@angular/core/testing';

import { ProcedimientosNuevaConsultaService } from './procedimientos-nueva-consulta.service';

describe('ProcedimientosNuevaConsultaService', () => {
	let service: ProcedimientosNuevaConsultaService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(ProcedimientosNuevaConsultaService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
