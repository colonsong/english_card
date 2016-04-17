package waterdrop.tw.catchcard.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;

import waterdrop.tw.catchcard.receiver.ShowCardReceiver;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ScreenOnCardService extends IntentService {
    private BroadcastReceiver receiver ;
    ShowCardReceiver showCardReceiver;
    public ScreenOnCardService() {
        super("ScreenOnCardService");
        setIntentRedelivery(true);
    }




    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {


            showCardReceiver = new ShowCardReceiver(getApplication());
            showCardReceiver.registerReciever();


        }
    }

    @Override
    public void onDestroy() {
        // 移除廣播接收元件
        showCardReceiver.unRegisterReciever();
    }





}
