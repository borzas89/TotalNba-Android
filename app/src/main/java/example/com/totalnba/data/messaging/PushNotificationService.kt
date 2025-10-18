package example.com.totalnba.data.messaging

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import example.com.totalnba.R

class PushNotificationService : FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification!!.title
        val text = remoteMessage.notification!!.body
        val CHANNEL_ID = "TOTALNBA_NOTIFICATION"
        val channel = NotificationChannel(
            CHANNEL_ID,
            "TotalNBA Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notification: Notification.Builder = Notification.Builder(
            this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.splash_icon)
            .setAutoCancel(true)

        // Check for notification permission before showing notification
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(1, notification.build())
        }
        super.onMessageReceived(remoteMessage)
    }
}