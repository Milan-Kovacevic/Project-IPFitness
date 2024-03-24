import { NewsItemResponse } from "./news-item-response";

export interface FitnessNewsResponse {
    title: string,
    link: string,
    image: string,
    description: string,
    copyright: string,
    items: NewsItemResponse[]
}
