package com.itcom202.weroom.framework;

import com.google.firebase.firestore.FirebaseFirestore;
import com.itcom202.weroom.account.models.Profile;

/**
 * SingleTone for Profile.
 * <p>
 * Used during the application to have a single point to get and update the value of the users Profile.
 */
public class ProfileSingleton {
    private static ProfileSingleton single_instance = null;
    private Profile userProfile;

    private ProfileSingleton( ) {
    }

    /**
     * Return the value of the current profile of the application.
     *
     * @return Current profile in the application. If there is no profiles return null;
     */
    public static synchronized Profile getInstance( ) {
        if ( single_instance == null ) {
            single_instance = new ProfileSingleton( );
        }
        return single_instance.userProfile;
    }

    /**
     * Initialize the value of userProfile with a given Profile.
     *
     * @param profile Profile to initialize the SingleTon.
     */
    public static void initialize( Profile profile ) {
        single_instance.userProfile = profile;
    }

    /**
     * Updates the value of the SingleTon and updates the value in the Firebase database.
     *
     * @param profile Profile to update the SingleTon.
     */
    public static void update( Profile profile ) {
        single_instance.userProfile = profile;
        FirebaseFirestore db = FirebaseFirestore.getInstance( );

        db.collection( DataBasePath.USERS.getValue( ) )
                .document( profile.getUserID( ) )
                .set( profile );
    }

    /**
     * Checks the profile contained in the SingleTon is a finalized profile.
     *
     * @return true if the profile from singleton is finished, false if there is missing data to have a finished account.
     */
    public static boolean isFinishedProfile( ) {
        if ( single_instance == null )
            return false;


        return ( single_instance.userProfile.getTenant( ) != null ) ||
                ( single_instance.userProfile.getLandlord( ) != null && single_instance.userProfile.getLandlord( ).getRoomsID( ) != null
                        && single_instance.userProfile.getLandlord( ).getRoomsID( ).size( ) > 0 );
    }
}
