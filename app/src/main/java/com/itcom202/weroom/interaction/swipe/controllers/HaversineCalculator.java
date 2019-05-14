package com.itcom202.weroom.interaction.swipe.controllers;

/**
 * Class that contains unique method to calculate the Haversine distance between to coordinates.
 * read more at: https://en.wikipedia.org/wiki/Haversine_formula
 */
class HaversineCalculator {

    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    /**
     * Method that return the distance between two coordinate points in Earth.
     *
     * @param startLat  Point 1 Latitude.
     * @param startLong Point 1 Longitude.
     * @param endLat    Point 2 Latitude.
     * @param endLong   Point 2 Longitude.
     * @return distance between point 1 and point 2
     */
    static double distance( double startLat, double startLong,
                            double endLat, double endLong ) {

        double dLat = Math.toRadians( ( endLat - startLat ) );
        double dLong = Math.toRadians( ( endLong - startLong ) );

        startLat = Math.toRadians( startLat );
        endLat = Math.toRadians( endLat );

        double a = haversin( dLat ) + Math.cos( startLat ) * Math.cos( endLat ) * haversin( dLong );
        double c = 2 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1 - a ) );

        return EARTH_RADIUS * c; // <-- d
    }

    private static double haversin( double val ) {
        return Math.pow( Math.sin( val / 2 ), 2 );
    }
}
