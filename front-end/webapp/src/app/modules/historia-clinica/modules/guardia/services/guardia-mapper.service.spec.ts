import { TestBed } from '@angular/core/testing';

import { GuardiaMapperService } from './guardia-mapper.service';

describe('GuardiaMapperService', () => {
  let service: GuardiaMapperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GuardiaMapperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
