import { apiClient } from "./index"

export const retrieveAllAdministratives = () => apiClient.get(`administratives`);

export const retrieveSubAdministrativesWithHierarchy = (code) => apiClient.get(`administratives/${code}`);