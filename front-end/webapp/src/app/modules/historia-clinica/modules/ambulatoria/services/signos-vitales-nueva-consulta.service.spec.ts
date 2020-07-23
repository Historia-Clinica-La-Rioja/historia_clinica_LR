import { TestBed } from '@angular/core/testing';

import { SignosVitalesNuevaConsultaService } from './signos-vitales-nueva-consulta.service';

describe('SignosVitalesNuevaConsultaService', () => {
	let service: SignosVitalesNuevaConsultaService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(SignosVitalesNuevaConsultaService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
