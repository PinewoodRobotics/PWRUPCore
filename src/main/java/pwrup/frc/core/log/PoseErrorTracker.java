package pwrup.frc.core.log;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import java.util.ArrayList;
import java.util.List;
import pwrup.frc.core.geometry.TranslationMath;

public class PoseErrorTracker<S extends Pose2d> {

  List<S> tracking_error_over_time_;
  int max_num_samples_;

  public PoseErrorTracker(int max_num_samples) {
    max_num_samples_ = max_num_samples;
    tracking_error_over_time_ = new ArrayList<S>(max_num_samples);
  }

  public void addObservation(S error) {
    if (tracking_error_over_time_.size() > max_num_samples_) {
      tracking_error_over_time_.remove(0);
    }
    tracking_error_over_time_.add(error);
  }

  public void reset() {
    tracking_error_over_time_.clear();
  }

  public Translation2d getMaxTranslationError() {
    if (tracking_error_over_time_.isEmpty()) return new Translation2d();
    double max_norm = Double.NEGATIVE_INFINITY;
    Translation2d max = null;
    for (var error : tracking_error_over_time_) {
      double norm = TranslationMath.getNorm2(error.getTranslation());
      if (norm > max_norm) {
        max_norm = norm;
        max = error.getTranslation();
      }
    }
    return max;
  }

  public Rotation2d getMaxRotationError() {
    if (tracking_error_over_time_.isEmpty()) return new Rotation2d();
    double max_norm = Double.NEGATIVE_INFINITY;
    Rotation2d max = null;
    for (var error : tracking_error_over_time_) {
      double norm = Math.abs(error.getRotation().getRadians());
      if (norm > max_norm) {
        max_norm = norm;
        max = error.getRotation();
      }
    }

    return max;
  }

  public double getTranslationRMSE() {
    double error_sum = 0.0;
    for (var error : tracking_error_over_time_) {
      error_sum += TranslationMath.getNorm2(error.getTranslation());
    }
    error_sum /= (double) tracking_error_over_time_.size();
    return Math.sqrt(error_sum);
  }

  public double getRotationRMSE() {
    double error_sum = 0.0;
    for (var error : tracking_error_over_time_) {
      error_sum +=
        error.getRotation().getRadians() * error.getRotation().getRadians();
    }
    error_sum /= (double) tracking_error_over_time_.size();
    return Math.sqrt(error_sum);
  }

  @Override
  public String toString() {
    if (tracking_error_over_time_.isEmpty()) return "";

    return String.format(
      "Error Summary---%n" +
      "Translation: RMSE %.2f, Max: %.2f%n" +
      "Rotation: RMSE %.2f, Max: %.2f%n",
      getTranslationRMSE(),
      getMaxTranslationError(),
      getRotationRMSE(),
      getMaxRotationError()
    );
  }
}
