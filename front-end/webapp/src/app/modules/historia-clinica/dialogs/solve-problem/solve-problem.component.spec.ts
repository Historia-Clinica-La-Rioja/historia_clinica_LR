import {async, TestBed} from '@angular/core/testing';

import {SolveProblemComponent} from "./solve-problem.component";

describe('SolveProblemComponent', () => {
  let component: SolveProblemComponent;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SolveProblemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {

  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
