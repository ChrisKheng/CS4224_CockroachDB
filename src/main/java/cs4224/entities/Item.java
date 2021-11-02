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
public class Item {

    @JsonProperty("i_id")
    private long id;

    @JsonProperty("i_name")
    private String name;

    @JsonProperty("i_price")
    private BigDecimal price;

}
