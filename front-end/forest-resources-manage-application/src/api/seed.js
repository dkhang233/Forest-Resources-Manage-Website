import { apiClient, baseURL } from "."

export const retrieveAllSeedType = () => apiClient.get('plant-facilities/plant_seed')
export const createSeedType = (seedType) => apiClient.post(`plant-facilities/plant_seed/${seedType.name}`, seedType)
export const updateSeedType = (seedType) => apiClient.put(`plant-facilities/plant_seed/${seedType.name}`, seedType)

export const retrieveAllFacilities = () => apiClient.get('plant-facilities')
export const createFacility = (facility) => apiClient.post(`plant-facilities/${facility.code}`,facility)
export const updateFacility = (facility) => apiClient.put(`plant-facilities/${facility.code}`,facility)

export const IMAGE_URL = baseURL + 'plant-facilities/plant_seed/images/'