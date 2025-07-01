import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
  IconButton,
  TextField,
  MenuItem,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Edit as EditIcon,
  Delete as DeleteIcon,
  Add as AddIcon,
  Visibility as VisibilityIcon,
} from '@mui/icons-material';
import { restaurantAPI } from '../services/api';

function RestaurantManagement() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');

  const categories = ['전체', '한식', '일식', '중식', '양식', '카페', '술집'];

  useEffect(() => {
    loadRestaurants();
  }, []);

  const loadRestaurants = async () => {
    try {
      setLoading(true);
      const response = await restaurantAPI.getRestaurants({
        name: searchTerm,
        category: categoryFilter === '전체' ? '' : categoryFilter,
      });
      setRestaurants(response.data.restaurants || response.data);
      setError(null);
    } catch (err) {
      setError('맛집 데이터를 불러오는데 실패했습니다.');
      console.error('Error loading restaurants:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      loadRestaurants();
    }, 500);

    return () => clearTimeout(timeoutId);
  }, [searchTerm, categoryFilter]);

  const getStatusChip = (isActive) => {
    return isActive ? (
      <Chip label="활성" color="success" size="small" />
    ) : (
      <Chip label="비활성" color="default" size="small" />
    );
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" component="h1">
          맛집 관리
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => console.log('맛집 추가')}
        >
          맛집 추가
        </Button>
      </Box>

      <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
        <TextField
          label="맛집명 검색"
          variant="outlined"
          size="small"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          sx={{ minWidth: 200 }}
        />
        <TextField
          select
          label="카테고리"
          variant="outlined"
          size="small"
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
          sx={{ minWidth: 120 }}
        >
          {categories.map((category) => (
            <MenuItem key={category} value={category === '전체' ? '' : category}>
              {category}
            </MenuItem>
          ))}
        </TextField>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>맛집명</TableCell>
              <TableCell>카테고리</TableCell>
              <TableCell>주소</TableCell>
              <TableCell>평점</TableCell>
              <TableCell>리뷰수</TableCell>
              <TableCell>상태</TableCell>
              <TableCell>액션</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {restaurants.map((restaurant) => (
              <TableRow key={restaurant.restaurantId}>
                <TableCell>{restaurant.restaurantId}</TableCell>
                <TableCell>{restaurant.name}</TableCell>
                <TableCell>
                  <Chip label={restaurant.category} variant="outlined" size="small" />
                </TableCell>
                <TableCell>{restaurant.address}</TableCell>
                <TableCell>⭐ {restaurant.averageRating}</TableCell>
                <TableCell>{restaurant.reviewCount}개</TableCell>
                <TableCell>{getStatusChip(restaurant.isActive)}</TableCell>
                <TableCell>
                  <IconButton size="small" color="primary">
                    <VisibilityIcon />
                  </IconButton>
                  <IconButton size="small" color="primary">
                    <EditIcon />
                  </IconButton>
                  <IconButton size="small" color="error">
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}

export default RestaurantManagement;