package com.itcom202.weroom.account.profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable {
    private List<String> match = new ArrayList<>();
    private List<String> liked = new ArrayList<>();
    private List<String> externalLikes = new ArrayList<>();
    private List<String> dislike = new ArrayList<>();
    public Match(){}

    public void addMatch(String elementID){
        match.add(elementID);
    }
    public void addLiked(String elementID){
        liked.add(elementID);
        if (externalLikes.contains(elementID))
            addMatch(elementID);
    }
    public void addExternalLikes(String elementID){
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
}
