import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAccounts, NewAccounts } from '../accounts.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccounts for edit and NewAccountsFormGroupInput for create.
 */
type AccountsFormGroupInput = IAccounts | PartialWithRequiredKeyOf<NewAccounts>;

type AccountsFormDefaults = Pick<NewAccounts, 'id'>;

type AccountsFormGroupContent = {
  id: FormControl<IAccounts['id'] | NewAccounts['id']>;
  username: FormControl<IAccounts['username']>;
  fullName: FormControl<IAccounts['fullName']>;
  sortableName: FormControl<IAccounts['sortableName']>;
  avatarImageUrl: FormControl<IAccounts['avatarImageUrl']>;
  phone: FormControl<IAccounts['phone']>;
  locale: FormControl<IAccounts['locale']>;
  gender: FormControl<IAccounts['gender']>;
  userType: FormControl<IAccounts['userType']>;
  userStatus: FormControl<IAccounts['userStatus']>;
};

export type AccountsFormGroup = FormGroup<AccountsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountsFormService {
  createAccountsFormGroup(accounts: AccountsFormGroupInput = { id: null }): AccountsFormGroup {
    const accountsRawValue = {
      ...this.getFormDefaults(),
      ...accounts,
    };
    return new FormGroup<AccountsFormGroupContent>({
      id: new FormControl(
        { value: accountsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl(accountsRawValue.username, {
        validators: [Validators.required],
      }),
      fullName: new FormControl(accountsRawValue.fullName, {
        validators: [Validators.required],
      }),
      sortableName: new FormControl(accountsRawValue.sortableName, {
        validators: [Validators.required],
      }),
      avatarImageUrl: new FormControl(accountsRawValue.avatarImageUrl),
      phone: new FormControl(accountsRawValue.phone, {
        validators: [Validators.required],
      }),
      locale: new FormControl(accountsRawValue.locale),
      gender: new FormControl(accountsRawValue.gender, {
        validators: [Validators.required],
      }),
      userType: new FormControl(accountsRawValue.userType, {
        validators: [Validators.required],
      }),
      userStatus: new FormControl(accountsRawValue.userStatus, {
        validators: [Validators.required],
      }),
    });
  }

  getAccounts(form: AccountsFormGroup): IAccounts | NewAccounts {
    return form.getRawValue() as IAccounts | NewAccounts;
  }

  resetForm(form: AccountsFormGroup, accounts: AccountsFormGroupInput): void {
    const accountsRawValue = { ...this.getFormDefaults(), ...accounts };
    form.reset(
      {
        ...accountsRawValue,
        id: { value: accountsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountsFormDefaults {
    return {
      id: null,
    };
  }
}
