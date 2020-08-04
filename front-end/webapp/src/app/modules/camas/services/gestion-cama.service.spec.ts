import { TestBed } from '@angular/core/testing';

import { GestionCamaService } from './gestion-cama.service';

describe('GestionCamaService', () => {
  let service: GestionCamaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GestionCamaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
