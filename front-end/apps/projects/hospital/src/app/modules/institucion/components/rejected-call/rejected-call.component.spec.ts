import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectedCallComponent } from './rejected-call.component';

describe('RejectedCallComponent', () => {
  let component: RejectedCallComponent;
  let fixture: ComponentFixture<RejectedCallComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectedCallComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RejectedCallComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
