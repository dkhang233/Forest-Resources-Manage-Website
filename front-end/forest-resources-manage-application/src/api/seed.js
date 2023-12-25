import { apiClient, baseURL } from "."

export const retrieveAllSeedType = () => apiClient.get('plant-facilities/plant_seed')
export const createSeedType = (seedType) => apiClient.post(`plant-facilities/plant_seed/${seedType.name}`, seedType)
export const updateSeedType = (seedType) => apiClient.put(`plant-facilities/plant_seed/${seedType.name}`, seedType)
export const deleteSeedType = (seedTypeName) => apiClient.delete(`plant-facilities/plant_seed/${seedTypeName}`)

export const retrieveAllFacilities = () => apiClient.get('plant-facilities')
export const createFacility = (facility) => apiClient.post(`plant-facilities/${facility.code}`, facility)
export const updateFacility = (facility) => apiClient.put(`plant-facilities/${facility.code}`, facility)
export const deleteFacility = (facilityCode) => apiClient.delete(`plant-facilities/${facilityCode}`)


export const retrieveAllCoordinates = () => apiClient.get('plant-facilities/coordinates')
export const updateCoordinates = (coordinates) => apiClient.put('plant-facilities/coordinates', coordinates)
export const deleteCoordinates = (code) => apiClient.delete(`plant-facilities/coordinates/${code}`)



export const IMAGE_URL = baseURL + 'plant-facilities/plant_seed/images/'