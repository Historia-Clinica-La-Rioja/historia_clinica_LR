import { TestBed } from '@angular/core/testing';

import { AntecedentesFamiliaresNuevaConsultaService } from './antecedentes-familiares-nueva-consulta.service';

describe('AntecedentesFamiliaresNuevaConsultaService', () => {
  let service: AntecedentesFamiliaresNuevaConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AntecedentesFamiliaresNuevaConsultaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
