import { TestBed } from '@angular/core/testing';

import { DatosAntropometricosNuevaConsultaService } from './datos-antropometricos-nueva-consulta.service';

describe('DatosAntropometricosNuevaConsultaService', () => {
  let service: DatosAntropometricosNuevaConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DatosAntropometricosNuevaConsultaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
