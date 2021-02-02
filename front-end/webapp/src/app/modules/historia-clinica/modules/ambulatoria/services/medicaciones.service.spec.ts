import { TestBed } from '@angular/core/testing';

import { MedicacionesService } from './medicaciones.service';

describe('MedicacionesService', () => {
  let service: MedicacionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MedicacionesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
