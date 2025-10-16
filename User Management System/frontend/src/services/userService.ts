import axios from 'axios';
import { User } from '../types/User';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8084/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const userService = {
  async getAllUsers(): Promise<User[]> {
    try {
      const response = await apiClient.get<User[]>('/users');
      return response.data;
    } catch (error) {
      console.error('Error fetching all users:', error);
      throw new Error('Failed to fetch users');
    }
  },

  async getUserById(id: number): Promise<User> {
    try {
      const response = await apiClient.get<User>(`/users/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching user ${id}:`, error);
      throw new Error('Failed to fetch user');
    }
  },

  async getUserByEmail(email: string): Promise<User> {
    try {
      const response = await apiClient.get<User>(`/users/email/${email}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching user by email ${email}:`, error);
      throw new Error('Failed to fetch user');
    }
  },

  async searchUsers(searchTerm: string): Promise<User[]> {
    try {
      const params = searchTerm ? { q: searchTerm } : {};
      const response = await apiClient.get<User[]>('/users/search', { params });
      return response.data;
    } catch (error) {
      console.error('Error searching users:', error);
      throw new Error('Failed to search users');
    }
  },

  async loadUsersFromAPI(): Promise<{ success: boolean; message: string; loadedCount: number }> {
    try {
      const response = await apiClient.post('/data/load');
      return response.data;
    } catch (error) {
      console.error('Error loading users from API:', error);
      throw new Error('Failed to load users from external API');
    }
  },

  async getDataStatus(): Promise<{ totalUsers: number; dataLoaded: boolean }> {
    try {
      const response = await apiClient.get('/data/status');
      return response.data;
    } catch (error) {
      console.error('Error getting data status:', error);
      throw new Error('Failed to get data status');
    }
  },
};