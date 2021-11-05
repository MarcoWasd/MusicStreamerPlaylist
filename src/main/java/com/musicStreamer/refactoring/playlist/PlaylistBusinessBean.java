package com.musicStreamer.refactoring.playlist;

import com.google.inject.Inject; 
import com.musicStreamer.refactoring.playlist.dao.PlaylistDaoBean;
import com.musicStreamer.refactoring.playlist.data.PlayListTrack;
import com.musicStreamer.refactoring.playlist.data.Track;
import com.musicStreamer.refactoring.playlist.data.PlayList;
import com.musicStreamer.refactoring.playlist.exception.PlaylistException;

import java.util.*;

public class PlaylistBusinessBean {

    private PlaylistDaoBean playlistDaoBean;
    static final int MAXPLAYLISTSIZE = 500;
    @Inject
    public PlaylistBusinessBean(PlaylistDaoBean playlistDaoBean){
        this.playlistDaoBean = playlistDaoBean;
    }

    /**
     * Add tracks to the index
     */
    List<PlayListTrack> addTracks(String uuid, List<Track> tracksToAdd, int toIndex) throws PlaylistException {


        try {

            final PlayList playList = playlistDaoBean.getPlaylistByUUID(uuid);

            if (tracksToAdd == null || !validateUUID(uuid) || !validateIndexes(toIndex, playList.getNrOfTracks())) {
                return Collections.EMPTY_LIST;
            }
            //We do not allow > maxPlaylistSize tracks in new playlists
            if (playList.getNrOfTracks() + tracksToAdd.size() > MAXPLAYLISTSIZE) {
                throw new PlaylistException("Playlist cannot have more than " + MAXPLAYLISTSIZE + " tracks");
            }

            // The index is out of bounds, put it in the end of the list.
            final int size = playList.getPlayListTracks() == null ? 0 : playList.getPlayListTracks().size();
            if (toIndex > size || toIndex < 0) {
                toIndex = size;
            }

            // Using a Tree guarantee the elements order, we do not need to sort and we can add tracks on the
            // original playlist
            Set<PlayListTrack> originalSet = playList.getPlayListTracks();


            final List<PlayListTrack> added = new ArrayList<PlayListTrack>(tracksToAdd.size());
            final ArrayList<PlayListTrack> tracklist = new ArrayList<PlayListTrack>(originalSet);
            for (Track track : tracksToAdd) {
                PlayListTrack playlistTrack = new PlayListTrack();
                playlistTrack.setTrack(track);
                playlistTrack.setTrackPlaylist(playList);
                playlistTrack.setDateAdded(new Date());
                playlistTrack.setIndex(toIndex);
                playlistTrack.setTrackPlaylist(playList);
                playList.setDuration(modifyTrackDurationToPlaylist(playList, track, true));

                tracklist.add(toIndex, playlistTrack);
                added.add(playlistTrack);
                toIndex++;
            }
            for (int i = toIndex; i<tracklist.size(); ++i) {
                tracklist.get(i).setIndex(tracklist.get(i).getIndex()+tracksToAdd.size());
            }
            playList.setNrOfTracks(originalSet.size());
            TreeSet<PlayListTrack> tl = new TreeSet<>(tracklist);
            playList.setPlayListTracks(tl);
            return added;

        } catch (Exception e) {
            e.printStackTrace();
            throw new PlaylistException("Generic error");
        }
    }
    
	/**
	 * Remove the tracks from the playlist located at the sent indexes
	 */
	List<PlayListTrack> removeTracks(String uuid, List<Integer> indexes) throws PlaylistException {

        // Input check
        if (indexes == null || indexes.size() == 0 || !validateUUID(uuid)) {
            return Collections.EMPTY_LIST;
        }

        final PlayList playList = playlistDaoBean.getPlaylistByUUID(uuid);

        final Set<PlayListTrack> originalSet = playList.getPlayListTracks();
        final ArrayList<PlayListTrack> trackList = new ArrayList<PlayListTrack>(originalSet);
        final List<PlayListTrack> removed = new ArrayList<PlayListTrack>(indexes.size());
        final LinkedList<PlayListTrack> indexDecrease = new LinkedList<>();
        /* The list made from a TreeSet is sorted. When I remove a track I need to update the indexes of the
        * next tracks in the list. I can't remove it on the spot otherwise I would "break" the indexing
        * so for each index I save the list of tracks to remove and the list of tracks of which I need to
        * update the index. After this loop I apply these changes*/
        for (Integer index: indexes) {
            PlayListTrack playListTrack = trackList.get(index);
            removed.add(playListTrack);
            playList.setDuration(modifyTrackDurationToPlaylist(playList, playListTrack.getTrack(), false));
            for (int i = index; i<trackList.size();++i) {
                indexDecrease.add(trackList.get(i));
            }
        }
        for (PlayListTrack plt: indexDecrease) {
            plt.setIndex(plt.getIndex()-1);
        }
        for (PlayListTrack plt:removed) {
            trackList.remove(plt);
        }

        playList.setPlayListTracks(new TreeSet<>(trackList));

        return removed;

	}
    public PlayList getPlaylistByID(String uuid) {
        return playlistDaoBean.getPlaylistByUUID(uuid);
    }

    public int getCurrentTrackListSize(String uuid) {

        return playlistDaoBean.getPlaylistByUUID(uuid).getNrOfTracks();
    }
    private boolean validateUUID(String uuid) {
        // We need to add a check to verify if uuid comply the specific format (for example with a regex). In absence of specification a simple
        // check is implemented
        return uuid != null;
    }
    private boolean validateIndexes(int toIndex, int length) {
        return toIndex >= 0 && toIndex <= length;
    }

    // Modified this method to use it in two ways
    private float modifyTrackDurationToPlaylist(PlayList playList, Track track, boolean add) {

        final float playlistTime = playList != null && playList.getDuration() != null ? playList.getDuration() : 0;
        final float trackTime = track != null ? track.getDuration() : 0;

        if (add)
            return playlistTime + trackTime;
        else
            return playlistTime - trackTime;

    }
}
