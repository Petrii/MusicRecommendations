package metropolia.edu.musicrecommendations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by petri on 23.4.2016.
 */
public class PlaybackBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mIntent = new Intent(context, SendDataService.class);
        mIntent.putExtra("artist", intent.getStringExtra("artist"));
        mIntent.putExtra("track", intent.getStringExtra("track"));
        //context.startService(mIntent);
    }
}
