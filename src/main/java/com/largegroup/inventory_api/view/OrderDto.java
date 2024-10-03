package com.largegroup.inventory_api.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OrderDto {

    @JsonProperty("invoice_number")
    Integer invoiceId;

    @JsonProperty("user_id")
    Integer userId;

    @JsonProperty("product_id")
    Integer productId;

    Integer quantity;

    Double  total;
}
