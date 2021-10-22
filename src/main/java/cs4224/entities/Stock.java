package cs4224.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @JsonProperty("s_quantity")
    private BigDecimal quantity;

    @JsonProperty("s_ytd")
    private BigDecimal ytdQuantity;

    @JsonProperty("s_order_cnt")
    private Long orderCount;

    @JsonProperty("s_remote_cnt")
    private Long remoteOrderCount;
}
