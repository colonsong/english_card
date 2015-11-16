package waterdrop.tw.catchcard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import recyclerview.android.com.myapplication.R;
import waterdrop.tw.catchcard.MainActivity;
import waterdrop.tw.catchcard.sql.Card;
import waterdrop.tw.catchcard.sql.CardDAO;

public class CardListViewActivity extends AppCompatActivity {
    private CardAdapter mCardAdapter;
    private CardDAO cardDAO;
    private LayoutInflater mInflater;

    private List<Integer> cardIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mInflater = LayoutInflater.from(this);








        mInflater = LayoutInflater.from(this);
        ListView cardListView = (ListView)findViewById(R.id.cardListView);
        cardDAO = new CardDAO(getApplicationContext());

        if (cardDAO.getCount() == 0) {
            cardDAO.sample();
        }
        cardIdList = cardDAO.getCardIdList();
        //Stetho.initializeWithDefaults(this);

        mCardAdapter = new CardAdapter(this, R.layout.card_lsit_view_item, cardDAO,cardIdList);
        cardListView.setAdapter(mCardAdapter);
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CardListViewActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", cardIdList.get(position));
                bundle.putString("action", "editCard");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        mCardAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class CardAdapter extends BaseAdapter {

        // 畫面資源編號
        private int resource;
        // 包裝的記事資料
        private CardDAO cardDAO;
        private List<Card> cards;
        private List<Integer> cardIdList;
        private Context mContext;


        public CardAdapter(Context context, int resource, CardDAO cardDAO , List<Integer> cardIdList) {

            this.mContext = context;
            this.cardDAO = cardDAO;
            this.resource = resource;
            this.cardIdList = cardIdList;


        }

        @Override
        public int getCount() {
            return cardDAO.getTotalCountByCardId(0);
        }

        @Override
        public List<Card> getItem(int position) {


            return cardDAO.getCard(cardIdList.get(position),0);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout itemLayout;
            // 讀取目前位置的記事物件
             cards =  getItem(position);

            if (convertView == null) {
                // 建立項目畫面元件

                 itemLayout =  (LinearLayout) mInflater.inflate(R.layout.card_lsit_view_item, parent, false);


            }
            else {
                itemLayout = (LinearLayout) convertView;
            }

            TextView itemText = (TextView) itemLayout.findViewById(R.id.cardTextView);

            Iterator<Card> iterator = cards.iterator();
            String wordStr = "";
            while (iterator.hasNext()) {
                Card card = iterator.next();

                wordStr = wordStr + "、"  + card.getWord();





            }



            itemText.setText(wordStr);



            return itemLayout;
        }




    }

    public void addCardBtn(View v)
    {
        Intent intent = new Intent();
        intent.setClass(CardListViewActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", cardDAO.getNewCardID());
        bundle.putString("action", "newCard");
        intent.putExtras(bundle);
        startActivity(intent);

    }

}



