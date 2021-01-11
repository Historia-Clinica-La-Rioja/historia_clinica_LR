import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SuspenderMedicacionComponent } from './suspender-medicacion.component';

describe('SuspenderMedicacionComponent', () => {
  let component: SuspenderMedicacionComponent;
  let fixture: ComponentFixture<SuspenderMedicacionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SuspenderMedicacionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SuspenderMedicacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
