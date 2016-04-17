package waterdrop.tw.catchcard.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import waterdrop.tw.catchcard.R;
import waterdrop.tw.catchcard.sql.Card;
import waterdrop.tw.catchcard.sql.CardDAO;

public class ShowCardReceiver extends BroadcastReceiver {
    private CardDAO cardDAO;
    private List<Integer> cardIdList;
    private List<Card> cards;
    private Context _context;

    public static boolean wasScreenOn = true;
    Context context;
    IntentFilter screenStateFilter;
    public ShowCardReceiver(Context context) {
        // 建立廣播接收元件物件
        Log.d(getClass().toString(), "start Receiver");
        this.context = context;
        screenStateFilter   = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);

    }

    public void registerReciever(){
        context.registerReceiver(this, screenStateFilter);
    }
    public void unRegisterReciever(){
        context.unregisterReceiver(this);
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // AND DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = true;
            Toast.makeText(context, " 偵測開啟螢幕 ", Toast.LENGTH_SHORT).show();
            // TODO: This method is called when the BroadcastReceiver is receiving
            String cardStr = getCardStr();
            showCardReceiver(cardStr);
        }

    }

    public void showCardReceiver(String cardWords) {
        // 取得NotificationManager物件
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 建立NotificationCompat.Builder物件
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);

        // 設定小圖示、大圖示、狀態列文字、時間、內容標題、內容訊息和內容額外資訊
        builder.setSmallIcon(R.drawable.arrow_yellow)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("單字卡提醒")
                .setContentText(cardWords)
                .setContentInfo("3");
        // 建立通知物件
        Notification notification = builder.build();
        // 更新BASIC_ID編號的通知
        nm.notify('0', builder.build());
    }

    private String getCardStr()
    {
        int total = cardDAO.getCount();
        if (cardDAO.getCount() == 0) {
            cardDAO.sample();
        }

        cardIdList = cardDAO.getCardIdList();
        Random ran = new Random();
        int ranInt = ran.nextInt(total);
        Log.d(getClass().toString(), ranInt + "");
        int cardId = cardIdList.get(ranInt);




        cards = cardDAO.getCard(cardId, 0);

        Iterator<Card> iterator = cards.iterator();
        String wordStr = "";
        while (iterator.hasNext()) {
            Card card = iterator.next();

            wordStr = wordStr + "、"  + card.getWord();




        }


        Log.d(getClass().toString(), " 文字: "  + wordStr);
        return wordStr;
    }
}

