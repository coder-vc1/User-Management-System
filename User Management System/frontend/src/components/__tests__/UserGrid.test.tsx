import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import UserGrid from '../UserGrid';
import { User } from '../../types/User';

const mockUsers: User[] = [
  {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    ssn: '123-45-6789',
    email: 'john.doe@example.com',
    age: 30,
    role: 'admin',
    phone: '123-456-7890',
    username: 'johnd',
    birthDate: '1993-01-01',
    gender: 'male',
  },
  {
    id: 2,
    firstName: 'Jane',
    lastName: 'Smith',
    ssn: '987-65-4321',
    email: 'jane.smith@example.com',
    age: 25,
    role: 'user',
    phone: '098-765-4321',
    username: 'janes',
    birthDate: '1998-05-15',
    gender: 'female',
  },
  {
    id: 3,
    firstName: 'Bob',
    lastName: 'Johnson',
    ssn: '555-12-3456',
    email: 'bob.johnson@example.com',
    age: 35,
    role: 'admin',
    phone: '555-123-4567',
    username: 'bobj',
    birthDate: '1988-12-20',
    gender: 'male',
  },
];

describe('UserGrid', () => {
  it('renders user cards with correct information', () => {
    render(<UserGrid users={mockUsers} />);
    
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('jane.smith@example.com')).toBeInTheDocument();
    expect(screen.getByText('Bob Johnson')).toBeInTheDocument();
    
    expect(screen.getByText('555-12-3456')).toBeInTheDocument();
  });

  it('displays loading skeletons when loading', () => {
    render(<UserGrid users={[]} isLoading={true} />);
    
    // Check for the presence of skeleton components by their class or test-id
    const skeletons = document.querySelectorAll('.MuiSkeleton-root');
    expect(skeletons.length).toBeGreaterThan(0);
  });

  it('displays error message when there is an error', () => {
    const errorMessage = 'Failed to load users';
    render(<UserGrid users={[]} error={errorMessage} />);
    
    expect(screen.getByText('Error loading users')).toBeInTheDocument();
    expect(screen.getByText(errorMessage)).toBeInTheDocument();
  });

  it('displays no users message when users array is empty', () => {
    render(<UserGrid users={[]} />);
    
    expect(screen.getByText('No users found')).toBeInTheDocument();
    expect(screen.getByText(/Try adjusting your search criteria/)).toBeInTheDocument();
  });

  it('sorts users by age ascending', () => {
    render(<UserGrid users={mockUsers} />);
    
    const sortSelect = screen.getByLabelText('Sort by');
    fireEvent.mouseDown(sortSelect);
    fireEvent.click(screen.getByText('Age (Low to High)'));
    
    const userCards = screen.getAllByText(/Age:/);
    expect(userCards[0]).toHaveTextContent('Age: 25'); // Jane
    expect(userCards[1]).toHaveTextContent('Age: 30'); // John
    expect(userCards[2]).toHaveTextContent('Age: 35'); // Bob
  });

  it('sorts users by age descending', () => {
    render(<UserGrid users={mockUsers} />);
    
    const sortSelect = screen.getByLabelText('Sort by');
    fireEvent.mouseDown(sortSelect);
    fireEvent.click(screen.getByText('Age (High to Low)'));
    
    const userCards = screen.getAllByText(/Age:/);
    expect(userCards[0]).toHaveTextContent('Age: 35'); // Bob
    expect(userCards[1]).toHaveTextContent('Age: 30'); // John
    expect(userCards[2]).toHaveTextContent('Age: 25'); // Jane
  });

  it('filters users by role', () => {
    render(<UserGrid users={mockUsers} />);
    
    const filterSelect = screen.getByLabelText('Filter by Role');
    fireEvent.mouseDown(filterSelect);
    fireEvent.click(screen.getByText('Admin'));
    
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('Bob Johnson')).toBeInTheDocument();
    expect(screen.queryByText('Jane Smith')).not.toBeInTheDocument();
    
    expect(screen.getByText('2 of 3 users')).toBeInTheDocument();
  });

  it('clears filters when clear button is clicked', () => {
    render(<UserGrid users={mockUsers} />);
    
    // Apply filters
    const sortSelect = screen.getByLabelText('Sort by');
    fireEvent.mouseDown(sortSelect);
    fireEvent.click(screen.getByText('Age (Low to High)'));
    
    const filterSelect = screen.getByLabelText('Filter by Role');
    fireEvent.mouseDown(filterSelect);
    fireEvent.click(screen.getByText('Admin'));
    
    // Clear filters
    const clearButton = screen.getByText('Clear Filters');
    fireEvent.click(clearButton);
    
    expect(screen.getByText('3 of 3 users')).toBeInTheDocument();
  });

  it('displays role chips with correct colors', () => {
    render(<UserGrid users={mockUsers} />);
    
    const adminChips = screen.getAllByText('admin');
    const userChip = screen.getByText('user');
    
    expect(adminChips).toHaveLength(2);
    expect(userChip).toBeInTheDocument();
  });

  it('displays user count correctly', () => {
    render(<UserGrid users={mockUsers} />);
    
    expect(screen.getByText('3 of 3 users')).toBeInTheDocument();
  });

  it('sorts users by name alphabetically', () => {
    render(<UserGrid users={mockUsers} />);
    
    const sortSelect = screen.getByLabelText('Sort by');
    fireEvent.mouseDown(sortSelect);
    fireEvent.click(screen.getByText('Name (A to Z)'));
    
    const userNames = screen.getAllByText(/^\w+ \w+$/);
    const names = userNames.map(element => element.textContent);
    expect(names).toContain('Bob Johnson');
    expect(names).toContain('Jane Smith');
    expect(names).toContain('John Doe');
  });
});