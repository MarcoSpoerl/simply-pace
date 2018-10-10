package com.marcospoerl.simplypace.model;

import com.google.common.base.Objects;

import java.math.BigDecimal;

public class PaceTerm implements Term {

    private BigDecimal secondsPerKilometer = BigDecimal.ZERO;

    public void setSecondsPerKilometer(BigDecimal secondsPerKilometer) {
        this.secondsPerKilometer = secondsPerKilometer;
    }

    public BigDecimal getSecondsPerKilometer() {
        return secondsPerKilometer;
    }

    @Override
    public int getType() {
        return TYPE_PACE;
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

        PaceTerm that = (PaceTerm) o;

        return Objects.equal(this.secondsPerKilometer, that.secondsPerKilometer);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.secondsPerKilometer);
    }
}
