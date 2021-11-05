package com.musicStreamer.refactoring.playlist;

import com.google.inject.Inject;
import com.musicStreamer.refactoring.playlist.data.PlayListTrack;
import com.musicStreamer.refactoring.playlist.data.Track;
import com.musicStreamer.refactoring.playlist.exception.PlaylistException;
import org.testng.annotations.*;

import java.util.*;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;


@Guice(modules = TestBusinessModule.class)
public class PlaylistBusinessBeanTest {

    @Inject
    PlaylistBusinessBean playlistBusinessBean;

    private ArrayList<Integer> randomInt;

    @BeforeClass
    public void setUp() throws Exception {
        randomInt = new ArrayList<>(playlistBusinessBean.MAXPLAYLISTSIZE);
        for (int i = 0; i< playlistBusinessBean.MAXPLAYLISTSIZE; ++i) {
            randomInt.add(i);
        }
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @DataProvider(name = "Playlists")
    public Object[][] getPlaylists() {
        return new Object[][] {
            {generateTrackList(2), 2},
            {generateTrackList(5), 5},
            {generateTrackList(0), 0},
            {generateTrackList(5454), 5454},
            {generateTrackList(999), 999},
            {generateTrackList(10), 10},
            {generateTrackList(1), 1},
            {null, 0}
        };
    }

    @DataProvider(name = "Indexes")
    public Object[][] getIndexes() {
        return new Object[][] {
                {generateIndexes(2), 2},
                {generateIndexes(10), 10},
                {generateIndexes(0), 0},
                {generateIndexes(4), 4},
                {null, 0}
        };
    }

    @Test(dataProvider = "Playlists")
    public void testAddTracks(List<Track> trackList, int expectedSize) throws Exception {
        String uuid = UUID.randomUUID().toString();
        try {
            List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(uuid, trackList, 5);
            assertEquals(playListTracks.size(), expectedSize);
        } catch (PlaylistException ex) {
            // Here I want to test the case I try to get a playlist > 500 (maxPlayListSize)
            assertTrue(trackList.size() + playlistBusinessBean.getCurrentTrackListSize(uuid) > playlistBusinessBean.MAXPLAYLISTSIZE);
        }
    }
    @Test(dataProvider = "Indexes")
    public void testDeleteTracks(List<Integer> indexes, int expectedSize) {
        String uuid = UUID.randomUUID().toString();
        /* Here I want to test with a full playlist so I should add 500 tracks but we currently have the default
        * playlist with 376  tracks so I add just 124 tracks*/
        List<Track> tl = generateTrackList(124);
        List<PlayListTrack> playListTracks = playlistBusinessBean.addTracks(uuid, tl, 0);
        List<PlayListTrack> deleted = playlistBusinessBean.removeTracks(uuid, indexes);

        int a = deleted.size();
        assertEquals(deleted.size(), expectedSize);
    }


    private List<Track> generateTrackList(int size) {
        LinkedList<Track> trackList = new LinkedList<>();
        for (int i = 0; i<size;++i) {
            Track track = new Track();
            track.setArtistId(i);
            track.setTitle("A brand new track "+ i);
            track.setId(76868+i);
            trackList.add(track);
        }
        return trackList;
    }

    private List<Integer> generateIndexes(int size) {
        LinkedList<Integer> indexes = new LinkedList<>();
        // This is just a way to create a collection of random not repetitive integers
        // possible additional work would be to find a more efficient way do to this
        Collections.shuffle(randomInt);
        for (int i = 0; i<size; ++i) {
            indexes.add(randomInt.get(i));
        }
        return indexes;
    }
}




























