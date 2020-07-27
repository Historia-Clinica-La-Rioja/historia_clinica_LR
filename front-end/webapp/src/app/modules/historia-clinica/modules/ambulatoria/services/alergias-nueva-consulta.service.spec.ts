import { TestBed } from '@angular/core/testing';

import { AlergiasNuevaConsultaService } from './alergias-nueva-consulta.service';

describe('AlergiasNuevaConsultaService', () => {
  let service: AlergiasNuevaConsultaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlergiasNuevaConsultaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
