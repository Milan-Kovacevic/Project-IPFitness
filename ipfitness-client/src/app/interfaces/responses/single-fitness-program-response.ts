export interface SingleFitnessProgramResponse {
    programId: number,
    name: string,
    description: string,
    location: string,
    difficulty: string,
    price: number,
    startDate: string,
    endDate: string,
    categoryId: number,
    attributeValues: { attributeId: number, value: string }[],
    images: string[],
    demonstrations: string[]
}
