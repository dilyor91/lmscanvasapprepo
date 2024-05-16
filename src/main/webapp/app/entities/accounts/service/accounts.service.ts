import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccounts, NewAccounts } from '../accounts.model';

export type PartialUpdateAccounts = Partial<IAccounts> & Pick<IAccounts, 'id'>;

export type EntityResponseType = HttpResponse<IAccounts>;
export type EntityArrayResponseType = HttpResponse<IAccounts[]>;

@Injectable({ providedIn: 'root' })
export class AccountsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/accounts');

  create(accounts: NewAccounts): Observable<EntityResponseType> {
    return this.http.post<IAccounts>(this.resourceUrl, accounts, { observe: 'response' });
  }

  update(accounts: IAccounts): Observable<EntityResponseType> {
    return this.http.put<IAccounts>(`${this.resourceUrl}/${this.getAccountsIdentifier(accounts)}`, accounts, { observe: 'response' });
  }

  partialUpdate(accounts: PartialUpdateAccounts): Observable<EntityResponseType> {
    return this.http.patch<IAccounts>(`${this.resourceUrl}/${this.getAccountsIdentifier(accounts)}`, accounts, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAccounts>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccounts[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccountsIdentifier(accounts: Pick<IAccounts, 'id'>): number {
    return accounts.id;
  }

  compareAccounts(o1: Pick<IAccounts, 'id'> | null, o2: Pick<IAccounts, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountsIdentifier(o1) === this.getAccountsIdentifier(o2) : o1 === o2;
  }

  addAccountsToCollectionIfMissing<Type extends Pick<IAccounts, 'id'>>(
    accountsCollection: Type[],
    ...accountsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accounts: Type[] = accountsToCheck.filter(isPresent);
    if (accounts.length > 0) {
      const accountsCollectionIdentifiers = accountsCollection.map(accountsItem => this.getAccountsIdentifier(accountsItem));
      const accountsToAdd = accounts.filter(accountsItem => {
        const accountsIdentifier = this.getAccountsIdentifier(accountsItem);
        if (accountsCollectionIdentifiers.includes(accountsIdentifier)) {
          return false;
        }
        accountsCollectionIdentifiers.push(accountsIdentifier);
        return true;
      });
      return [...accountsToAdd, ...accountsCollection];
    }
    return accountsCollection;
  }
}
