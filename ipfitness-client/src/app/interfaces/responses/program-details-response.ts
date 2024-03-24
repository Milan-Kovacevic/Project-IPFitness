import { ProgramAttributeResponse } from './program-attribute-response';

export interface ProgramDetailsResponse {
  programId: number;
  name: string;
  price: number;
  location: string;
  difficulty: string;
  startDate: string;
  endDate: string;
  numberOfParticipants: number;
  numberOfComments: number;
  description: string;
  categoryName?: string;
  attributes: ProgramAttributeResponse[];
  pictures: string[];
  ownerId: number;
  ownerFirstName: string;
  ownerLastName: string;
  ownerBiography?: string;
  ownerContactInfo?: string;
  ownerAvatar?: string;
  isPurchased: boolean;
}
