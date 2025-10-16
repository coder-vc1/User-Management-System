import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Container,
  ThemeProvider,
  createTheme,
  CssBaseline,
  Alert,
  Snackbar,
  Button,
  AppBar,
  Toolbar,
  Typography,
  CircularProgress,
  Backdrop,
} from '@mui/material';
import { Refresh as RefreshIcon } from '@mui/icons-material';
import SearchBar from './components/SearchBar';
import UserGrid from './components/UserGrid';
import { userService } from './services/userService';
import { User } from './types/User';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
      light: '#42a5f5',
      dark: '#1565c0',
    },
    secondary: {
      main: '#dc004e',
    },
    background: {
      default: '#f5f5f5',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 600,
    },
  },
  components: {
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 20,
          textTransform: 'none',
        },
      },
    },
  },
});

const App: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isInitialLoading, setIsInitialLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [dataStatus, setDataStatus] = useState<{ totalUsers: number; dataLoaded: boolean } | null>(null);
  const [snackbar, setSnackbar] = useState<{ open: boolean; message: string; severity: 'success' | 'error' | 'info' }>({
    open: false,
    message: '',
    severity: 'info',
  });


  const loadUsers = useCallback(async (searchTerm: string = '') => {
    setIsLoading(true);
    setError(null);
    
    try {
      const fetchedUsers = searchTerm
        ? await userService.searchUsers(searchTerm)
        : await userService.getAllUsers();
      
      setUsers(fetchedUsers);
      
      if (searchTerm) {
        setSnackbar({ 
          open: true, 
          message: `Found ${fetchedUsers.length} user(s) matching "${searchTerm}"`, 
          severity: 'success' 
        });
      }
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'An error occurred';
      setError(errorMessage);
      setSnackbar({ 
        open: true, 
        message: errorMessage, 
        severity: 'error' 
      });
    } finally {
      setIsLoading(false);
    }
  }, []);

  const loadDataStatus = useCallback(async () => {
    try {
      const status = await userService.getDataStatus();
      setDataStatus(status);
      
      if (!status.dataLoaded) {
        setSnackbar({ 
          open: true, 
          message: 'No data loaded. Click "Load Data" to fetch users from external API.', 
          severity: 'info' 
        });
      }
    } catch (err) {
      console.error('Error loading data status:', err);
    }
  }, []);

  const loadDataFromAPI = useCallback(async () => {
    setIsLoading(true);
    try {
      const result = await userService.loadUsersFromAPI();
      setSnackbar({ 
        open: true, 
        message: `Successfully loaded ${result.loadedCount} users`, 
        severity: 'success' 
      });
      await loadDataStatus();
      await loadUsers();
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Failed to load data';
      setSnackbar({ 
        open: true, 
        message: errorMessage, 
        severity: 'error' 
      });
    } finally {
      setIsLoading(false);
    }
  }, [loadDataStatus, loadUsers]);

  useEffect(() => {
    const initializeApp = async () => {
      setIsInitialLoading(true);
      try {
        await loadDataStatus();
        await loadUsers();
      } catch (err) {
        console.error('Error initializing app:', err);
        setError('Failed to initialize application');
      } finally {
        setIsInitialLoading(false);
      }
    };

    initializeApp();
  }, [loadDataStatus, loadUsers]);

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  if (isInitialLoading) {
    return (
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Backdrop open={true} sx={{ zIndex: 9999, backgroundColor: 'rgba(0,0,0,0.8)' }}>
          <Box display="flex" flexDirection="column" alignItems="center" gap={2}>
            <CircularProgress size={60} color="primary" />
            <Typography variant="h6" color="white">
              Loading User Management System...
            </Typography>
          </Box>
        </Backdrop>
      </ThemeProvider>
    );
  }

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
        <AppBar position="static" elevation={2}>
          <Toolbar>
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              User Management System
            </Typography>
            
            {dataStatus && (
              <Typography variant="body2" sx={{ mr: 2 }}>
                Total Users: {dataStatus.totalUsers}
              </Typography>
            )}
            
            <Button
              color="inherit"
              startIcon={<RefreshIcon />}
              onClick={() => loadUsers()}
              disabled={isLoading}
              sx={{ mr: 1 }}
            >
              Refresh
            </Button>
            
            {dataStatus && !dataStatus.dataLoaded && (
              <Button
                color="inherit"
                variant="outlined"
                onClick={loadDataFromAPI}
                disabled={isLoading}
              >
                Load Data
              </Button>
            )}
          </Toolbar>
        </AppBar>

        <Container maxWidth="xl" sx={{ py: 4 }}>
          <SearchBar
            onSearch={loadUsers}
            isLoading={isLoading}
          />

          {dataStatus && !dataStatus.dataLoaded && users.length === 0 && !error && (
            <Alert severity="info" sx={{ mb: 3 }}>
              <Typography variant="body1">
                Welcome to the User Management System!
              </Typography>
              <Typography variant="body2" sx={{ mt: 1 }}>
                No user data is currently loaded. Click the "Load Data" button in the top navigation to fetch users from the external DummyJSON API.
              </Typography>
            </Alert>
          )}

          <UserGrid
            users={users}
            isLoading={isLoading}
            error={error}
          />
        </Container>

        <Snackbar
          open={snackbar.open}
          autoHideDuration={6000}
          onClose={handleCloseSnackbar}
          anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        >
          <Alert 
            onClose={handleCloseSnackbar} 
            severity={snackbar.severity}
            sx={{ width: '100%' }}
            variant="outlined"
          >
            {snackbar.message}
          </Alert>
        </Snackbar>
      </Box>
    </ThemeProvider>
  );
};

export default App;
