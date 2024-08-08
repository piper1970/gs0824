package sample.pos.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

/**
 * POJO for capturing inputs necessary to checkout a tool
 */
@Builder
@Value
public class CheckinRequest {
    String toolCode;
    int dayCount;
    int discountPercentage;
    LocalDate checkoutDate;
}
