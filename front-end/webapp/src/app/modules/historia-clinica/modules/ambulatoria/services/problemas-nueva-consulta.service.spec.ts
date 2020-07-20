import { TestBed } from '@angular/core/testing';

import { ProblemasNuevaConsultaService } from './problemas-nueva-consulta.service';

describe('ProblemasNuevaConsultaService', () => {
	let service: ProblemasNuevaConsultaService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(ProblemasNuevaConsultaService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
