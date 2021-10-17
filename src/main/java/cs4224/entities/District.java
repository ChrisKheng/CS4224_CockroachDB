package cs4224.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {

    @JsonProperty("d_id")
    private Integer id;

    @JsonProperty("d_w_id")
    private Integer warehouseId;

    @JsonProperty("d_name")
    private String name;

    @JsonProperty("d_street_1")
    private String street1;

    @JsonProperty("d_street_2")
    private String street2;

    @JsonProperty("d_city")
    private String city;

    @JsonProperty("d_state")
    private String state;

    @JsonProperty("d_zip")
    private String zip;

    @JsonProperty("d_tax")
    private BigDecimal tax;

    @JsonProperty("d_ytd")
    private BigDecimal amountPaidYTD;

    @JsonProperty("d_next_o_id")
    private Integer nextOrderId;

    public String toAddress() {
        return String.format(" Address (street_1, street_2, city, state, zip) : (%s, %s, %s, %s, %s)",
                street1, street2, city, state, zip);
    }
}
