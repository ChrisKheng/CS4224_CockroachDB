package cs4224.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;


@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @JsonProperty("c_id")
    private long id;

    @JsonProperty("c_w_id")
    private long warehouseId;

    @JsonProperty("c_d_id")
    private long districtId;

    @JsonProperty("c_first")
    private String firstName;

    @JsonProperty("c_middle")
    private String middleName;

    @JsonProperty("c_last")
    private String lastName;

    @JsonProperty("c_street_1")
    private String street1;

    @JsonProperty("c_street_2")
    private String street2;

    @JsonProperty("c_city")
    private String city;

    @JsonProperty("c_state")
    private String state;

    @JsonProperty("c_zip")
    private String zip;

    @JsonProperty("c_phone")
    private String phone;

    @JsonProperty("c_since")
    private Instant entryCreateDateTime;

    @JsonProperty("c_credit")
    private String creditStatus;

    @JsonProperty("c_credit_lim")
    private BigDecimal creditLimit;

    @JsonProperty("c_discount")
    private BigDecimal discountRate;

    @JsonProperty("c_balance")
    private BigDecimal balance;

    @JsonProperty("c_ytd_payment")
    private Float paymentYTD;

    @JsonProperty("c_payment_cnt")
    private long numPayments;

    @JsonProperty("c_delivery_cnt")
    private long numDeliveries;

    @JsonProperty("c_data")
    private String miscData;

    public String toSpecifier() {
        return String.format("(%d, %d, %d)", warehouseId, districtId, id);
    }

    public String toName() {
        return String.format(" Name (First Middle Last) : (%s %s %s)", firstName, middleName, lastName);
    }

    public String toAddress() {
        return String.format(" Address (street_1, street_2, city, state, zip) : (%s, %s, %s, %s, %s)", street1, street2, city, state, zip);
    }

    public String toOtherInfo() {
        return String.format(" Phone: %s \n Since: %s \n Credit: %s \n Credit Limit:%f \n Discount: %f \n Balance: %f",
                phone, entryCreateDateTime, creditStatus, creditLimit, discountRate, balance);
    }

}
