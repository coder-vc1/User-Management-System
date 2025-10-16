import axios from 'axios';
import { userService } from '../userService';
import { User } from '../../types/User';

jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

const mockApiClient = {
  get: jest.fn(),
  post: jest.fn(),
};

mockedAxios.create.mockReturnValue(mockApiClient as any);

describe('userService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const mockUser: User = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    ssn: '123-45-6789',
    email: 'john.doe@example.com',
    age: 30,
    role: 'admin',
  };

  const mockUsers: User[] = [mockUser];

  describe('getAllUsers', () => {
    it('should fetch all users successfully', async () => {
      mockApiClient.get.mockResolvedValue({ data: mockUsers });

      const result = await userService.getAllUsers();

      expect(mockApiClient.get).toHaveBeenCalledWith('/users');
      expect(result).toEqual(mockUsers);
    });

    it('should throw error when request fails', async () => {
      const errorMessage = 'Network Error';
      mockApiClient.get.mockRejectedValue(new Error(errorMessage));

      await expect(userService.getAllUsers()).rejects.toThrow('Failed to fetch users');
      expect(mockApiClient.get).toHaveBeenCalledWith('/users');
    });
  });

  describe('getUserById', () => {
    it('should fetch user by id successfully', async () => {
      mockApiClient.get.mockResolvedValue({ data: mockUser });

      const result = await userService.getUserById(1);

      expect(mockApiClient.get).toHaveBeenCalledWith('/users/1');
      expect(result).toEqual(mockUser);
    });

    it('should throw error when user not found', async () => {
      mockApiClient.get.mockRejectedValue(new Error('User not found'));

      await expect(userService.getUserById(999)).rejects.toThrow('Failed to fetch user');
      expect(mockApiClient.get).toHaveBeenCalledWith('/users/999');
    });
  });

  describe('getUserByEmail', () => {
    it('should fetch user by email successfully', async () => {
      mockApiClient.get.mockResolvedValue({ data: mockUser });

      const result = await userService.getUserByEmail('john.doe@example.com');

      expect(mockApiClient.get).toHaveBeenCalledWith('/users/email/john.doe@example.com');
      expect(result).toEqual(mockUser);
    });
  });

  describe('searchUsers', () => {
    it('should search users with search term', async () => {
      mockApiClient.get.mockResolvedValue({ data: mockUsers });

      const result = await userService.searchUsers('John');

      expect(mockApiClient.get).toHaveBeenCalledWith('/users/search', { params: { q: 'John' } });
      expect(result).toEqual(mockUsers);
    });

    it('should search users without search term', async () => {
      mockApiClient.get.mockResolvedValue({ data: mockUsers });

      const result = await userService.searchUsers('');

      expect(mockApiClient.get).toHaveBeenCalledWith('/users/search', { params: {} });
      expect(result).toEqual(mockUsers);
    });
  });

  describe('loadUsersFromAPI', () => {
    it('should load users from external API successfully', async () => {
      const mockResponse = {
        success: true,
        message: 'Users loaded successfully',
        loadedCount: 100,
      };
      mockApiClient.post.mockResolvedValue({ data: mockResponse });

      const result = await userService.loadUsersFromAPI();

      expect(mockApiClient.post).toHaveBeenCalledWith('/data/load');
      expect(result).toEqual(mockResponse);
    });

    it('should throw error when loading fails', async () => {
      mockApiClient.post.mockRejectedValue(new Error('Loading failed'));

      await expect(userService.loadUsersFromAPI()).rejects.toThrow('Failed to load users from external API');
    });
  });

  describe('getDataStatus', () => {
    it('should get data status successfully', async () => {
      const mockStatus = {
        totalUsers: 100,
        dataLoaded: true,
      };
      mockApiClient.get.mockResolvedValue({ data: mockStatus });

      const result = await userService.getDataStatus();

      expect(mockApiClient.get).toHaveBeenCalledWith('/data/status');
      expect(result).toEqual(mockStatus);
    });

    it('should throw error when getting status fails', async () => {
      mockApiClient.get.mockRejectedValue(new Error('Status check failed'));

      await expect(userService.getDataStatus()).rejects.toThrow('Failed to get data status');
    });
  });
});