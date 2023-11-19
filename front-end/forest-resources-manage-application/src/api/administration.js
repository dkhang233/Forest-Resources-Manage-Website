import { apiClient } from "./index"

export const retrieveAllAdministrations = () => apiClient.get(`administrations`);

export const retrieveSubAdministrationsWithHierarchy = (code) => apiClient.get(`administrations/${code}`);

export const updateAdministration  = (administration) => apiClient.post(`administrations/${administration.code}`,administration)