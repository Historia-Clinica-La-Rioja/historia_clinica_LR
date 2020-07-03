import { TestBed } from '@angular/core/testing';

import { EvolutionNotesListenerService } from './evolution-notes-listener.service';

describe('EvolutionNotesListenerService', () => {
  let service: EvolutionNotesListenerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvolutionNotesListenerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
