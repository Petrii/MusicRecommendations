package metropolia.edu.musicrecommendations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

public class MainActivity extends ActionBarActivity {

    private static final String CLIENT_ID = "1e526b554f064d828866b8042c1918c6";
    private static final String REDIRECT_URI = "musicrecommendations://callback";
    private static final int REQUEST_CODE = 1337;
    public static String ACCESS_TOKEN;
    public static ProsessTracks prosessTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "playlist-modify-public", "user-read-email", "playlist-modify-private"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.clearCookies(this);
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        //prosessTracks = new ProsessTracks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    ACCESS_TOKEN = response.getAccessToken();
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
}
