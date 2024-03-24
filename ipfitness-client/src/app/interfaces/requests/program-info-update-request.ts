export interface ProgramInfoUpdateRequest {
  userId: number;
  name: string;
  description: string;
  location: string;
  difficulty: string;
  price: number;
  startDate: Date;
  endDate: Date;
  categoryId: number;
  values: { attributeId: number; value: string }[];
}
