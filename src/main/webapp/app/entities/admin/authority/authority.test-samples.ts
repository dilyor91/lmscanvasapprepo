import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'e74507ba-bd69-460c-b74d-7d6440621262',
};

export const sampleWithPartialData: IAuthority = {
  name: '484c1fc6-6748-41d3-b144-81b601e0b44c',
};

export const sampleWithFullData: IAuthority = {
  name: '61f5fd21-61e9-4b40-a4cd-7b91086d911d',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
