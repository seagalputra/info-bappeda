package id.go.jabarprov.bappeda.infobappeda.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import id.go.jabarprov.bappeda.infobappeda.R;

public class NotificationDisplayService extends Service {
    final int NOTIFICATION_ID = 16;
    public NotificationDisplayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        displayNotification("Test Notification", "Diberitahukan dengan hormat kepada seluruh pegawai (PNS\\/Non PNS) di lingkungan BAPPEDA JABAR untuk mengikuti Upacara Peringatan Hari Lahir Pancasila pada hari jumat, 1 Juni 2018 di lapangan parkir belakang BAPPEDA JABAR pukul 07.00 dengan menggunakan Seragam KORPRI dan Peci Nasional\\/Kerudung Hitam bagi PNS dan Pakaian batik bagi NON PNS");
        return super.onStartCommand(intent, flags, startId);
    }

    private void displayNotification(String title, String text){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.chatting)
                //.setLargeIcon(BitmapFactory.decodeResource(R.drawable.xyz))
                .setColor(getResources().getColor(R.color.colorAccent))
                //.setSound()
                .setLights(Color.WHITE , 1000, 5000)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{0, 300, 300, 300})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }
}
