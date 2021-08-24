import { TestBed } from '@angular/core/testing';

import { AproposService } from './apropos.service';

describe('AproposService', () => {
  let service: AproposService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AproposService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
