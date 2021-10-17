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
    private Integer orderId;

    @JsonProperty("o_w_id")
    private Integer warehouseId;

    @JsonProperty("o_d_id")
    private Integer districtId;

    @JsonProperty("i_id")
    private Integer itemId;
}
