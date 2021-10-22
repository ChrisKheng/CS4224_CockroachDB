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
public class OrderLine {

    @JsonProperty("ol_i_id")
    private Long itemId;

    @JsonProperty("ol_quantity")
    private BigDecimal quantity;

    @JsonProperty("ol_amount")
    private BigDecimal amount;
}
