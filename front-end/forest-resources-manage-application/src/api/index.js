import axios from "axios";

export const apiClient = axios.create(
    {
        baseURL: 'http://localhost:8088/api/v1',
        timeout : 6000
    }
);

export const apiMap = axios.create(
    {
        baseURL: 'https://api.openstreetmap.org/api/0.6',
        timeout : 0
    }
);