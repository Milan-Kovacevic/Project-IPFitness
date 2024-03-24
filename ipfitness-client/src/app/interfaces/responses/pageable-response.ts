export interface PageableResponse<T> {
    content: T[],
    totalElements: number,
    totalPages: number,
    size: number,
    number: number,
    numberOfElements: number,
    first: boolean,
    last: boolean
}
