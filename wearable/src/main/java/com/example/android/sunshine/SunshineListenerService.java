package com.example.android.sunshine;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.TimeUnit;

public class SunshineListenerService  extends WearableListenerService {

    private final String TAG = SunshineListenerService.class.getSimpleName();

    public static final String WEATHER_PATH = "/weather";
    final String WEATHER_KEY = "com.example.android.sunshine.weather_key";
    final String WEATHER_MIN_KEY = "com.example.android.sunshine.weather_min_key";
    final String WEATHER_MAX_KEY = "com.example.android.sunshine.weather_max_key";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        // Loop through the events and send a message back to the node that created the data item.
        for (DataEvent event : dataEvents) {
            Uri uri = event.getDataItem().getUri();
            String path = uri.getPath();
            if (WEATHER_PATH.equals(path)) {
                if (event.getType() == DataEvent.TYPE_CHANGED) {
                    // DataItem changed
                    DataItem item = event.getDataItem();
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    int weatherId = dataMap.getInt(WEATHER_KEY);
                    double low = dataMap.getDouble(WEATHER_MIN_KEY);
                    double high = dataMap.getDouble(WEATHER_MAX_KEY);

                    SunshineWatchFace.WEATHER_IMAGE = Utils.getSmallArtResourceIdForWeatherCondition(weatherId);
                    SunshineWatchFace.WEATHER_LOW = low;
                    SunshineWatchFace.WEATHER_HIGH = high;
                }
            }
        }
    }
}
