import { apiClient } from "./index"

export const retrieveAllUsers = () => apiClient.get("users");

export const updateUser = (user) => apiClient.post(`users/${user.username}`,user);
