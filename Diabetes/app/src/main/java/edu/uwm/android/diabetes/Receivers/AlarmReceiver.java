package edu.uwm.android.diabetes.Receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.uwm.android.diabetes.Activities.ListDataActivity;
import edu.uwm.android.diabetes.Activities.MainActivity;
import edu.uwm.android.diabetes.Activities.RegimenActivity;
import edu.uwm.android.diabetes.Database.BloodGlucose;
import edu.uwm.android.diabetes.Database.DatabaseHandler;
import edu.uwm.android.diabetes.R;

/**
 * Created by Rafa on 8/14/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    DatabaseHandler databaseHandler;
    String userName;
    boolean dateProblem = false;

    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = sdf.format(new Date());
        if ( currentDate.charAt(0) == '0') {
            currentDate = currentDate.substring(1,9);
            dateProblem = true;
            System.out.println("I got into the If and the new date is "+currentDate);
        } else if(currentDate.charAt(3) == '0'){
            currentDate = currentDate.substring(0,2)+currentDate.substring(4,9);
        }
        if(dateProblem && currentDate.charAt(2)=='0'){
            currentDate = currentDate.substring(0,1)+currentDate.substring(3,8);
        }

        databaseHandler = new DatabaseHandler(context);
        String action = intent.getAction();
        if(action.equals("android.media.action.DISPLAY_NOTIFICATION")){
            System.out.println("This is the value of Username = "+intent.getExtras().getString("userName"));
            if(intent.getExtras().getString("userName")!=null){
                userName = intent.getExtras().getString("userName");
            }
        }
        Intent notificationIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        String dietAndExercise = "You don't have any scheduled diet/exercise for today.";
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        userName = pref.getString("user", userName);
        Cursor result = databaseHandler.getRegimenByDate(currentDate,userName);
        if (result.moveToFirst()) {
            dietAndExercise = "";
            do {
                dietAndExercise =  dietAndExercise + "\n "+ result.getString(2) + "\n "+ result.getString(3) ;
            } while (result.moveToNext());
        }else{
            System.out.println("There is nothing to show");
        }

        Notification notification = builder.setContentTitle("Regimen Reminder")
                .setContentText(dietAndExercise)
                .setTicker("Reminder "+ dietAndExercise)
                .setSmallIcon(R.mipmap.heart)
                .setContentIntent(pendingIntent).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

        }
}
