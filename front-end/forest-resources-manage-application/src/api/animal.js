import { apiClient } from "./index"

export const retrieveAllAnimalSpecies= () => apiClient.get("animal-storage-facilities/species")
export const updateAnimalSpecie= (animalName,animal) => apiClient.put(`animal-storage-facilities/species/${animalName}`,animal)
export const retrieveAnimalQuantityInMoth = (beginMonth,endMonth) => apiClient.get(`animal-storage-facilities/species/facilities-quantity/month/${beginMonth}/${endMonth}`) 
export const retrieveAnimalQuantityInQuarter = (beginQuarter,endQuarter) => apiClient.get(`animal-storage-facilities/species/facilities/quarter/${beginQuarter}/${endQuarter}`) 