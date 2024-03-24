export interface ProgramAddRequest {
    userId: number,
    name: string,
    description?: string,
    location: string,
    difficulty: string,
    price: number,
    startDate: Date,
    endDate: Date,
    categoryId: number,
    attributeValues: { attributeId: number, value: string }[],
    pictures: File[],
    demonstrations: string[]
}
