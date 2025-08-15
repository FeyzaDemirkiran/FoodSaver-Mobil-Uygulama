package com.example.foodsaver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmBildirimPlanlayici {

    @SuppressLint("ScheduleExactAlarm")
    public static void planla(Context context, String urunAdi, String sonKullanmaStr, int uyariGunu) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        Calendar hedefZaman = Calendar.getInstance();

        try {
            hedefZaman.setTime(sdf.parse(sonKullanmaStr));
        } catch (ParseException e) {
            Toast.makeText(context, "Tarih formatı hatalı!", Toast.LENGTH_SHORT).show();
            return;
        }


        hedefZaman.add(Calendar.DAY_OF_MONTH, -uyariGunu);
        hedefZaman.set(Calendar.HOUR_OF_DAY, 10);
        hedefZaman.set(Calendar.MINUTE, 58);
        hedefZaman.set(Calendar.SECOND, 0);
        hedefZaman.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("urun_adi", urunAdi);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                urunAdi.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                hedefZaman.getTimeInMillis(),
                pendingIntent
        );
    }
}

