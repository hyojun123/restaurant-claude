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
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Visibility as VisibilityIcon,
  Block as BlockIcon,
  CheckCircle as CheckCircleIcon,
  Search as SearchIcon,
} from '@mui/icons-material';
import { userAPI } from '../services/api';

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [selectedUser, setSelectedUser] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const response = await userAPI.getUsers({
        nickname: searchTerm,
        isActive: statusFilter === '활성' ? true : statusFilter === '비활성' ? false : undefined,
      });
      setUsers(response.data.users || response.data);
      setError(null);
    } catch (err) {
      setError('회원 데이터를 불러오는데 실패했습니다.');
      console.error('Error loading users:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      loadUsers();
    }, 500);

    return () => clearTimeout(timeoutId);
  }, [searchTerm, statusFilter]);

  const statusOptions = ['전체', '활성', '정지', '탈퇴'];

  const getStatusChip = (isActive) => {
    return isActive ? (
      <Chip label="활성" color="success" size="small" />
    ) : (
      <Chip label="비활성" color="error" size="small" />
    );
  };

  const getSocialTypeChip = (socialType) => {
    const colors = {
      LOCAL: 'default',
      KAKAO: 'warning',
      NAVER: 'success',
      GOOGLE: 'info'
    };
    return <Chip label={socialType} color={colors[socialType] || 'default'} size="small" variant="outlined" />;
  };

  const handleViewUser = (user) => {
    setSelectedUser(user);
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setSelectedUser(null);
  };

  const handleToggleUserStatus = async (userId, currentStatus) => {
    try {
      await userAPI.updateUserStatus(userId, { isActive: !currentStatus });
      loadUsers(); // 데이터 새로고침
    } catch (err) {
      console.error('Error updating user status:', err);
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
        회원 관리
      </Typography>

      {/* 통계 카드 */}
      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="primary">
                전체 회원
              </Typography>
              <Typography variant="h4">
                {users.length.toLocaleString()}명
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="success.main">
                활성 회원
              </Typography>
              <Typography variant="h4">
                {users.filter(u => u.status === 'active').length}명
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="warning.main">
                정지 회원
              </Typography>
              <Typography variant="h4">
                {users.filter(u => u.status === 'suspended').length}명
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Typography variant="h6" color="info.main">
                오늘 가입
              </Typography>
              <Typography variant="h4">
                5명
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* 검색 및 필터 */}
      <Box sx={{ display: 'flex', gap: 2, mb: 3 }}>
        <TextField
          label="회원 검색"
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
      </Box>

      {/* 회원 테이블 */}
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>ID</TableCell>
              <TableCell>프로필</TableCell>
              <TableCell>이메일</TableCell>
              <TableCell>닉네임</TableCell>
              <TableCell>가입 유형</TableCell>
              <TableCell>상태</TableCell>
              <TableCell>가입일</TableCell>
              <TableCell>최근 로그인</TableCell>
              <TableCell>리뷰</TableCell>
              <TableCell>찜</TableCell>
              <TableCell>액션</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.userId}>
                <TableCell>{user.userId}</TableCell>
                <TableCell>
                  <Avatar sx={{ width: 32, height: 32 }}>
                    {user.nickname.charAt(0)}
                  </Avatar>
                </TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{user.nickname}</TableCell>
                <TableCell>{getSocialTypeChip(user.socialType)}</TableCell>
                <TableCell>{getStatusChip(user.isActive)}</TableCell>
                <TableCell>{new Date(user.createdAt).toLocaleDateString()}</TableCell>
                <TableCell>{user.lastLoginAt ? new Date(user.lastLoginAt).toLocaleDateString() : '없음'}</TableCell>
                <TableCell>{user.reviewCount || 0}개</TableCell>
                <TableCell>{user.favoriteCount || 0}개</TableCell>
                <TableCell>
                  <IconButton size="small" color="primary" onClick={() => handleViewUser(user)}>
                    <VisibilityIcon />
                  </IconButton>
                  {user.isActive ? (
                    <IconButton 
                      size="small" 
                      color="warning"
                      onClick={() => handleToggleUserStatus(user.userId, user.isActive)}
                    >
                      <BlockIcon />
                    </IconButton>
                  ) : (
                    <IconButton 
                      size="small" 
                      color="success"
                      onClick={() => handleToggleUserStatus(user.userId, user.isActive)}
                    >
                      <CheckCircleIcon />
                    </IconButton>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* 사용자 상세 정보 다이얼로그 */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>회원 상세 정보</DialogTitle>
        <DialogContent>
          {selectedUser && (
            <Grid container spacing={2} sx={{ mt: 1 }}>
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle2" color="text.secondary">
                  사용자 ID
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {selectedUser.id}
                </Typography>
                
                <Typography variant="subtitle2" color="text.secondary">
                  이메일
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {selectedUser.email}
                </Typography>
                
                <Typography variant="subtitle2" color="text.secondary">
                  닉네임
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {selectedUser.nickname}
                </Typography>
                
                <Typography variant="subtitle2" color="text.secondary">
                  가입 유형
                </Typography>
                <Box sx={{ mb: 2 }}>
                  {getSocialTypeChip(selectedUser.socialType)}
                </Box>
              </Grid>
              
              <Grid item xs={12} sm={6}>
                <Typography variant="subtitle2" color="text.secondary">
                  계정 상태
                </Typography>
                <Box sx={{ mb: 2 }}>
                  {getStatusChip(selectedUser.status)}
                </Box>
                
                <Typography variant="subtitle2" color="text.secondary">
                  가입일
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {selectedUser.createdAt}
                </Typography>
                
                <Typography variant="subtitle2" color="text.secondary">
                  최근 로그인
                </Typography>
                <Typography variant="body1" sx={{ mb: 2 }}>
                  {selectedUser.lastLogin}
                </Typography>
                
                <Typography variant="subtitle2" color="text.secondary">
                  활동 현황
                </Typography>
                <Typography variant="body1">
                  리뷰 {selectedUser.reviewCount}개, 찜 {selectedUser.favoriteCount}개
                </Typography>
              </Grid>
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

export default UserManagement;