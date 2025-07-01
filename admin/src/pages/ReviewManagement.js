import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
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
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Avatar,
  Grid,
  Card,
  CardContent,
  Rating,
  ImageList,
  ImageListItem,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Visibility as VisibilityIcon,
  Delete as DeleteIcon,
  Block as BlockIcon,
  CheckCircle as CheckCircleIcon,
  Search as SearchIcon,
  Restaurant as RestaurantIcon,
  Person as PersonIcon,
} from '@mui/icons-material';
import { reviewAPI } from '../services/api';

function ReviewManagement() {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [ratingFilter, setRatingFilter] = useState('');
  const [selectedReview, setSelectedReview] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);

  useEffect(() => {
    loadReviews();
  }, []);

  const loadReviews = async () => {
    try {
      setLoading(true);
      const response = await reviewAPI.getReviews({
        minRating: ratingFilter && ratingFilter !== '전체' ? parseInt(ratingFilter.replace('점', '')) : undefined,
        maxRating: ratingFilter && ratingFilter !== '전체' ? parseInt(ratingFilter.replace('점', '')) : undefined,
        isActive: statusFilter === '활성' ? true : statusFilter === '비활성' ? false : undefined,
      });
      setReviews(response.data.reviews || response.data);
      setError(null);
    } catch (err) {
      setError('리뷰 데이터를 불러오는데 실패했습니다.');
      console.error('Error loading reviews:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      loadReviews();
    }, 500);

    return () => clearTimeout(timeoutId);
  }, [searchTerm, statusFilter, ratingFilter]);

  const statusOptions = ['전체', '활성', '신고됨', '숨김'];
  const ratingOptions = ['전체', '5점', '4점', '3점', '2점', '1점'];

  const getStatusChip = (isActive) => {
    return isActive ? (
      <Chip label="활성" color="success" size="small" />
    ) : (
      <Chip label="비활성" color="error" size="small" />
    );
  };

  const handleViewReview = (review) => {
    setSelectedReview(review);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedReview(null);
  };

  const handleToggleReviewStatus = async (reviewId, currentStatus) => {
    try {
      await reviewAPI.updateReviewStatus(reviewId, { isActive: !currentStatus });
      loadReviews(); // 데이터 새로고침
    } catch (err) {
      console.error('Error updating review status:', err);
    }
  };

  const handleDeleteReview = async (reviewId) => {
    if (window.confirm('정말로 이 리뷰를 삭제하시겠습니까?')) {
      try {
        await reviewAPI.deleteReview(reviewId);
        loadReviews(); // 데이터 새로고침
      } catch (err) {
        console.error('Error deleting review:', err);
      }
    }
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
      
      <Typography variant="h4" component="h1" gutterBottom>
        리뷰 관리
      </Typography>

      {/* 통계 카드 */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="primary">
                전체 리뷰
              </Typography>
              <Typography variant="h4">
                {reviews.length.toLocaleString()}개
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="success.main">
                활성 리뷰
              </Typography>
              <Typography variant="h4">
                {reviews.filter(r => r.status === 'active' && !r.isReported).length}개
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="error.main">
                신고된 리뷰
              </Typography>
              <Typography variant="h4">
                {reviews.filter(r => r.isReported).length}개
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="info.main">
                평균 평점
              </Typography>
              <Typography variant="h4">
                {(reviews.reduce((sum, r) => sum + r.rating, 0) / reviews.length).toFixed(1)}점
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* 검색 및 필터 */}
      <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
        <TextField
          label="리뷰 검색"
          variant="outlined"
          size="small"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          InputProps={{
            endAdornment: <SearchIcon color="action" />
          }}
          sx={{ minWidth: 250 }}
        />
        <TextField
          select
          label="상태"
          variant="outlined"
          size="small"
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
          sx={{ minWidth: 120 }}
        >
          {statusOptions.map((option) => (
            <MenuItem key={option} value={option === '전체' ? '' : option}>
              {option}
            </MenuItem>
          ))}
        </TextField>
        <TextField
          select
          label="평점"
          variant="outlined"
          size="small"
          value={ratingFilter}
          onChange={(e) => setRatingFilter(e.target.value)}
          sx={{ minWidth: 100 }}
        >
          {ratingOptions.map((option) => (
            <MenuItem key={option} value={option === '전체' ? '' : option}>
              {option}
            </MenuItem>
          ))}
        </TextField>
      </Box>

      {/* 리뷰 테이블 */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>작성자</TableCell>
              <TableCell>맛집</TableCell>
              <TableCell>평점</TableCell>
              <TableCell>내용</TableCell>
              <TableCell>상태</TableCell>
              <TableCell>좋아요</TableCell>
              <TableCell>작성일</TableCell>
              <TableCell>액션</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {reviews.map((review) => (
              <TableRow key={review.reviewId}>
                <TableCell>{review.reviewId}</TableCell>
                <TableCell>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Avatar sx={{ width: 24, height: 24 }}>
                      {review.user?.nickname?.charAt(0) || 'U'}
                    </Avatar>
                    {review.user?.nickname || '알 수 없음'}
                  </Box>
                </TableCell>
                <TableCell>{review.restaurant?.name || '알 수 없음'}</TableCell>
                <TableCell>
                  <Rating value={review.rating} size="small" readOnly />
                </TableCell>
                <TableCell>
                  <Typography 
                    variant="body2" 
                    sx={{ 
                      maxWidth: 200, 
                      overflow: 'hidden', 
                      textOverflow: 'ellipsis',
                      whiteSpace: 'nowrap'
                    }}
                  >
                    {review.content}
                  </Typography>
                </TableCell>
                <TableCell>{getStatusChip(review.isActive)}</TableCell>
                <TableCell>{review.likeCount || 0}</TableCell>
                <TableCell>{new Date(review.createdAt).toLocaleDateString()}</TableCell>
                <TableCell>
                  <IconButton size="small" color="primary" onClick={() => handleViewReview(review)}>
                    <VisibilityIcon />
                  </IconButton>
                  {review.isActive ? (
                    <IconButton 
                      size="small" 
                      color="warning"
                      onClick={() => handleToggleReviewStatus(review.reviewId, review.isActive)}
                    >
                      <BlockIcon />
                    </IconButton>
                  ) : (
                    <IconButton 
                      size="small" 
                      color="success"
                      onClick={() => handleToggleReviewStatus(review.reviewId, review.isActive)}
                    >
                      <CheckCircleIcon />
                    </IconButton>
                  )}
                  <IconButton 
                    size="small" 
                    color="error"
                    onClick={() => handleDeleteReview(review.reviewId)}
                  >
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* 리뷰 상세 정보 다이얼로그 */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>리뷰 상세 정보</DialogTitle>
        <DialogContent>
          {selectedReview && (
            <Grid container spacing={3} sx={{ mt: 1 }}>
              <Grid item xs={12} md={6}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
                  <PersonIcon color="action" />
                  <Box>
                    <Typography variant="subtitle2" color="text.secondary">
                      작성자
                    </Typography>
                    <Typography variant="body1">
                      {selectedReview.userNickname} (ID: {selectedReview.userId})
                    </Typography>
                  </Box>
                </Box>

                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
                  <RestaurantIcon color="action" />
                  <Box>
                    <Typography variant="subtitle2" color="text.secondary">
                      맛집
                    </Typography>
                    <Typography variant="body1">
                      {selectedReview.restaurantName} (ID: {selectedReview.restaurantId})
                    </Typography>
                  </Box>
                </Box>

                <Box sx={{ mb: 2 }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    평점
                  </Typography>
                  <Rating value={selectedReview.rating} readOnly />
                </Box>

                <Box sx={{ mb: 2 }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    상태
                  </Typography>
                  {getStatusChip(selectedReview.status, selectedReview.isReported)}
                </Box>
              </Grid>

              <Grid item xs={12} md={6}>
                <Box sx={{ mb: 2 }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    작성일
                  </Typography>
                  <Typography variant="body1">
                    {selectedReview.createdAt}
                  </Typography>
                </Box>

                <Box sx={{ mb: 2 }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    좋아요 수
                  </Typography>
                  <Typography variant="body1">
                    {selectedReview.likeCount}개
                  </Typography>
                </Box>

                {selectedReview.tags && (
                  <Box sx={{ mb: 2 }}>
                    <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                      태그
                    </Typography>
                    <Typography variant="body2" color="primary">
                      {selectedReview.tags}
                    </Typography>
                  </Box>
                )}
              </Grid>

              <Grid item xs={12}>
                <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                  리뷰 내용
                </Typography>
                <Paper sx={{ p: 2, bgcolor: 'grey.50' }}>
                  <Typography variant="body1">
                    {selectedReview.content}
                  </Typography>
                </Paper>
              </Grid>

              {selectedReview.photos && selectedReview.photos.length > 0 && (
                <Grid item xs={12}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    첨부 사진
                  </Typography>
                  <ImageList cols={3} rowHeight={164}>
                    {selectedReview.photos.map((photo, index) => (
                      <ImageListItem key={index}>
                        <Box
                          sx={{
                            width: '100%',
                            height: 164,
                            bgcolor: 'grey.200',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            borderRadius: 1
                          }}
                        >
                          <Typography variant="body2" color="text.secondary">
                            {photo}
                          </Typography>
                        </Box>
                      </ImageListItem>
                    ))}
                  </ImageList>
                </Grid>
              )}
            </Grid>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>
            닫기
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default ReviewManagement;