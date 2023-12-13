import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestAttentionComponent } from './request-attention.component';

describe('RequestAttentionComponent', () => {
  let component: RequestAttentionComponent;
  let fixture: ComponentFixture<RequestAttentionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestAttentionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestAttentionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
