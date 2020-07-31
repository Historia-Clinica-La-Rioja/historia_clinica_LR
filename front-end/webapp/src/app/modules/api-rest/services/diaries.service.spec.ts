import { TestBed } from '@angular/core/testing';

import { DiariesService } from './diaries.service';

describe('DiariesService', () => {
  let service: DiariesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiariesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
