package com.attendance.backend.util;

public final class DistanceCalculator {

    private static final double EARTH_RADIUS_METERS = 6_371_000;

    private DistanceCalculator() {
    }

    public static double calculateMeters(double lat1, double lon1, double lat2, double lon2) {
        double latitudeDistance = Math.toRadians(lat2 - lat1);
        double longitudeDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METERS * c;
    }
}
