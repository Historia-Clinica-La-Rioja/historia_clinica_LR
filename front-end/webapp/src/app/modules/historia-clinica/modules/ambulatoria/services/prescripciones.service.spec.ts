import { TestBed } from '@angular/core/testing';

import { PrescripcionesService } from './prescripciones.service';

describe('PrescripcionesService', () => {
  let service: PrescripcionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrescripcionesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
