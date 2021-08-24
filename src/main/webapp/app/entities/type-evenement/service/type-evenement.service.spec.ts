import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeEvenement, TypeEvenement } from '../type-evenement.model';

import { TypeEvenementService } from './type-evenement.service';

describe('Service Tests', () => {
  describe('TypeEvenement Service', () => {
    let service: TypeEvenementService;
    let httpMock: HttpTestingController;
    let elemDefault: ITypeEvenement;
    let expectedResult: ITypeEvenement | ITypeEvenement[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TypeEvenementService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TypeEvenement', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TypeEvenement()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TypeEvenement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TypeEvenement', () => {
        const patchObject = Object.assign({}, new TypeEvenement());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TypeEvenement', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TypeEvenement', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTypeEvenementToCollectionIfMissing', () => {
        it('should add a TypeEvenement to an empty array', () => {
          const typeEvenement: ITypeEvenement = { id: 123 };
          expectedResult = service.addTypeEvenementToCollectionIfMissing([], typeEvenement);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeEvenement);
        });

        it('should not add a TypeEvenement to an array that contains it', () => {
          const typeEvenement: ITypeEvenement = { id: 123 };
          const typeEvenementCollection: ITypeEvenement[] = [
            {
              ...typeEvenement,
            },
            { id: 456 },
          ];
          expectedResult = service.addTypeEvenementToCollectionIfMissing(typeEvenementCollection, typeEvenement);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TypeEvenement to an array that doesn't contain it", () => {
          const typeEvenement: ITypeEvenement = { id: 123 };
          const typeEvenementCollection: ITypeEvenement[] = [{ id: 456 }];
          expectedResult = service.addTypeEvenementToCollectionIfMissing(typeEvenementCollection, typeEvenement);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeEvenement);
        });

        it('should add only unique TypeEvenement to an array', () => {
          const typeEvenementArray: ITypeEvenement[] = [{ id: 123 }, { id: 456 }, { id: 42403 }];
          const typeEvenementCollection: ITypeEvenement[] = [{ id: 123 }];
          expectedResult = service.addTypeEvenementToCollectionIfMissing(typeEvenementCollection, ...typeEvenementArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const typeEvenement: ITypeEvenement = { id: 123 };
          const typeEvenement2: ITypeEvenement = { id: 456 };
          expectedResult = service.addTypeEvenementToCollectionIfMissing([], typeEvenement, typeEvenement2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(typeEvenement);
          expect(expectedResult).toContain(typeEvenement2);
        });

        it('should accept null and undefined values', () => {
          const typeEvenement: ITypeEvenement = { id: 123 };
          expectedResult = service.addTypeEvenementToCollectionIfMissing([], null, typeEvenement, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(typeEvenement);
        });

        it('should return initial array if no TypeEvenement is added', () => {
          const typeEvenementCollection: ITypeEvenement[] = [{ id: 123 }];
          expectedResult = service.addTypeEvenementToCollectionIfMissing(typeEvenementCollection, undefined, null);
          expect(expectedResult).toEqual(typeEvenementCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
