import { apiClient } from "./index"

export const retrieveAllAdministrations = () => apiClient.get(`administrations`)

export const retrieveSubAdministrationsWithHierarchy = (code) => apiClient.get(`administrations/${code}/sub`)

export const retrieveAdministrationByName = (name) => apiClient.get(`administrations/name/${name}`)

export const updateAdministration  = (code,administration) => apiClient.post(`administrations/${code}`,administration)