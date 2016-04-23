package metropolia.edu.musicrecommendations;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.client.Response;

/**
 * Created by petri on 23.4.2016.
 */
public class ProsessTracks {
    private SpotifyApi api;
    private SpotifyService spotify;
    private String TAG = "ProsessTracks";
    private String USER_ID;

    public ProsessTracks() {
        api = new SpotifyApi();
        api.setAccessToken(MainActivity.ACCESS_TOKEN);
        spotify = api.getService();

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                while(USER_ID == null) {
                    USER_ID = spotify.getMe().id;
                    Log.d(TAG, USER_ID);
                }
                testData();
            }
        }).start();*/
        testData();
    }

    public void prosessTrackList(JSONObject tracklist){

    }

    public void testData() {
        List<String> tracks = new ArrayList<>();
        List<String> artists = new ArrayList<>();

        tracks.add("Täytyy Jaksaa");
        artists.add("Elastinen");

        tracks.add("Lähiöunelmii");
        artists.add("Teflon Brothers");

        tracks.add("Sireenit");
        artists.add("Evelina");

        tracks.add("Oota Mua");
        artists.add("Elastinen");

        tracks.add("No Money");
        artists.add("Galantis");

        for(int i = 0; i < 5; i++) {
            searchSpotifyUri(tracks.get(i), artists.get(i));
        }

    }

    private synchronized void searchSpotifyUri(final String track, final String artist){
        Log.d(TAG, "Searching for: "+ track +" - "+ artist);
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 20);

        spotify.searchTracks(track, options, new SpotifyCallback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                Log.d(TAG, "Found some tracks");
                getCorrectTrack(tracksPager.tracks.items, track, artist);
            }

            @Override
            public void failure(SpotifyError error) {
                Log.d(TAG, "Error when getting tracks");
            }
        });
    }

    private void getCorrectTrack(List<Track> trackList, String qTrack, String qArtist) {
        for(Track track : trackList) {
            if(track.name.equals(qTrack)) {
                for(ArtistSimple artist : track.artists) {
                    Log.d(TAG, "Track: "+ track.name +" qTrack: "+ qTrack);
                    Log.d(TAG, "Artist: "+ artist.name +" qArtist: "+ qArtist);

                    if(artist.name.equals(qArtist)) {
                        Log.d(TAG, "Track matches query strings");
                        addTrackToQueue(track.uri);
                    }
                }
            }
        }
    }

    private void addTrackToQueue(String uri) {
        Log.d(TAG, "Adding track to playlist");
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uris", uri);
        spotify.addTracksToPlaylist("XXXXXXX", "2V6Gfrk8eoIir5QaKjTNYW", parameters, parameters, new SpotifyCallback<Pager<PlaylistTrack>>() {
            @Override
            public void failure(SpotifyError spotifyError) {
                Log.d(TAG, "Error in adding tracks to auto-generated playlist: " + spotifyError);
            }

            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, retrofit.client.Response response) {
                Log.d(TAG, "Success in adding track to auto-generated playlist");
            }
        });
    }
}
