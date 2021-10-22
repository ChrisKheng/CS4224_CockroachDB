package cs4224.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderByItem {

    @JsonProperty("o_id")
    private long orderId;

    @JsonProperty("o_w_id")
    private long warehouseId;

    @JsonProperty("o_d_id")
    private long districtId;

    @JsonProperty("i_id")
    private long itemId;
}
