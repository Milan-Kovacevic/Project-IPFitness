export interface FitnessProgramResponse {
    programId: number;
    name: string;
    categoryName?: string;
    location: string;
    difficulty: string;
    createdBy: string;
    userId: number,
    price: number;
    images?: string[]
}
