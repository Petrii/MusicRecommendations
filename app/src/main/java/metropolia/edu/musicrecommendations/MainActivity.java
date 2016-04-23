package metropolia.edu.musicrecommendations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    private static final String CLIENT_ID = "1e526b554f064d828866b8042c1918c6";
    private static final String REDIRECT_URI = "musicrecommendations://callback";
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        //AuthenticationClient.clearCookies(this);
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    SpotifyApi api = new SpotifyApi();
                    api.setAccessToken(response.getAccessToken());
                    SpotifyService spotify = api.getService();
                    spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
                        @Override
                        public void success(Album album, Response response) {
                            Log.d("Album success", album.name);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("Album failure", error.toString());
                        }
                    });
                    Log.d("Got token", ""+response.getAccessToken());
                    break;
                // Auth flow returned an error
                case ERROR:
                    Log.d("Auth error",""+response.getError());
                    break;
                // Most likely auth flow was cancelled
                default:
                    Log.d("Auth result",""+response.getType());
            }
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        super.onDestroy();
    }

}
