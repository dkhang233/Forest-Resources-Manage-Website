import { apiClient } from "@/api/index";

export const retrieveAllWoodType = () => apiClient.get('wood-facilities/production-type')
export const createProductionType = (productionType) => apiClient.post(`wood-facilities/production-type/${productionType.woodType}`,productionType)
export const updateProductionType = (productionType) => apiClient.put(`wood-facilities/production-type/${productionType.woodType}`,productionType)
export const deleteProductionType = (woodType) => apiClient.delete(`wood-facilities/production-type/${woodType}`)

export const retrieveAllOperationForm = () => apiClient.get('wood-facilities/operation-form')
export const createOperationForm = (operationForm) => apiClient.post(`wood-facilities/operation-form/${operationForm.name}`,operationForm)
export const updateOperationForm = (operationForm) => apiClient.put(`wood-facilities/operation-form/${operationForm.name}`,operationForm)
export const deleteOperationForm = (operationFormName) => apiClient.delete(`wood-facilities/operation-form/${operationFormName}`)