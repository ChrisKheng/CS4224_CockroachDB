package cs4224.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLine {

    @JsonProperty("ol_i_id")
    private long itemId;

    @JsonProperty("ol_quantity")
    private BigDecimal quantity;

    @JsonProperty("ol_amount")
    private BigDecimal amount;

    @JsonProperty("ol_supply_w_id")
    private long supplyingWarehouseId;

    @JsonProperty("ol_delivery_d")
    private Instant deliveryDateTime;

}
