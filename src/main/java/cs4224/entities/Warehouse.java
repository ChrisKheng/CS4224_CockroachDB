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
public class Warehouse {

    @JsonProperty("w_id")
    private Integer id;

    @JsonProperty("w_name")
    private String name;

    @JsonProperty("w_street_1")
    private String street1;

    @JsonProperty("w_street_2")
    private String street2;

    @JsonProperty("w_city")
    private String city;

    @JsonProperty("w_state")
    private String state;

    @JsonProperty("w_zip")
    private String zip;

    @JsonProperty("w_tax")
    private BigDecimal tax;

    @JsonProperty("w_ytd")
    private BigDecimal amountPaidYTD;

    public String toAddress() {
        return String.format(" Address (street_1, street_2, city, state, zip) : (%s, %s, %s, %s, %s)",
                street1, street2, city, state, zip);
    }
}
