import { TestBed } from '@angular/core/testing';

import { EpicrisisService } from './epicrisis.service';

describe('EpicrisisService', () => {
  let service: EpicrisisService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EpicrisisService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
