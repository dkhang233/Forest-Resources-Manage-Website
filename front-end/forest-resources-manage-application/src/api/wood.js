import { apiClient } from "@/api/index";

export const retrieveAllWoodType = () => apiClient.get('wood-facilities/production-type')
export const createProductionType = (productionType) => apiClient.post(`wood-facilities/production-type/${productionType.woodType}`, productionType)
export const updateProductionType = (productionType) => apiClient.put(`wood-facilities/production-type/${productionType.woodType}`, productionType)
export const deleteProductionType = (woodType) => apiClient.delete(`wood-facilities/production-type/${woodType}`)

export const retrieveAllOperationForm = () => apiClient.get('wood-facilities/operation-form')
export const createOperationForm = (operationForm) => apiClient.post(`wood-facilities/operation-form/${operationForm.name}`, operationForm)
export const updateOperationForm = (operationForm) => apiClient.put(`wood-facilities/operation-form/${operationForm.name}`, operationForm)
export const deleteOperationForm = (operationFormName) => apiClient.delete(`wood-facilities/operation-form/${operationFormName}`)

export const retrieveAllWoodFacilities = () => apiClient.get('wood-facilities')
export const updateWoodFacility = (facility) => apiClient.put(`wood-facilities/${facility.code}`, facility)
export const createWoodFacility = (facility) => apiClient.post(`wood-facilities/${facility.code}`, facility)
export const deleteWoodFacility = (facilityCode) => apiClient.delete(`wood-facilities/${facilityCode}`)

export const createWoodQuantity = (woodQuantity) => apiClient.put('wood-facilities/production-type/facilities/add', woodQuantity)
export const retrieveWoodQuantityByMonth = (start, end) => apiClient.get(`wood-facilities/production-type/facilities/month/${start}/${end}`)
export const retrieveWoodQuantityByQuarter = (start, end) => apiClient.get(`wood-facilities/production-type/facilities/quarter/${start}/${end}`)
export const retrieveWoodQuantityByYear = (start, end) => apiClient.get(`wood-facilities/production-type/facilities/year/${start}/${end}`)
export const retrieveAllWoodQuantityNow = () => apiClient.get('wood-facilities/production-type/facilities/now')

export const retrieveAllCoordinates = () => apiClient.get('wood-facilities/coordinates')
export const updateCoordinates = (coordinates) => apiClient.put('wood-facilities/coordinates', coordinates)
export const deleteCoordinates = (code) => apiClient.delete(`wood-facilities/coordinates/${code}`)
