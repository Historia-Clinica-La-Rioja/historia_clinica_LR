import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddInmunizationComponent } from './add-inmunization.component';

describe('AddInmunizationComponent', () => {
  let component: AddInmunizationComponent;
  let fixture: ComponentFixture<AddInmunizationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddInmunizationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddInmunizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
