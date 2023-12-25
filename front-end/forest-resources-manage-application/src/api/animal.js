import { apiClient, baseURL } from "./index"

export const retrieveAllAnimalFacilities = () => apiClient.get("animal-storage-facilities")
export const addAnimalFacility = (animalFacility) => apiClient.post(`animal-storage-facilities/${animalFacility.code}`, animalFacility)
export const updateAnimalFacility = (animalFacility) => apiClient.put(`animal-storage-facilities/${animalFacility.code}`, animalFacility)
export const deleteAnimalFacility = (animalFacilityCode) => apiClient.delete(`animal-storage-facilities/${animalFacilityCode}`)

export const retrieveAllAnimalSpecies = () => apiClient.get("animal-storage-facilities/species")
export const createAmimal = (animalName, animal) => apiClient.post(`animal-storage-facilities/species/${animalName}`, animal)
export const updateAnimalSpecie = (animalName, animal) => apiClient.put(`animal-storage-facilities/species/${animalName}`, animal)
export const deleteAnimalSpecie = (animalName) => apiClient.delete(`animal-storage-facilities/species/${animalName}`)

export const retrieveAnimalQuantityInMoth = (beginMonth, endMonth) => apiClient.get(`animal-storage-facilities/species/facilities/month/${beginMonth}/${endMonth}`)
export const retrieveAnimalQuantityInQuarter = (beginQuarter, endQuarter) => apiClient.get(`animal-storage-facilities/species/facilities/quarter/${beginQuarter}/${endQuarter}`)
export const retrieveAnimalQuantityInYear = (beginYear, endYear) => apiClient.get(`animal-storage-facilities/species/facilities/year/${beginYear}/${endYear}`)
export const retrieveAnimalQuantityNow = () => apiClient.get('animal-storage-facilities/species/facility-quantity/now')
export const addAnimalQuantity = (animalQuantity) => apiClient.post('animal-storage-facilities/species/facility-quantity/add', animalQuantity)


export const retrieveAllCoordinates = () => apiClient.get('animal-storage-facilities/coordinates')
export const updateCoordinates = (coordinates) => apiClient.put('animal-storage-facilities/coordinates', coordinates)
export const deleteCoordinates = (code) => apiClient.delete(`animal-storage-facilities/coordinates/${code}`)

// export const IMAGE_URL = 'http://localhost:8088/api/v1/animal-storage-facilities/species/images/'
export const IMAGE_URL = baseURL + 'animal-storage-facilities/species/images/'