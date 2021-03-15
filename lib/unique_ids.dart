library unique_ids;

import 'dart:async';

import 'package:flutter/services.dart';

/// This class provide unique identifier for app service.
/// for example,
/// If you need a application id for your service, you can use uuid.
/// And If you need a unique id for device, you can use advertising id.
class UniqueIds {
  static const MethodChannel _channel = const MethodChannel('unique_ids');

  /// get adId from platform.
  static Future<String> get adId async {
    final String _adId = await _channel.invokeMethod('adId');
    return _adId;
  }

  /// create uuid.
  static Future<String> get uuid async {
    final String _uuid = await _channel.invokeMethod('uuid');
    return _uuid;
  }
}
