package com.marcospoerl.simplypace.model;

import com.google.common.base.Objects;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.TimeUnit;


public class PaceTerm implements Term {

    private BigDecimal secondsPerKilometer = BigDecimal.ZERO;
    private PaceMode paceMode;

    public void setPaceMode(PaceMode paceMode) {
        this.paceMode = paceMode;
    }

    public void setPace(BigDecimal pace){
            if (pace.compareTo(BigDecimal.ZERO) == 0) {
                this.secondsPerKilometer = BigDecimal.ZERO;
            } else {
                switch (paceMode) {
                    case MIN:
                        this.secondsPerKilometer = pace;
                        break;
                    case KMH:
                        this.secondsPerKilometer = SECONDS_PER_HOUR.divide(pace, MathContext.DECIMAL128);
                        break;
                    case MIN100:
                        this.secondsPerKilometer = pace.multiply(TEN);
                }
            }
    }

    public void setSecondsPerKilometer(BigDecimal secondsPerKilometer) {
        this.secondsPerKilometer = secondsPerKilometer;
    }

    public BigDecimal getSecondsPerKilometer() {
        return secondsPerKilometer;
    }

    private BigDecimal getKilometersPerHour() {
        if (secondsPerKilometer.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return SECONDS_PER_HOUR.divide(secondsPerKilometer, MathContext.DECIMAL128);
    }

    private BigDecimal getSecondsPerHundredMeter(){
        return secondsPerKilometer.divide(TEN, MathContext.DECIMAL128);
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

    public String asText() {
        switch (paceMode) {
            case MIN:
                Long secondsPerKilometerL = secondsPerKilometer.longValue();
                return String.format("%d:%02d min/km",
                        TimeUnit.SECONDS.toMinutes(secondsPerKilometerL),
                        TimeUnit.SECONDS.toSeconds(secondsPerKilometerL) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(secondsPerKilometerL)));
            case KMH:
                return String.format("%,.2f km/h", getKilometersPerHour());
            case MIN100:
                Long secondsPerHundretMeter = getSecondsPerHundredMeter().longValue();
                return String.format("%d:%02d min/100m",
                        TimeUnit.SECONDS.toMinutes(secondsPerHundretMeter),
                        TimeUnit.SECONDS.toSeconds(secondsPerHundretMeter) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(secondsPerHundretMeter)));
        }
        return null;
    }
}
