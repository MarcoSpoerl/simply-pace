package com.marcospoerl.simplypace.model;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.math.MathContext;

public class DistanceTerm implements Term {

    private BigDecimal distanceInMeter = BigDecimal.ZERO;

    public void setDistanceInMeter(BigDecimal distanceInMeter) {
        this.distanceInMeter = distanceInMeter;
    }

    public BigDecimal getDistanceInMeter() {
        return distanceInMeter;
    }

    public void setDistanceInKilometer(BigDecimal distanceInKilometer) {
        setDistanceInMeter(distanceInKilometer.multiply(ONE_THOUSAND));
    }

    public BigDecimal getDistanceInKilometer() {
        return getDistanceInMeter().divide(ONE_THOUSAND, MathContext.DECIMAL128);
    }

    @Override
    public int getType() {
        return TYPE_DISTANCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }

        DistanceTerm that = (DistanceTerm) o;

        return Objects.equal(this.distanceInMeter, that.distanceInMeter);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.distanceInMeter);
    }
}
