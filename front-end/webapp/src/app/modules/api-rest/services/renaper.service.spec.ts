import { TestBed } from '@angular/core/testing';

import { RenaperService } from './renaper.service';

describe('RenaperService', () => {
  let service: RenaperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RenaperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
