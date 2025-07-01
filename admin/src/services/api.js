import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('adminToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('adminToken');
      localStorage.removeItem('adminInfo');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authAPI = {
  login: (credentials) => api.post('/admin/auth/login', credentials),
  getProfile: () => api.get('/admin/auth/profile'),
};

export const dashboardAPI = {
  getStats: () => api.get('/admin/dashboard/stats'),
  getPopularRestaurants: () => api.get('/admin/dashboard/popular-restaurants'),
  getUserTrends: () => api.get('/admin/dashboard/user-trends'),
};

export const restaurantAPI = {
  getRestaurants: (params) => api.get('/admin/restaurants', { params }),
  getRestaurant: (id) => api.get(`/admin/restaurants/${id}`),
  createRestaurant: (data) => api.post('/admin/restaurants', data),
  updateRestaurant: (id, data) => api.put(`/admin/restaurants/${id}`, data),
  deleteRestaurant: (id) => api.delete(`/admin/restaurants/${id}`),
  updateStatus: (id, status) => api.put(`/admin/restaurants/${id}/status`, status),
  getCategories: () => api.get('/admin/restaurants/categories'),
};

export const userAPI = {
  getUsers: (params) => api.get('/admin/users', { params }),
  getUser: (id) => api.get(`/admin/users/${id}`),
  updateUserStatus: (id, status) => api.put(`/admin/users/${id}/status`, status),
  getUserReviews: (id) => api.get(`/admin/users/${id}/reviews`),
  getUserFavorites: (id) => api.get(`/admin/users/${id}/favorites`),
};

export const reviewAPI = {
  getReviews: (params) => api.get('/admin/reviews', { params }),
  getReview: (id) => api.get(`/admin/reviews/${id}`),
  deleteReview: (id) => api.delete(`/admin/reviews/${id}`),
  updateReviewStatus: (id, status) => api.put(`/admin/reviews/${id}/status`, status),
  getStatistics: () => api.get('/admin/reviews/statistics'),
};

export default api;