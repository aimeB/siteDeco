jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITypeEvenement, TypeEvenement } from '../type-evenement.model';
import { TypeEvenementService } from '../service/type-evenement.service';

import { TypeEvenementRoutingResolveService } from './type-evenement-routing-resolve.service';

describe('Service Tests', () => {
  describe('TypeEvenement routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TypeEvenementRoutingResolveService;
    let service: TypeEvenementService;
    let resultTypeEvenement: ITypeEvenement | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TypeEvenementRoutingResolveService);
      service = TestBed.inject(TypeEvenementService);
      resultTypeEvenement = undefined;
    });

    describe('resolve', () => {
      it('should return ITypeEvenement returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeEvenement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTypeEvenement).toEqual({ id: 123 });
      });

      it('should return new ITypeEvenement if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeEvenement = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTypeEvenement).toEqual(new TypeEvenement());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TypeEvenement })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTypeEvenement = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTypeEvenement).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
