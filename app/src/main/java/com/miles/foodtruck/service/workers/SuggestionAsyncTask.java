package com.miles.foodtruck.service.workers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.miles.foodtruck.controller.SuggestionNotification;
import com.miles.foodtruck.model.TrackacbleManager;
import com.miles.foodtruck.model.abstracts.AbstractTrackable;
import com.miles.foodtruck.service.LocationService;
import com.miles.foodtruck.service.TrackingService;
import com.miles.foodtruck.util.Constant;
import com.miles.foodtruck.util.Helpers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Called when suggestion needed.
//Generate the suggestion list.
//Then send to notification.
public class SuggestionAsyncTask extends AsyncTask<Void,Integer,Void>{

    private static final String LOG_TAG = SuggestionAsyncTask.class.getName();
    private Context context;
    private ArrayList<TrackableInfo> trackableInfos = new ArrayList<>();


    public SuggestionAsyncTask(Context context){

        this.context = context;

    }

    //Work in another thread
    @Override
    protected Void doInBackground(Void... voids) {

        //Case not got the current location. Cancel.
        if (LocationService.getCurrLocation()== null)
        {
            this.cancel(true);
        }

        //Get all trackables in the memory
        final ArrayList<AbstractTrackable> trackables =
                TrackacbleManager.getTrackableList();

        //trackable info used to store the distance information for this trackable.
        trackableInfos = new ArrayList<>();

        if (trackables.size() != 0) {

            //For each trackable, get the distance to meet location and walk time
            //And store into trackable info
            for (AbstractTrackable trackable : trackables) {

                //Get available tracking info from tracking service
                //Filter the tracking info, remove non-stops
                List<TrackingService.TrackingInfo> trackingInfos =
                        Helpers.getTrackingInfoForTrackable(Integer.toString(trackable.getId()),
                                new Date(), context, true);


                //for each tracking info, determine whether it can be arrived before it leaves.
                if (trackingInfos.size() != 0) {

                    for (TrackingService.TrackingInfo trackingInfo : trackingInfos) {

                        //use api to calculate distance + time.
                        TrackableInfo trackableInfo = getTrackableInfo(trackingInfo);


                        if (trackableInfo == null)
                        {
                            continue;
                        }
                        //If generated trackable info is in the tracking info time slot, or Can be
                        //reached even departure in the future
                        //Add to the list
                        if((trackableInfo.meetTime.after(trackableInfo.targetStartTime) &&
                                trackableInfo.meetTime.before(trackableInfo.targetEndTime))||
                                trackableInfo.meetTime.getTime() == trackableInfo.targetStartTime.getTime()||
                                trackableInfo.meetTime.getTime() == trackableInfo.targetEndTime.getTime()||
                                trackableInfo.meetTime.before(trackableInfo.targetStartTime))
                        {
                            trackableInfo.title = trackable.getName();
                            if(trackableInfo.meetTime.before(trackableInfo.targetStartTime))
                            {
                                trackableInfo.meetTime = trackableInfo.targetStartTime;
                            }
                            //Add to the list and put the nearest at first.
                            if(trackableInfos.size() ==0)
                            {
                                trackableInfos.add(trackableInfo);
                            }
                            else
                            {
                                int position = -1;
                                for (int i = 0; i < trackableInfos.size();i++)
                                {
                                    if (trackableInfo.distance < trackableInfos.get(i).distance)
                                    {
                                        position = i;
                                        break;
                                    }
                                }

                                if (position != -1)
                                {
                                    trackableInfos.add(position,trackableInfo);
                                }
                                else
                                {
                                    trackableInfos.add(trackableInfo);
                                }
                            }
                        }

                    }//end for loop
                }
            }
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {

        //Show notification
        if (trackableInfos.size()!=0)
        {
            SuggestionNotification suggestionNotification =
                    new SuggestionNotification(context, trackableInfos);

            suggestionNotification.show();
        }



    }



    public static TrackableInfo getTrackableInfo(TrackingService.TrackingInfo trackingInfo)  {

        //API URL
        String APIUrl = Constant.DistanceUrl + Constant.DistanceKey;

        StringBuilder htmlStringBuilder = new StringBuilder();
        HttpURLConnection connection = null;

        //Get Destination and current location
        String startEnd = String.format("&origins=%f,%f&destinations=%f,%f",
                LocationService.getCurrLocation().getLatitude(),
                LocationService.getCurrLocation().getLongitude(),
                trackingInfo.latitude,
                trackingInfo.longitude);


        URL url;

        try {
            url = new URL(APIUrl + startEnd);
            // set some connection properties
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // milliseconds
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

            // check the response code
            int statusCode = connection.getResponseCode();


            if (statusCode != HttpURLConnection.HTTP_OK)
            {
                Log.e(LOG_TAG, "Invalid Response Code: " + statusCode);
                return null;
            }

            // wrap the stream to extract its contents (the HTML at the given URL)
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));

            // calculate number of blocks for progress updates
            int length = connection.getContentLength();

            // some sites don't set the length so we guess!
            // OPTION: could switch to indeterminate progress widget
            if (length == -1)
                length = 50000;

            String line;
            while ((line = br.readLine()) != null)
            {

                htmlStringBuilder.append(line);
            }

            //Parse JSON responds and build trackable info
            TrackableInfo trackableInfo = parseJSON(htmlStringBuilder.toString());
            trackableInfo.meetLocation = Double.toString(trackingInfo.latitude)+","+
                    Double.toString(trackingInfo.longitude);
            trackableInfo.trackableId = trackingInfo.trackableId;
            trackableInfo.targetStartTime = trackingInfo.date;
            trackableInfo.targetEndTime = Helpers.caculateEndTime(trackingInfo.date,trackingInfo.stopTime);
            trackableInfo.meetTime = Helpers.caculateEndTime(new Date(),trackableInfo.duration/60);

            return trackableInfo;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //close connection
            if (connection!=null)
            {
                connection.disconnect();
            }
        }

        return null;
    }

    //Data Access Object Only
    public static class TrackableInfo {
        public int trackableId;
        public int distance;
        public int duration;
        public String title;
        public Date targetStartTime;
        public Date targetEndTime;
        public Date meetTime;
        public String meetLocation;

        @Override
        public String toString() {

            String str =
                    String.format("Trackable ID: %d \nDistance: %d \nTravel Time: %d\nMeet Time: %s",
                    trackableId,distance,duration,meetTime.toString());

            return str;

        }
    }

    //Parse JSON to trackable info object
    public static TrackableInfo parseJSON(String str){

        try {
            JSONObject info = new JSONObject(str);
            JSONObject element = info.getJSONArray("rows").getJSONObject(0)
                    .getJSONArray("elements").getJSONObject(0);


            int distance = element.getJSONObject("distance").getInt("value");
            int duration = element.getJSONObject("duration").getInt("value");
            TrackableInfo trackableInfo = new TrackableInfo();
            trackableInfo.distance = distance;
            trackableInfo.duration = duration;

            return trackableInfo;


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }



}
