import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  Paper,
  List,
  ListItem,
  ListItemText,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Restaurant as RestaurantIcon,
  People as PeopleIcon,
  RateReview as ReviewIcon,
  TrendingUp as TrendingUpIcon,
} from '@mui/icons-material';
import { dashboardAPI } from '../services/api';

const StatCard = ({ title, value, icon, color }) => (
  <Card sx={{ height: '100%' }}>
    <CardContent>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
        <Box sx={{ color: color, mr: 2 }}>
          {icon}
        </Box>
        <Typography variant="h6" component="div">
          {title}
        </Typography>
      </Box>
      <Typography variant="h4" component="div" sx={{ fontWeight: 'bold' }}>
        {value}
      </Typography>
    </CardContent>
  </Card>
);

function Dashboard() {
  const [stats, setStats] = useState(null);
  const [popularRestaurants, setPopularRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        const [statsResponse, popularResponse] = await Promise.all([
          dashboardAPI.getStats(),
          dashboardAPI.getPopularRestaurants()
        ]);
        
        setStats(statsResponse.data);
        setPopularRestaurants(popularResponse.data);
      } catch (error) {
        setError('대시보드 데이터를 불러오는데 실패했습니다.');
        console.error('Dashboard fetch error:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mb: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        대시보드
      </Typography>
      
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="총 맛집 수"
            value={stats?.totalRestaurants || 0}
            icon={<RestaurantIcon />}
            color="primary.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="총 회원 수"
            value={(stats?.totalUsers || 0).toLocaleString()}
            icon={<PeopleIcon />}
            color="secondary.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="총 리뷰 수"
            value={stats?.totalReviews || 0}
            icon={<ReviewIcon />}
            color="success.main"
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="오늘 신규 회원"
            value={stats?.todayNewUsers || 0}
            icon={<TrendingUpIcon />}
            color="warning.main"
          />
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              인기 맛집 TOP 5
            </Typography>
            <List>
              {popularRestaurants.map((restaurant, index) => (
                <ListItem key={index}>
                  <ListItemText
                    primary={`${index + 1}. ${restaurant.name}`}
                    secondary={`${restaurant.category} | ⭐ ${restaurant.averageRating} (리뷰 ${restaurant.reviewCount}개)`}
                  />
                </ListItem>
              ))}
            </List>
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              최근 활동
            </Typography>
            <List>
              <ListItem>
                <ListItemText
                  primary="새로운 맛집 등록"
                  secondary="홈메이드 피자 - 2시간 전"
                />
              </ListItem>
              <ListItem>
                <ListItemText
                  primary="신규 회원 가입"
                  secondary="김맛집러버 - 3시간 전"
                />
              </ListItem>
              <ListItem>
                <ListItemText
                  primary="리뷰 작성"
                  secondary="스테이크 하우스에 새 리뷰 - 4시간 전"
                />
              </ListItem>
              <ListItem>
                <ListItemText
                  primary="맛집 정보 수정"
                  secondary="달콤한 카페 영업시간 변경 - 6시간 전"
                />
              </ListItem>
            </List>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}

export default Dashboard;