import { apiClient } from "./index"

export const retrieveAllAnimalSpecies= () => apiClient.get("animal-storage-facilities/species")
export const updateAnimalSpecie= (animalName,animal) => apiClient.put(`animal-storage-facilities/species/${animalName}`,animal)
