package com.itcom202.weroom.account.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable, Parcelable {
    public static final Creator<Match> CREATOR = new Creator<Match>( ) {
        @Override
        public Match createFromParcel( Parcel in ) {
            return new Match( in );
        }

        @Override
        public Match[] newArray( int size ) {
            return new Match[ size ];
        }
    };
    private List<String> match = new ArrayList<>( );
    private List<String> liked = new ArrayList<>( );
    private List<String> externalLikes = new ArrayList<>( );
    private List<String> dislike = new ArrayList<>( );
    private boolean lazySwipe = false;

    //public constructor needed for FirebaseFireStore.
    public Match( ) {
    }

    protected Match( Parcel in ) {
        match = in.createStringArrayList( );
        liked = in.createStringArrayList( );
        externalLikes = in.createStringArrayList( );
        dislike = in.createStringArrayList( );
        lazySwipe = in.readByte( ) != 0;
    }

    /**
     * Method that creates a match. Can only be called internally from 'addLiked' and
     * 'addExternalLikes'
     *
     * @param elementID ID of the other user or room that you are creating a match.
     */
    private void addMatch( String elementID ) {
        if ( ! match.contains( elementID ) )
            match.add( elementID );
    }

    /**
     * Method that creates a like to another room or profile in the application.
     * It check if the same ID is contained in @externalLikes and create a match if true.
     *
     * @param elementID ID of the other user or room that we liked.
     */
    public void addLiked( String elementID ) {
        if ( ! liked.contains( elementID ) )
            liked.add( elementID );
        if ( externalLikes.contains( elementID ) )
            addMatch( elementID );

    }

    /**
     * Method used to express that an external room or profile liked our profile/room.
     * It check if the same ID is contained in @liked and create a match if true.
     *
     * @param elementID ID of the external profile or room that liked our profile/room.
     * @return return true if the profile where we are adding a externalLike is a lazySwiper. And false if its not a lazy swiper.
     */
    public boolean addExternalLikes( String elementID ) {
        if ( ! externalLikes.contains( elementID ) )
            externalLikes.add( elementID );
        if ( lazySwipe ) {
            addMatch( elementID );
        } else if ( liked.contains( elementID ) )
            addMatch( elementID );
        return lazySwipe;
    }

    /**
     * Adds the user or room ID to our dislike list, also removes the match if we created a match
     * beforehand.
     *
     * @param elementID ID of the profile/room that we would like to dislike.
     */
    public void addDislike( String elementID ) {
        if ( ! dislike.contains( elementID ) )
            dislike.add( elementID );
        match.remove( elementID );
    }

    public List<String> getMatch( ) {
        return match;
    }

    public void setMatch( List<String> match ) {
        this.match = match;
    }

    public List<String> getLiked( ) {
        return liked;
    }

    public void setLiked( List<String> liked ) {
        this.liked = liked;
    }

    public List<String> getExternalLikes( ) {
        return externalLikes;
    }

    public void setExternalLikes( List<String> externalLikes ) {
        this.externalLikes = externalLikes;
    }

    public List<String> getDislike( ) {
        return dislike;
    }

    public void setDislike( List<String> dislike ) {
        this.dislike = dislike;
    }

    public boolean isLazySwipe( ) {
        return lazySwipe;
    }

    public void setLazySwipe( boolean lazySwipe ) {

        this.lazySwipe = lazySwipe;
    }

    @Override
    public int describeContents( ) {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags ) {
        dest.writeStringList( match );
        dest.writeStringList( liked );
        dest.writeStringList( externalLikes );
        dest.writeStringList( dislike );
        dest.writeByte( ( byte ) ( lazySwipe ? 1 : 0 ) );
    }
}
