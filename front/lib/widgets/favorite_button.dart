import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/restaurant.dart';
import '../providers/auth_provider.dart';
import '../services/favorite_service.dart';

class FavoriteButton extends StatefulWidget {
  final Restaurant restaurant;
  final bool showText;
  final Color? color;
  final double size;

  const FavoriteButton({
    super.key,
    required this.restaurant,
    this.showText = false,
    this.color,
    this.size = 24,
  });

  @override
  State<FavoriteButton> createState() => _FavoriteButtonState();
}

class _FavoriteButtonState extends State<FavoriteButton> {
  bool _isFavorited = false;
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _checkFavoriteStatus();
  }

  Future<void> _checkFavoriteStatus() async {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    if (!authProvider.isLoggedIn) return;

    try {
      final isFavorited = await FavoriteService.checkFavoriteStatus(
        widget.restaurant.restaurantId,
      );
      if (mounted) {
        setState(() {
          _isFavorited = isFavorited;
        });
      }
    } catch (e) {
      // 에러는 무시하고 기본값 유지
    }
  }

  Future<void> _toggleFavorite() async {
    final authProvider = Provider.of<AuthProvider>(context, listen: false);
    
    if (!authProvider.isLoggedIn) {
      _showLoginDialog();
      return;
    }

    setState(() {
      _isLoading = true;
    });

    try {
      final result = await FavoriteService.toggleFavorite(
        widget.restaurant.restaurantId,
      );

      if (mounted) {
        if (result['success']) {
          setState(() {
            _isFavorited = result['isFavorited'];
          });
          
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(result['message']),
              backgroundColor: _isFavorited ? Colors.orange : Colors.grey,
              duration: const Duration(seconds: 1),
            ),
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(result['message']),
              backgroundColor: Colors.red,
            ),
          );
        }
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(e.toString()),
            backgroundColor: Colors.red,
          ),
        );
      }
    } finally {
      if (mounted) {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  void _showLoginDialog() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('로그인 필요'),
        content: const Text('찜하기를 사용하려면 로그인이 필요합니다.'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('취소'),
          ),
          ElevatedButton(
            onPressed: () {
              Navigator.pop(context);
              Navigator.pushNamed(context, '/login');
            },
            child: const Text('로그인'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final iconData = _isFavorited ? Icons.favorite : Icons.favorite_border;
    final iconColor = _isFavorited 
        ? Colors.red 
        : (widget.color ?? Colors.grey);

    if (widget.showText) {
      return ElevatedButton.icon(
        onPressed: _isLoading ? null : _toggleFavorite,
        icon: _isLoading
            ? SizedBox(
                width: 16,
                height: 16,
                child: CircularProgressIndicator(
                  strokeWidth: 2,
                  valueColor: AlwaysStoppedAnimation<Color>(
                    Theme.of(context).primaryColor,
                  ),
                ),
              )
            : Icon(iconData, size: widget.size),
        label: Text(_isFavorited ? '찜 해제' : '찜하기'),
        style: ElevatedButton.styleFrom(
          backgroundColor: _isFavorited ? Colors.red.shade50 : Colors.grey.shade50,
          foregroundColor: _isFavorited ? Colors.red : Colors.grey.shade700,
          side: BorderSide(
            color: _isFavorited ? Colors.red : Colors.grey.shade300,
          ),
        ),
      );
    }

    return IconButton(
      onPressed: _isLoading ? null : _toggleFavorite,
      icon: _isLoading
          ? SizedBox(
              width: widget.size,
              height: widget.size,
              child: CircularProgressIndicator(
                strokeWidth: 2,
                valueColor: AlwaysStoppedAnimation<Color>(iconColor),
              ),
            )
          : Icon(
              iconData,
              color: iconColor,
              size: widget.size,
            ),
      tooltip: _isFavorited ? '찜 해제' : '찜하기',
    );
  }
}