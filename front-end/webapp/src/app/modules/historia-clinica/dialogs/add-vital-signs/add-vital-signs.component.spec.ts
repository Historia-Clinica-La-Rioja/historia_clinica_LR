import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddVitalSignsComponent } from './add-vital-signs.component';

describe('AddVitalSignsComponent', () => {
  let component: AddVitalSignsComponent;
  let fixture: ComponentFixture<AddVitalSignsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddVitalSignsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddVitalSignsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
