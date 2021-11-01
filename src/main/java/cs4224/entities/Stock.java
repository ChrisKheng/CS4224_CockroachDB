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
    private long orderCount;

    @JsonProperty("s_remote_cnt")
    private long remoteOrderCount;

    @JsonProperty("s_dist_01")
    private String sDist01;

    @JsonProperty("s_dist_02")
    private String sDist02;

    @JsonProperty("s_dist_03")
    private String sDist03;

    @JsonProperty("s_dist_04")
    private String sDist04;

    @JsonProperty("s_dist_05")
    private String sDist05;

    @JsonProperty("s_dist_06")
    private String sDist06;

    @JsonProperty("s_dist_07")
    private String sDist07;

    @JsonProperty("s_dist_08")
    private String sDist08;

    @JsonProperty("s_dist_09")
    private String sDist09;

    @JsonProperty("s_dist_10")
    private String sDist10;
}
