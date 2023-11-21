import { apiClient } from "./index"

export const retrieveAllAdministrations = () => apiClient.get(`administrations`);

export const retrieveSubAdministrationsWithHierarchy = (code) => apiClient.get(`administrations/${code}/sub`);

export const updateAdministration  = (code,administration) => apiClient.post(`administrations/${code}`,administration)