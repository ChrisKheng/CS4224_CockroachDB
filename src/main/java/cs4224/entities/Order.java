package cs4224.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @JsonProperty("o_id")
    private Integer id;

    @JsonProperty("o_w_id")
    private Integer warehouseId;

    @JsonProperty("o_d_id")
    private Integer districtId;

    @JsonProperty("o_c_id")
    private Integer customerId;

    @JsonProperty("o_carrier_id")
    private Integer carrierId;

    @JsonProperty("o_ol_cnt")
    private BigDecimal numItems;

    @JsonProperty("o_all_local")
    private BigDecimal allLocal;

    @JsonProperty("o_entry_d")
    private Instant entryDateTime;
    
}
