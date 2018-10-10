package com.marcospoerl.simplypace.model;

import java.math.MathContext;

public class Calculator {

    public static void calculate(Term term1, Term term2, Term term3) {
        if (term3 instanceof DistanceTerm) {
            calculateDistance((DistanceTerm) term3, term1, term2);
        } else if (term3 instanceof TimeTerm) {
            calculateTime((TimeTerm) term3, term1, term2);
        } else if (term3 instanceof PaceTerm) {
            calculatePace((PaceTerm) term3, term1, term2);
        }
    }

    private static void calculateDistance(DistanceTerm distanceTerm, Term term1, Term term2) {
        TimeTerm timeTerm;
        PaceTerm paceTerm;
        if (term1 instanceof TimeTerm) {
            timeTerm = (TimeTerm) term1;
            paceTerm = (PaceTerm) term2;
        } else {
            paceTerm = (PaceTerm) term1;
            timeTerm = (TimeTerm) term2;
        }
        if (0 != paceTerm.getSecondsPerKilometer().longValue()) {
            distanceTerm.setDistanceInKilometer(timeTerm.getTimeInSeconds().divide(paceTerm.getSecondsPerKilometer(), MathContext.DECIMAL128));
        }
    }

    private static void calculateTime(TimeTerm timeTerm, Term term1, Term term2) {
        DistanceTerm distanceTerm;
        PaceTerm paceTerm;
        if (term1 instanceof DistanceTerm) {
            distanceTerm = (DistanceTerm) term1;
            paceTerm = (PaceTerm) term2;
        } else {
            paceTerm = (PaceTerm) term1;
            distanceTerm = (DistanceTerm) term2;
        }
        timeTerm.setTimeInSeconds(distanceTerm.getDistanceInKilometer().multiply(paceTerm.getSecondsPerKilometer()));
    }

    private static void calculatePace(PaceTerm paceTerm, Term term1, Term term2) {
        DistanceTerm distanceTerm;
        TimeTerm timeTerm;
        if (term1 instanceof DistanceTerm) {
            distanceTerm = (DistanceTerm) term1;
            timeTerm = (TimeTerm) term2;
        } else {
            timeTerm = (TimeTerm) term1;
            distanceTerm = (DistanceTerm) term2;
        }
        if (0 != distanceTerm.getDistanceInMeter().longValue()) {
            paceTerm.setSecondsPerKilometer(timeTerm.getTimeInSeconds().divide(distanceTerm.getDistanceInKilometer(), MathContext.DECIMAL128));
        }
    }
}
