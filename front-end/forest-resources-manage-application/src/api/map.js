import { apiMap } from ".";

export const retrieveAllDataOfRelation = (relationId) => apiMap.get(`relation/${relationId}.json`)
export const retrieveAllDataOfWay = (wayId) => apiMap.get(`way/${wayId}/full.json`)