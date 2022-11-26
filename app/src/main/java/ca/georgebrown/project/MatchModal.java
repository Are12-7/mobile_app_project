package ca.georgebrown.project;

import android.os.Parcel;
import android.os.Parcelable;

public class MatchModal implements Parcelable {

    private String matchID;
    private String homeTeam;
    private String awayTeam;
    private String matchDate;
    private String matchImage;
    private String matchLocation;
    private String matchDescription;

    public MatchModal() {
    }

    public MatchModal(String matchID, String homeTeam, String awayTeam, String matchDate, String matchImage, String matchLocation, String matchDescription) {
        this.matchID = matchID;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchDate = matchDate;
        this.matchImage = matchImage;
        this.matchLocation = matchLocation;
        this.matchDescription = matchDescription;
    }

    protected MatchModal(Parcel in) {
        matchID = in.readString();
        homeTeam = in.readString();
        awayTeam = in.readString();
        matchDate = in.readString();
        matchImage = in.readString();
        matchLocation = in.readString();
        matchDescription = in.readString();
    }

    public static final Creator<MatchModal> CREATOR = new Creator<MatchModal>() {
        @Override
        public MatchModal createFromParcel(Parcel in) {
            return new MatchModal(in);
        }

        @Override
        public MatchModal[] newArray(int size) {
            return new MatchModal[size];
        }
    };

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getMatchImage() {
        return matchImage;
    }

    public void setMatchImage(String matchImage) {
        this.matchImage = matchImage;
    }

    public String getMatchLocation() {
        return matchLocation;
    }

    public void setMatchLocation(String matchLocation) {
        this.matchLocation = matchLocation;
    }

    public String getMatchDescription() {
        return matchDescription;
    }

    public void setMatchDescription(String matchDescription) {
        this.matchDescription = matchDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(matchID);
        parcel.writeString(homeTeam);
        parcel.writeString(awayTeam);
        parcel.writeString(matchDate);
        parcel.writeString(matchImage);
        parcel.writeString(matchLocation);
        parcel.writeString(matchDescription);
    }
}
