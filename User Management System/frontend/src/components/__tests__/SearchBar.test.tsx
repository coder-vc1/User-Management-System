import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import SearchBar from '../SearchBar';

jest.mock('../../utils/debounce', () => ({
  debounce: (fn: Function) => fn, // Mock debounce to execute immediately for testing
}));

describe('SearchBar', () => {
  const mockOnSearch = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders search input with placeholder', () => {
    render(<SearchBar onSearch={mockOnSearch} />);
    
    expect(screen.getByPlaceholderText(/Search users by first name, last name, or SSN/)).toBeInTheDocument();
    expect(screen.getByText('User Search')).toBeInTheDocument();
  });

  it('calls onSearch when Enter key is pressed with valid search term', () => {
    render(<SearchBar onSearch={mockOnSearch} />);
    
    const input = screen.getByPlaceholderText(/Search users by first name, last name, or SSN/);
    fireEvent.change(input, { target: { value: 'John' } });
    fireEvent.keyPress(input, { key: 'Enter', code: 'Enter', charCode: 13 });
    
    expect(mockOnSearch).toHaveBeenCalledWith('John');
  });

  it('calls onSearch when search icon is clicked', () => {
    render(<SearchBar onSearch={mockOnSearch} />);
    
    const input = screen.getByPlaceholderText(/Search users by first name, last name, or SSN/);
    fireEvent.change(input, { target: { value: 'Jane' } });
    
    const searchButton = screen.getByRole('button');
    fireEvent.click(searchButton);
    
    expect(mockOnSearch).toHaveBeenCalledWith('Jane');
  });

  it('shows minimum character requirement message for short search terms', () => {
    render(<SearchBar onSearch={mockOnSearch} />);
    
    const input = screen.getByPlaceholderText(/Search users by first name, last name, or SSN/);
    fireEvent.change(input, { target: { value: 'Jo' } });
    
    expect(screen.getByText('Enter at least 3 characters to search')).toBeInTheDocument();
  });

  it('does not show minimum character requirement message for valid search terms', () => {
    render(<SearchBar onSearch={mockOnSearch} />);
    
    const input = screen.getByPlaceholderText(/Search users by first name, last name, or SSN/);
    fireEvent.change(input, { target: { value: 'John' } });
    
    expect(screen.queryByText('Enter at least 3 characters to search')).not.toBeInTheDocument();
  });

  it('disables search button for short search terms', () => {
    render(<SearchBar onSearch={mockOnSearch} />);
    
    const input = screen.getByPlaceholderText(/Search users by first name, last name, or SSN/);
    fireEvent.change(input, { target: { value: 'Jo' } });
    
    const searchButton = screen.getByRole('button');
    expect(searchButton).toBeDisabled();
  });

  it('enables search button for valid search terms', () => {
    render(<SearchBar onSearch={mockOnSearch} />);
    
    const input = screen.getByPlaceholderText(/Search users by first name, last name, or SSN/);
    fireEvent.change(input, { target: { value: 'John' } });
    
    const searchButton = screen.getByRole('button');
    expect(searchButton).not.toBeDisabled();
  });

  it('calls onSearch with empty string when input is cleared', () => {
    render(<SearchBar onSearch={mockOnSearch} />);
    
    const input = screen.getByPlaceholderText(/Search users by first name, last name, or SSN/);
    fireEvent.change(input, { target: { value: '' } });
    
    expect(mockOnSearch).toHaveBeenCalledWith('');
  });

  it('disables input when loading', () => {
    render(<SearchBar onSearch={mockOnSearch} isLoading={true} />);
    
    const input = screen.getByPlaceholderText(/Search users by first name, last name, or SSN/);
    expect(input).toBeDisabled();
  });
});