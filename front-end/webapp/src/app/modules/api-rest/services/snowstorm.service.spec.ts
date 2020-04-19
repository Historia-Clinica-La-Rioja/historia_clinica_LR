import { TestBed } from '@angular/core/testing';

import { SnowstormService } from './snowstorm.service';

describe('SnowstormService', () => {
  let service: SnowstormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SnowstormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
