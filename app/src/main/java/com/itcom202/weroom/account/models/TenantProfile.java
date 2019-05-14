package com.itcom202.weroom.account.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.InputMismatchException;

public class TenantProfile implements Serializable, Parcelable {
    public static final Creator<TenantProfile> CREATOR = new Creator<TenantProfile>( ) {
        @Override
        public TenantProfile createFromParcel( Parcel in ) {
            return new TenantProfile( in );
        }

        @Override
        public TenantProfile[] newArray( int size ) {
            return new TenantProfile[ size ];
        }
    };

    private static final int LESS_THAN_THREE_MONTH = 1;
    private static final int THREE_TO_SIX_MONTH = 2;
    private static final int SIX_TO_TWELVE_MONTH = 3;
    private static final int OVER_TWELVE_MONTH = 4;
    private static final String YES = "Yes";
    private static final String NO = "No";
    private static final String I_DONT_CARE = "Does not matter";
    private String mUserUid;
    private int mDistanceCenter;
    private int mPeriodOfRent;
    private int mMinDeposit;
    private int mMAxDeposit;
    private int mMinRent;
    private int mMaxRent;
    private String mChosenCityname;
    private String mChosenCityId;
    private double mCityLatitude;
    private double mCityLongitude;
    private String mFurnished;
    private String mInternet;
    private String mLaundry;
    private String mPetFriendly;
    private String mSmokeFriendly;

    protected TenantProfile( Parcel in ) {
        mUserUid = in.readString( );
        mDistanceCenter = in.readInt( );
        mPeriodOfRent = in.readInt( );
        mMinDeposit = in.readInt( );
        mMAxDeposit = in.readInt( );
        mMinRent = in.readInt( );
        mMaxRent = in.readInt( );
        mChosenCityname = in.readString( );
        mChosenCityId = in.readString( );
        mCityLatitude = in.readDouble( );
        mCityLongitude = in.readDouble( );
        mFurnished = in.readString( );
        mInternet = in.readString( );
        mLaundry = in.readString( );
        mPetFriendly = in.readString( );
        mSmokeFriendly = in.readString( );
    }

    //public constructor needed so the fire base can de-serialize the object.
    public TenantProfile( ) {
    }

    @Override
    public int describeContents( ) {
        return 0;
    }

    @Override
    public void writeToParcel( Parcel dest, int flags ) {
        dest.writeString( mUserUid );
        dest.writeInt( mDistanceCenter );
        dest.writeInt( mPeriodOfRent );
        dest.writeInt( mMinDeposit );
        dest.writeInt( mMAxDeposit );
        dest.writeInt( mMinRent );
        dest.writeInt( mMaxRent );
        dest.writeString( mChosenCityname );
        dest.writeString( mChosenCityId );
        dest.writeDouble( mCityLatitude );
        dest.writeDouble( mCityLongitude );
        dest.writeString( mFurnished );
        dest.writeString( mInternet );
        dest.writeString( mLaundry );
        dest.writeString( mPetFriendly );
        dest.writeString( mSmokeFriendly );
    }

    public String getUserUid( ) {
        return mUserUid;
    }

    public void setUserUid( String userUid ) {
        mUserUid = userUid;
    }

    public int getDistanceCenter( ) {
        return mDistanceCenter;
    }

    public void setDistanceCenter( int distanceCenter ) {
        mDistanceCenter = distanceCenter;
    }

    public int getPeriodOfRent( ) {
        return mPeriodOfRent;
    }

    public void setPeriodOfRent( int periodOfRent ) {
        mPeriodOfRent = periodOfRent;
    }

    public int getMinDeposit( ) {
        return mMinDeposit;
    }

    public void setMinDeposit( int minDeposit ) {
        mMinDeposit = minDeposit;
    }

    public int getMAxDeposit( ) {
        return mMAxDeposit;
    }

    public void setMAxDeposit( int MAxDeposit ) {
        mMAxDeposit = MAxDeposit;
    }

    public int getMinRent( ) {
        return mMinRent;
    }

    public void setMinRent( int minRent ) {
        mMinRent = minRent;
    }

    public int getMaxRent( ) {
        return mMaxRent;
    }

    public void setMaxRent( int maxRent ) {
        mMaxRent = maxRent;
    }

    public String getmFurnished( ) {
        return mFurnished;
    }

    public void setmFurnished( String mFurnished ) {
        this.mFurnished = mFurnished;
    }

    public String getmInternet( ) {
        return mInternet;
    }

    public void setmInternet( String mInternet ) {
        this.mInternet = mInternet;
    }

    public String getmLaundry( ) {
        return mLaundry;
    }

    public void setmLaundry( String mLaundry ) {
        this.mLaundry = mLaundry;
    }

    public String getPetFriendly( ) {
        return mPetFriendly;
    }

    public void setPetFriendly( String petFriendly ) {
        mPetFriendly = petFriendly;
    }

    public String getSmokeFriendly( ) {
        return mSmokeFriendly;
    }

    public void setSmokeFriendly( String smokeFriendly ) {
        mSmokeFriendly = smokeFriendly;
    }

    public String getChoosenCityname( ) {
        return mChosenCityname;
    }

    public void setChoosenCityname( String choosenCityname ) {
        mChosenCityname = choosenCityname;
    }

    public String getChoosenCityId( ) {
        return mChosenCityId;
    }

    public void setChoosenCityId( String choosenCityId ) {
        mChosenCityId = choosenCityId;
    }

    public double getCityLatitude( ) {
        return mCityLatitude;
    }

    public void setCityLatitude( double cityLatitude ) {
        mCityLatitude = cityLatitude;
    }

    public double getCityLongitude( ) {
        return mCityLongitude;
    }

    public void setCityLongitude( double cityLongitude ) {
        mCityLongitude = cityLongitude;
    }

    public static class Builder {
        private String sUserUid;
        private int sDistanceCenter;
        private int sPeriodOfRent;
        private int sMinDeposit;
        private int sMaxDeposit;
        private int sMinRent;
        private int sMaxRent;
        private String sChosenCityName;
        private String sChosenCityID;
        private double sLatitudeCity;
        private double sLongitudeCity;
        private String sFurnished;
        private String sInternet;
        private String sLaundry;
        private String sPetFriendly;
        private String sSmokeFriendly;

        public Builder( String userUid ) {
            this.sUserUid = userUid;
        }

        public Builder distanceFromCenter( int distance ) {
            if ( distance < 0 || distance > 50 )
                throw new InputMismatchException( "Wrong distance" );
            sDistanceCenter = distance;
            return this;
        }

        public Builder withRentingPeriod( int period ) {
            if ( period != LESS_THAN_THREE_MONTH && period != THREE_TO_SIX_MONTH &&
                    period != SIX_TO_TWELVE_MONTH && period != OVER_TWELVE_MONTH )
                throw new InputMismatchException( "Wrong period time" );
            sPeriodOfRent = period;
            return this;
        }

        public Builder withDepositRange( int min, int max ) {
            if ( min > max )
                throw new InputMismatchException( "wrong deposit input" );
            sMinDeposit = min;
            sMaxDeposit = max;
            return this;
        }

        public Builder withRentRange( int min, int max ) {
            if ( min > max )
                throw new InputMismatchException( "Wrong rent input" );
            sMinRent = min;
            sMaxRent = max;
            return this;
        }

        public Builder withCity( String id, String name, double latitude, double longitude ) {
            sChosenCityID = id;
            sChosenCityName = name;
            sLongitudeCity = longitude;
            sLatitudeCity = latitude;
            return this;
        }

        public Builder isFurnished( String f ) {
            if ( ! f.equals( YES ) && ! f.equals( NO ) && ! f.equals( I_DONT_CARE ) )
                throw new InputMismatchException( "Wrong answer - furnished" );
            sFurnished = f;
            return this;
        }

        public Builder hasInternet( String i ) {
            if ( ! i.equals( YES ) && ! i.equals( NO ) && ! i.equals( I_DONT_CARE ) )
                throw new InputMismatchException( "Wrong answer - internet" );
            sInternet = i;
            return this;
        }

        public Builder hasLaundry( String l ) {
            if ( ! l.equals( YES ) && ! l.equals( NO ) && ! l.equals( I_DONT_CARE ) )
                throw new InputMismatchException( "Wrong answer - laundry" );
            sLaundry = l;
            return this;
        }

        public Builder isPetFriendly( String p ) {
            if ( ! p.equals( YES ) && ! p.equals( NO ) && ! p.equals( I_DONT_CARE ) )
                throw new InputMismatchException( "Wrong answer - pet friendly" );
            sPetFriendly = p;
            return this;
        }

        public Builder isSmokingFriendly( String s ) {
            if ( ! s.equals( YES ) && ! s.equals( NO ) && ! s.equals( I_DONT_CARE ) )
                throw new InputMismatchException( "Wrong answer - smoking friendly" );
            sSmokeFriendly = s;
            return this;
        }

        public TenantProfile build( ) {
            TenantProfile t = new TenantProfile( );
            t.mUserUid = this.sUserUid;
            t.mDistanceCenter = this.sDistanceCenter;
            t.mPeriodOfRent = this.sPeriodOfRent;
            t.mMinDeposit = this.sMinDeposit;
            t.mMAxDeposit = this.sMaxDeposit;
            t.mMinRent = this.sMinRent;
            t.mMaxRent = this.sMaxRent;
            t.mFurnished = this.sFurnished;
            t.mInternet = this.sInternet;
            t.mLaundry = this.sLaundry;
            t.mPetFriendly = this.sPetFriendly;
            t.mSmokeFriendly = this.sSmokeFriendly;
            t.mChosenCityname = this.sChosenCityName;
            t.mChosenCityId = this.sChosenCityID;
            t.mCityLatitude = this.sLatitudeCity;
            t.mCityLongitude = this.sLongitudeCity;
            return t;
        }
    }
}
