import { TestBed } from '@angular/core/testing';

import { DockPopupService } from './dock-popup.service';

describe('OverlayService', () => {
  let service: DockPopupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DockPopupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
