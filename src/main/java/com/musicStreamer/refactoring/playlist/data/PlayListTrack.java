package com.musicStreamer.refactoring.playlist.data;

import java.io.Serializable;
import java.util.Date;

public class PlayListTrack implements Serializable, Comparable<PlayListTrack> {

    private static final long serialVersionUID = 5464240796158432162L;

    private Integer id;
    private PlayList playlist;
    private int index;
    private Date dateAdded;
    private int trackId;

    private Track track;

    public PlayListTrack() {
        dateAdded = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public PlayList getTrackPlayList() {
        return playlist;
    }

    public void setTrackPlaylist(PlayList playlist) {
        this.playlist = playlist;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int compareTo(PlayListTrack o) {
        return this.getIndex() - o.getIndex();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayListTrack that = (PlayListTrack) o;

        if (index != that.index) return false;
        if (trackId != that.trackId) return false;
        if (dateAdded != null ? !dateAdded.equals(that.dateAdded) : that.dateAdded != null) return false;
        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + index;
        result = 31 * result + (dateAdded != null ? dateAdded.hashCode() : 0);
        result = 31 * result + trackId;
        return result;
    }

    public String toString() {
        return "PlayListTrack id[" + getId() + "], trackId[" + getTrackId() + "]";
    }
}
