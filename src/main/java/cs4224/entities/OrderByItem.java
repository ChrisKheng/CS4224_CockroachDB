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
    private Long orderId;

    @JsonProperty("o_w_id")
    private Long warehouseId;

    @JsonProperty("o_d_id")
    private Long districtId;

    @JsonProperty("i_id")
    private Long itemId;
}
