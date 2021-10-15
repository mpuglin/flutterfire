package io.flutter.plugins.firebase.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;

import java.util.HashMap;
import java.util.Map;

public class FlutterFirebaseDatabaseException extends Exception {
  private final String code;
  private final String message;
  private final Map<String, Object> additionalData;

  static private final String MODULE = "firebase_database";
  static public final String UNKNOWN_ERROR_CODE = "unknown";
  static public final String UNKNOWN_ERROR_MESSAGE = "An unknown error occurred";

  static FlutterFirebaseDatabaseException fromDatabaseError(DatabaseError e) {
    final int errorCode = e.getCode();

    String code = UNKNOWN_ERROR_CODE;
    String message = UNKNOWN_ERROR_MESSAGE;

    switch (errorCode) {
      case DatabaseError.DATA_STALE:
        code = "data-stale";
        message = "The transaction needs to be run again with current data.";
        break;
      case DatabaseError.OPERATION_FAILED:
        code = "failure";
        message = "The server indicated that this operation failed.";
        break;
      case DatabaseError.PERMISSION_DENIED:
        code = "permission-denied";
        message = "Client doesn't have permission to access the desired data.";
        break;
      case DatabaseError.DISCONNECTED:
        code = "disconnected";
        message = "The operation had to be aborted due to a network disconnect.";
        break;
      case DatabaseError.EXPIRED_TOKEN:
        code = "expired-token";
        message = "The supplied auth token has expired.";
        break;
      case DatabaseError.INVALID_TOKEN:
        code = "invalid-token";
        message = "The supplied auth token was invalid.";
        break;
      case DatabaseError.MAX_RETRIES:
        code = "max-retries";
        message = "The transaction had too many retries.";
        break;
      case DatabaseError.OVERRIDDEN_BY_SET:
        code = "overridden-by-set";
        message = "The transaction was overridden by a subsequent set.";
        break;
      case DatabaseError.UNAVAILABLE:
        code = "unavailable";
        message = "The service is unavailable.";
        break;
      case DatabaseError.NETWORK_ERROR:
        code = "network-error";
        message = "The operation could not be performed due to a network error.";
        break;
      case DatabaseError.WRITE_CANCELED:
        code = "write-cancelled";
        message = "The write was canceled by the user.";
        break;
    }


    final FlutterFirebaseDatabaseException ffdbException;
    final Map<String, Object> details = new HashMap<>();

    if (code.equals(UNKNOWN_ERROR_CODE)) {
      message = e.getMessage();
    }

    ffdbException = new FlutterFirebaseDatabaseException(MODULE, message, details);

    return ffdbException;
  }

  static FlutterFirebaseDatabaseException fromDatabaseException(DatabaseException e) {
    final DatabaseError error = DatabaseError.fromException(e);
    return fromDatabaseError(error);
  }

  static FlutterFirebaseDatabaseException fromException(@Nullable Exception e) {
    if (e == null) return unknown();
    return unknown(e.getMessage());
  }

  static FlutterFirebaseDatabaseException unknown() {
    return unknown(null);
  }

  static FlutterFirebaseDatabaseException unknown(@Nullable String errorMessage) {
    final Map<String, Object> details = new HashMap<>();

    String message = errorMessage;

    if (errorMessage == null) {
      message = UNKNOWN_ERROR_MESSAGE;
    }

    return new FlutterFirebaseDatabaseException(MODULE, message, details);
  }

  public FlutterFirebaseDatabaseException(
    @NonNull String code, @NonNull String message, @Nullable Map<String, Object> additionalData) {
    this.code = code;
    this.message = message;

    if (additionalData != null) {
      this.additionalData = additionalData;
    } else {
      this.additionalData = new HashMap<>();
    }
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public Map<String, Object> getAdditionalData() {
    return additionalData;
  }

}
