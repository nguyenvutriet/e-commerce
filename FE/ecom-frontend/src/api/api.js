import axios from "axios";

const api = axios.create({
    baseURL: `${import.meta.env.VITE_BACK_END_URL}/api`,
    withCredentials: true,
});

// Tự động đính kèm JWT token vào mỗi request
api.interceptors.request.use(
    (config) => {
        const auth = localStorage.getItem("auth");
        if (auth) {
            const { jwtToken } = JSON.parse(auth);
            if (jwtToken) {
                config.headers["Authorization"] = `Bearer ${jwtToken}`;
            }
        }
        return config;
    },
    (error) => Promise.reject(error)
);

export default api;