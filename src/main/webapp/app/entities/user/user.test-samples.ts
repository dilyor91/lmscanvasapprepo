import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 21097,
  login: 'G',
};

export const sampleWithPartialData: IUser = {
  id: 10429,
  login: 'Ze@tHQ\\JRTr\\l6',
};

export const sampleWithFullData: IUser = {
  id: 975,
  login: 'SelFoD@S\\/ii\\7FVH',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
