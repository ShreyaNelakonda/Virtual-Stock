package dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Data transfer object to get time stamp bins and type of time span for calculating performance
 * of a portfolio.
 */
public class TimeStampBinsDTO {

  private final List<LocalDate> timeStampBins;
  private final TimestampType type;

  /**
   * Enum class to identify timestamp as year, month or day.
   */
  public enum TimestampType {
    YEAR,MONTH,DAY
  }

  public TimeStampBinsDTO(List<LocalDate> timeStampBins, TimestampType type) {
    this.timeStampBins = timeStampBins;
    this.type = type;
  }

  public List<LocalDate> getTimeStampBins() {
    return timeStampBins;
  }

  public TimestampType getType() {
    return type;
  }

}
