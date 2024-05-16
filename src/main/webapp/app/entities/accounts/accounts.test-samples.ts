import { IAccounts, NewAccounts } from './accounts.model';

export const sampleWithRequiredData: IAccounts = {
  id: 25378,
  username: 'instruction',
  fullName: 'abnormally discover immediately',
  sortableName: 'parched loyal briskly',
  phone: '536-468-8377 x2098',
  gender: 'even',
  userType: 'TEACHER',
  userStatus: 'PENDING',
};

export const sampleWithPartialData: IAccounts = {
  id: 22971,
  username: 'hiccups ugh',
  fullName: 'amongst',
  sortableName: 'ouch',
  avatarImageUrl: 'sanctify',
  phone: '1-893-875-8427 x10121',
  gender: 'deduce',
  userType: 'STUDENT',
  userStatus: 'ACTIVE',
};

export const sampleWithFullData: IAccounts = {
  id: 28750,
  username: 'meanwhile',
  fullName: 'than aboard',
  sortableName: 'misunderstand than',
  avatarImageUrl: 'organisation beech',
  phone: '387-295-0686 x78136',
  locale: 'among supposing as',
  gender: 'excepting vice happy-go-lucky',
  userType: 'TEACHER',
  userStatus: 'PENDING',
};

export const sampleWithNewData: NewAccounts = {
  username: 'kaleidoscopic',
  fullName: 'along colonization',
  sortableName: 'merrily shiny blah',
  phone: '1-241-498-6956 x418',
  gender: 'whether goldfish underneath',
  userType: 'TEACHER',
  userStatus: 'ACTIVE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
