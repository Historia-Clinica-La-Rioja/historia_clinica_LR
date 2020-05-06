import { TestBed } from '@angular/core/testing';

import { SectorService } from './sector.service';

describe('SectorService', () => {
  let service: SectorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SectorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
