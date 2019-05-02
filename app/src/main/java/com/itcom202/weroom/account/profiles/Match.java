package com.itcom202.weroom.account.profiles;

import android.os.Parcel;
import android.os.Parcelable;

import com.itcom202.weroom.ProfileSingleton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable , Parcelable {
    private List<String> match = new ArrayList<>();
    private List<String> liked = new ArrayList<>();
    private List<String> externalLikes = new ArrayList<>();
    private List<String> dislike = new ArrayList<>();
    public Match(){}

    protected Match(Parcel in) {
        match = in.createStringArrayList();
        liked = in.createStringArrayList();
        externalLikes = in.createStringArrayList();
        dislike = in.createStringArrayList();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    private void addMatch(String elementID){
        if (!match.contains(elementID))
            match.add(elementID);
    }
    public void addLiked(String elementID){
        if (!liked.contains(elementID))
            liked.add(elementID);
        if (externalLikes.contains(elementID))
            addMatch(elementID);

    }
    public void addExternalLikes(String elementID){
        if (!externalLikes.contains(elementID))
            externalLikes.add(elementID);
        if (liked.contains(elementID))
            addMatch(elementID);
    }
    public void addDislike(String elementID){
        dislike.add(elementID);
        match.remove(elementID);
    }

    public List<String> getMatch() {
        return match;
    }

    public void setMatch(List<String> match) {
        this.match = match;
    }

    public List<String> getLiked() {
        return liked;
    }

    public void setLiked(List<String> liked) {
        this.liked = liked;
    }

    public List<String> getExternalLikes() {
        return externalLikes;
    }

    public void setExternalLikes(List<String> externalLikes) {
        this.externalLikes = externalLikes;
    }

    public List<String> getDislike() {
        return dislike;
    }

    public void setDislike(List<String> dislike) {
        this.dislike = dislike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(match);
        dest.writeStringList(liked);
        dest.writeStringList(externalLikes);
        dest.writeStringList(dislike);
    }
}
