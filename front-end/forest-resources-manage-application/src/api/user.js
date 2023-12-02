import { apiClient } from "./index"

export const retrieveAllUsers = () => apiClient.get("users");

export const updateUser = (username,user) => apiClient.post(`users/${username}`, user, {
    headers: {
        'Content-Type': 'multipart/form-data'
    }
});

export const createUser = (user) => apiClient.post(`users`, user, {
    headers: {
        'Content-Type': 'multipart/form-data'
    }
});
