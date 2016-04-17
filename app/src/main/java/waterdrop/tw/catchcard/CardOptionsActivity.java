package waterdrop.tw.catchcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

/**
 * Created by colon on 2015/10/30.
 */
public class CardOptionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 取消元件的應用程式標題
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_card_options);
    }


    // 點擊確定與取消按鈕都會呼叫這個方法
    public void action(View view) {
        // 確定按鈕
        if (view.getId() == R.id.addCardBtn) {

            // 取得回傳資料用的Intent物件
            Intent result = getIntent();
            // 設定標題與內容
            result.putExtra("action", "add");

// 設定回應結果為確定
            setResult(Activity.RESULT_OK, result);

        }
        else if(view.getId() == R.id.addCardItem)
        {
            // 取得回傳資料用的Intent物件
            Intent result = getIntent();
            // 設定標題與內容
            result.putExtra("action", "addCardItem");

// 設定回應結果為確定
            setResult(Activity.RESULT_OK, result);
        }
        else
        {
            // 取得回傳資料用的Intent物件
            Intent result = getIntent();
            // 設定標題與內容
            result.putExtra("action", "del");
// 設定回應結果為確定
            setResult(Activity.RESULT_OK, result);


        }


        // 結束
        finish();
    }


}
