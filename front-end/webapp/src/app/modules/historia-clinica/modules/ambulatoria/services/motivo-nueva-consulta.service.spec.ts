import { TestBed } from '@angular/core/testing';

import { MotivoNuevaConsultaService } from './motivo-nueva-consulta.service';

describe('MotivoNuevaConsultaService', () => {
  let service: MotivoNuevaConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MotivoNuevaConsultaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
