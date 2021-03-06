package co.com.neubs.shopneubs.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import co.com.neubs.shopneubs.PedidoDetalleActivity;
import co.com.neubs.shopneubs.PrincipalActivity;
import co.com.neubs.shopneubs.ProductoDetalleActivity;
import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.adapters.PedidoDetalleAdapter;

/**
 * Created by bikerlfh on 7/19/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null){
            showNotification(remoteMessage);
        }
    }

    private void showNotification(RemoteMessage remoteMessage) {
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon((android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ? R.drawable.ic_notification_small : R.drawable.ic_notification_small_normal)
                //.setColor(getColor(R.color.colorAccent))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_large))
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setLights(getColor(R.color.colorAccent),3000,3000)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                .setAutoCancel(true)
                .setSound(defaultSound);

        Intent intent = new Intent(this,PrincipalActivity.class);
        if (remoteMessage.getData().size() > 0) {
            // OJO.. Siempre se debe enviar el click_action
            //intent.setAction(remoteMessage.getNotification().getClickAction());

            /**
             * NO SE ENVÍA LA ACCIÓN PARA INDICARLE A LA ACTIVIDIAD QUE NO FUE LLAMADA
             * CUANDO LA APLICACIÓN ESTABA EN SEGUNDO PLANO
             */
            String clickAction = remoteMessage.getNotification().getClickAction();
            if(clickAction != null && !clickAction.isEmpty()){
                switch (clickAction){
                    case ProductoDetalleActivity.ACTION_INTENT:
                        intent = new Intent(this,ProductoDetalleActivity.class);
                        break;
                    case PedidoDetalleActivity.ACTION_INTENT:
                        intent = new Intent(this,PedidoDetalleActivity.class);
                        break;
                    case PrincipalActivity.ACTION_INTENT:
                        intent = new Intent(this,PrincipalActivity.class);
                        intent.setAction(clickAction);
                }
            }
            //  Se agregan los datos como extras al intent
            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }
}
