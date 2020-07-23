import { TestBed } from '@angular/core/testing';

import { BedService } from './bed.service';

describe('BedService', () => {
  let service: BedService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
