import { EnumItem } from '../models/enum-item';
import { PaymentItem } from '../models/payment-item';
export const BACKEND_BASE_URL = 'http://localhost:9001/api/v1';
export const RECAPTCHA_API_KEY = '6LfreVApAAAAAJ8kk__ZTXj8xwYedam6bg_dtcom';

export const ONLINE_LOCATION = 'ONLINE';
export const programLocations: EnumItem[] = [
  { key: ONLINE_LOCATION, displayName: 'Online' },
  { key: 'GYM', displayName: 'Gym' },
  { key: 'PARK', displayName: 'Park' },
  { key: 'FITNESS_CENTER', displayName: 'Fitness Center' },
  { key: 'YOGA_STUDIO', displayName: 'Yoga Studio' },
  { key: 'CYCLING_TRAILS', displayName: 'Cycling Trails' },
  { key: 'DANCE_STUDIO', displayName: 'Dance Studio' },
];

export const programDifficulties: EnumItem[] = [
  { key: 'BEGINNER', displayName: 'Beginner' },
  { key: 'INTERMEDIATE', displayName: 'Intermediate' },
  { key: 'ADVANCED', displayName: 'Advanced' },
];

export const programStates: EnumItem[] = [
  { key: 'ALL', displayName: 'All' },
  { key: 'ACTIVE', displayName: 'Active' },
  { key: 'UPCOMING', displayName: 'Upcoming' },
  { key: 'FINISHED', displayName: 'Finished' },
];

export const paymentTypes: PaymentItem[] = [
  {
    key: 'PAY_PAL',
    displayName: 'Pay-Pal',
    label: 'Pay-Pal Number',
    placeholder: 'xxxx-xxxx-xxxx-xxxx',
    mask: '9999-9999-9999-9999',
    pattern: '[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}',
  },
  {
    key: 'SHOPPING_CARD',
    displayName: 'Shopping Card',
    label: 'Shopping Card Number',
    placeholder: 'xxxx-xxxx-xxxx-xxxx',
    mask: '9999-9999-9999-9999',
    pattern: '[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}',
  },
  {
    key: 'GIFT_CARD',
    displayName: 'Gift Card',
    label: 'Gift card Number',
    placeholder: 'xxxxx-xxxxx',
    mask: '*****-*****',
    pattern: '[a-zA-Z0-9]{5}-[a-zA-Z0-9]{5}',
  },
  {
    key: 'ON_SITE',
    displayName: 'On Site',
    label: 'Received Access Key',
    mask: 'os-**********',
    placeholder: 'xx-xxxxxxxxxx',
    pattern: '[a-zA-Z0-9]{2}-[a-zA-Z0-9]{10}',
  },
];

export const SHOW_OWN_PROGRAMS_KEY = 'OWN_PROGRAMS';
export const SHOW_PURCHASED_PROGRAMS_KEY = 'PURCHASED_PROGRAMS';

export const additionalOptions: EnumItem[] = [
  { key: SHOW_OWN_PROGRAMS_KEY, displayName: 'Show my Programs' },
  { key: SHOW_PURCHASED_PROGRAMS_KEY, displayName: 'Show purchased Programs' },
];
