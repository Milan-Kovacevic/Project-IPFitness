export interface PageRequest {
    page: number,
    size: number,
    sort?: string,
    locationFilter?: string,
    categoryFilter?: number,
    difficultyFilter?: string,
    stateFilter?: string
    searchQuery?: string,
    additionalOption?: string
}
