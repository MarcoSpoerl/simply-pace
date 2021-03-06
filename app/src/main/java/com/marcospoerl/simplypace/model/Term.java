package com.marcospoerl.simplypace.model;

import java.math.BigDecimal;

public interface Term {

    int TYPE_DISTANCE = 0;
    int TYPE_TIME = 1;
    int TYPE_PACE = 2;

    BigDecimal ONE_THOUSAND = new BigDecimal(1000);

    int getType();

}
