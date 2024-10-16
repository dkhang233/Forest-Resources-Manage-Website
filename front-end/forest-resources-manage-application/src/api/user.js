import { apiClient } from "./index"

export const login = (user) => apiClient.post("users/login", user)
export const resetPassword = (email) => apiClient.post(`users/reset-password`, email)
export const verifyOtp = (otp) => apiClient.post("users/verify-otp", otp)
export const changePassword = (newPassword) => apiClient.put("users/change-password", newPassword)
export const changePasswordWithCurrentPass = (username, newPassword) => apiClient.put(`users/change-password/${username}`, newPassword)
export const retrieveAllUsers = () => apiClient.get("users");

export const retrieveUserByUserName = (username) => apiClient.get(`users/${username}`);

export const updateUserByAdmin = (username, user) => apiClient.post(`users/admin/${username}`, user, {
    headers: {
        'Content-Type': 'multipart/form-data'
    }
});

export const updateUser = (username, user) => apiClient.post(`users/${username}`, user, {
    headers: {
        'Content-Type': 'multipart/form-data'
    }
});

export const createUser = (user) => apiClient.post(`users`, user, {
    headers: {
        'Content-Type': 'multipart/form-data'
    }
});

// export const IMAGE_URL = "http://localhost:8088/api/v1/users/avatar/"
// export const IMAGE_URL = "https://warm-fortress-76305-a18888609ffb.herokuapp.com/api/v1/users/avatar/"
export const IMAGE_URL = "https://forest-agys.onrender.com/api/v1/users/avatar/"
