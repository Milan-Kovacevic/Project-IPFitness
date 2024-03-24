export interface PurchaseItemResponse {
  purchaseId: number;
  paymentType: string;
  price: number,
  purchaseDate: string;
  programId: number;
  name: string;
  ownerFirstName: string;
  ownerLastName: string;
  difficulty: string;
  location: string;
  programPicture?: string;
}
