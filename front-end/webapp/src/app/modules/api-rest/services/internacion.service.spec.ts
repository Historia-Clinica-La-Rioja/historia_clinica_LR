import { TestBed } from '@angular/core/testing';

import { InternacionService } from './internacion.service';

describe('InternacionService', () => {
  let service: InternacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InternacionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
