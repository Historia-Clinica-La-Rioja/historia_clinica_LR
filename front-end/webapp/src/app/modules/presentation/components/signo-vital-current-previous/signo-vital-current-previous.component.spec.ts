import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SignoVitalCurrentPreviousComponent } from './signo-vital-current-previous.component';

describe('SignoVitalCurrentPreviousComponent', () => {
  let component: SignoVitalCurrentPreviousComponent;
  let fixture: ComponentFixture<SignoVitalCurrentPreviousComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SignoVitalCurrentPreviousComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SignoVitalCurrentPreviousComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
