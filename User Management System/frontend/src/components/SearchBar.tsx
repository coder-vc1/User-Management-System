import React, { useState, useCallback, useRef, useEffect } from 'react';
import {
  Box,
  TextField,
  InputAdornment,
  IconButton,
  Paper,
  Typography,
} from '@mui/material';
import { Search as SearchIcon } from '@mui/icons-material';

interface SearchBarProps {
  onSearch: (searchTerm: string) => void;
  isLoading?: boolean;
  placeholder?: string;
}

const SearchBar: React.FC<SearchBarProps> = React.memo(({
  onSearch,
  isLoading = false,
  placeholder = "Search users by first name, last name, or SSN..."
}) => {
  const [searchTerm, setSearchTerm] = useState('');
  const inputRef = useRef<HTMLInputElement>(null);
  const [shouldMaintainFocus, setShouldMaintainFocus] = useState(false);


  // Maintain focus after API calls complete
  useEffect(() => {
    if (shouldMaintainFocus && inputRef.current && !isLoading) {
      setTimeout(() => {
        inputRef.current?.focus();
        setShouldMaintainFocus(false);
      }, 50);
    }
  }, [isLoading, shouldMaintainFocus]);

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value;
    setSearchTerm(value);
  };

  const handleKeyPress = (event: React.KeyboardEvent) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      setShouldMaintainFocus(true);
      if (searchTerm.length >= 3 || searchTerm.length === 0) {
        onSearch(searchTerm);
      }
    }
  };

  const handleSearchClick = () => {
    setShouldMaintainFocus(true);
    if (searchTerm.length >= 3 || searchTerm.length === 0) {
      onSearch(searchTerm);
    }
  };

  return (
    <Paper elevation={2} sx={{ p: 3, mb: 4, backgroundColor: '#f8f9fa' }}>
      <Box display="flex" flexDirection="column" alignItems="center" gap={2}>
        <Typography
          variant="h4"
          component="h1"
          fontWeight="bold"
          color="primary"
          textAlign="center"
        >
          User Search
        </Typography>
        
        <Box sx={{ width: '100%', maxWidth: 600 }}>
          <TextField
            fullWidth
            variant="outlined"
            value={searchTerm}
            onChange={handleInputChange}
            onKeyPress={handleKeyPress}
            placeholder={placeholder}
            disabled={isLoading}
            inputRef={inputRef}
            autoFocus
            InputProps={{
              endAdornment: (
                <InputAdornment position="end">
                  <IconButton
                    onClick={handleSearchClick}
                    disabled={isLoading || (searchTerm.length > 0 && searchTerm.length < 3)}
                    color="primary"
                  >
                    <SearchIcon />
                  </IconButton>
                </InputAdornment>
              ),
            }}
            sx={{
              '& .MuiOutlinedInput-root': {
                backgroundColor: 'white',
                borderRadius: 25,
                '&:hover fieldset': {
                  borderColor: 'primary.main',
                },
              },
            }}
          />
          
          {searchTerm.length > 0 && searchTerm.length < 3 && (
            <Typography
              variant="caption"
              color="text.secondary"
              sx={{ mt: 1, display: 'block', textAlign: 'center' }}
            >
              Enter at least 3 characters to search
            </Typography>
          )}
        </Box>
      </Box>
    </Paper>
  );
});

export default SearchBar;