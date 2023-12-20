import { apiClient } from "./index"

export const login = (user) => apiClient.post("users/login",user)
export const resetPassword  = (email) => apiClient.post(`users/reset-password`,email)
export const verifyOtp = (otp) => apiClient.post("users/verify-otp",otp)
export const changePassword = (newPassword) => apiClient.post("users/change-password",newPassword)
export const retrieveAllUsers = () => apiClient.get("users");

export const retrieveUserByUserName = (username) => apiClient.get(`users/${username}`);

export const updateUserByAdmin = (username,user) => apiClient.post(`users/admin/${username}`, user, {
    headers: {
        'Content-Type': 'multipart/form-data'
    }
});

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
