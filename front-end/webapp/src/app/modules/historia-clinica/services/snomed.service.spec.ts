import { TestBed } from '@angular/core/testing';

import { SnomedService } from './snomed.service';

describe('SnomedService', () => {
  let service: SnomedService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SnomedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
