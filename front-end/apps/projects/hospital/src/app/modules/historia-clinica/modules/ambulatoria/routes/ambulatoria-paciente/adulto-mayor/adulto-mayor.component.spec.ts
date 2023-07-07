import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdultoMayorComponent } from './adulto-mayor.component';

describe('AdultoMayorComponent', () => {
  let component: AdultoMayorComponent;
  let fixture: ComponentFixture<AdultoMayorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdultoMayorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdultoMayorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
