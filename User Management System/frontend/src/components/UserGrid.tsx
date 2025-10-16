import React, { useState, useMemo } from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  Chip,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Button,
  IconButton,
  Tooltip,
  Paper,
  Skeleton,
} from "@mui/material";
import {
  Sort as SortIcon,
  SortByAlpha as SortByAlphaIcon,
  FilterList as FilterIcon,
  Clear as ClearIcon,
} from "@mui/icons-material";
import { User } from "../types/User";

interface UserGridProps {
  users: User[];
  isLoading?: boolean;
  error?: string | null;
}

type SortOption = "age-asc" | "age-desc" | "name-asc" | "name-desc" | "none";

const UserGrid: React.FC<UserGridProps> = ({
  users,
  isLoading = false,
  error,
}) => {
  const [sortOption, setSortOption] = useState<SortOption>("none");
  const [roleFilter, setRoleFilter] = useState<string>("all");

  const availableRoles = useMemo(() => {
    const roles = new Set(users.map((user) => user.role));
    return Array.from(roles).sort();
  }, [users]);

  const filteredAndSortedUsers = useMemo(() => {
    let result = [...users];

    if (roleFilter !== "all") {
      result = result.filter((user) => user.role === roleFilter);
    }

    switch (sortOption) {
      case "age-asc":
        result.sort((a, b) => a.age - b.age);
        break;
      case "age-desc":
        result.sort((a, b) => b.age - a.age);
        break;
      case "name-asc":
        result.sort((a, b) =>
          `${a.firstName} ${a.lastName}`.localeCompare(
            `${b.firstName} ${b.lastName}`
          )
        );
        break;
      case "name-desc":
        result.sort((a, b) =>
          `${b.firstName} ${b.lastName}`.localeCompare(
            `${a.firstName} ${a.lastName}`
          )
        );
        break;
      default:
        break;
    }

    return result;
  }, [users, sortOption, roleFilter]);

  const handleClearFilters = () => {
    setSortOption("none");
    setRoleFilter("all");
  };

  const renderLoadingSkeleton = () => (
    <Grid container spacing={3}>
      {Array.from({ length: 6 }, (_, index) => {
        return (
          <Grid size={{ xs: 12, sm: 6, md: 4 }} key={index}>
            <Card elevation={2}>
              <CardContent>
                <Skeleton variant="rectangular" height={120} />
                <Skeleton variant="text" sx={{ mt: 2 }} />
                <Skeleton variant="text" />
                <Skeleton variant="text" />
              </CardContent>
            </Card>
          </Grid>
        );
      })}
    </Grid>
  );

  if (isLoading) {
    return (
      <Box>
        <Paper elevation={1} sx={{ p: 2, mb: 3, backgroundColor: "#f5f5f5" }}>
          <Skeleton variant="rectangular" height={60} />
        </Paper>
        {renderLoadingSkeleton()}
      </Box>
    );
  }

  if (error) {
    return (
      <Paper
        elevation={2}
        sx={{ p: 2, textAlign: "center", backgroundColor: "#ffeaea" }}
      >
        <Typography color="error" variant="h6">
          Error loading users
        </Typography>
        <Typography color="text.secondary" sx={{ mt: 1 }}>
          {error}
        </Typography>
      </Paper>
    );
  }

  if (users.length === 0) {
    return (
      <Paper
        elevation={2}
        sx={{ p: 4, textAlign: "center", backgroundColor: "#f8f9fa" }}
      >
        <Typography variant="h6" color="text.secondary">
          No users found
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
          Try adjusting your search criteria or load data from the external API.
        </Typography>
      </Paper>
    );
  }

  return (
    <Box>
      <Paper elevation={1} sx={{ p: 2, mb: 3, backgroundColor: "#f5f5f5" }}>
        <Box display="flex" gap={2} alignItems="center" flexWrap="wrap">
          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>Sort by</InputLabel>
            <Select
              value={sortOption}
              label="Sort by"
              onChange={(e) => setSortOption(e.target.value as SortOption)}
            >
              <MenuItem value="none">None</MenuItem>
              <MenuItem value="age-asc">Age (Low to High)</MenuItem>
              <MenuItem value="age-desc">Age (High to Low)</MenuItem>
              <MenuItem value="name-asc">Name (A to Z)</MenuItem>
              <MenuItem value="name-desc">Name (Z to A)</MenuItem>
            </Select>
          </FormControl>

          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>Filter by Role</InputLabel>
            <Select
              value={roleFilter}
              label="Filter by Role"
              onChange={(e) => setRoleFilter(e.target.value)}
            >
              <MenuItem value="all">All Roles</MenuItem>
              {availableRoles.map((role) => (
                <MenuItem key={role} value={role}>
                  {role.charAt(0).toUpperCase() + role.slice(1)}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <Button
            variant="outlined"
            size="small"
            startIcon={<ClearIcon />}
            onClick={handleClearFilters}
            disabled={sortOption === "none" && roleFilter === "all"}
          >
            Clear Filters
          </Button>

          <Box sx={{ ml: "auto" }}>
            <Typography variant="body2" color="text.secondary">
              {filteredAndSortedUsers.length} of {users.length} users
            </Typography>
          </Box>
        </Box>
      </Paper>

      <Grid container spacing={3}>
        {filteredAndSortedUsers.map((user) => (
          <Grid size={{ xs: 12, sm: 6, md: 4 }} key={user.id}>
            <Card
              elevation={2}
              sx={{
                height: "100%",
                transition: "transform 0.2s, box-shadow 0.2s",
                "&:hover": {
                  transform: "translateY(-2px)",
                  boxShadow: 4,
                },
              }}
            >
              <CardContent>
                <Box
                  display="flex"
                  justifyContent="space-between"
                  alignItems="flex-start"
                  mb={2}
                >
                  <Typography variant="h6" fontWeight="bold" color="primary">
                    {user.firstName} {user.lastName}
                  </Typography>
                  <Chip
                    label={user.role}
                    color={user.role === "admin" ? "primary" : "default"}
                    size="small"
                  />
                </Box>

                <Box display="flex" flexDirection="column" gap={1}>
                  <Typography variant="body2" color="text.secondary">
                    <strong>ID:</strong> {user.id}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    <strong>Email:</strong> {user.email}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    <strong>Age:</strong> {user.age}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    <strong>SSN:</strong> {user.ssn}
                  </Typography>
                  {user.phone && (
                    <Typography variant="body2" color="text.secondary">
                      <strong>Phone:</strong> {user.phone}
                    </Typography>
                  )}
                  {user.username && (
                    <Typography variant="body2" color="text.secondary">
                      <strong>Username:</strong> {user.username}
                    </Typography>
                  )}
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default UserGrid;
