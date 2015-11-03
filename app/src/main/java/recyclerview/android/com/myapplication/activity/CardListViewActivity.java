package recyclerview.android.com.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import recyclerview.android.com.myapplication.R;
import recyclerview.android.com.myapplication.sql.Card;
import recyclerview.android.com.myapplication.sql.CardDAO;

public class CardListViewActivity extends AppCompatActivity {
    private CardAdapter mCardAdapter;
    private CardDAO cardDAO;
    private LayoutInflater mInflater;
    private ListView cardList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list_view);
        mInflater = LayoutInflater.from(this);
        cardList = (ListView)findViewById(R.id.cardListView);
        cardDAO = new CardDAO(getApplicationContext());

        mCardAdapter = new CardAdapter(this, R.layout.card_lsit_view_item, cardDAO);
        cardList.setAdapter(mCardAdapter);
    }
    public class CardAdapter extends BaseAdapter {

        // 畫面資源編號
        private int resource;
        // 包裝的記事資料
        private CardDAO cardDAO;
        private List<Card> cards;
        private Context mContext;
        private int totalCount;

        public CardAdapter(Context context, int resource, CardDAO cardDAO) {

            this.mContext = context;
            this.cardDAO = cardDAO;
            this.resource = resource;
            totalCount = cardDAO.getTotalCountByDepth(0);

        }

        @Override
        public int getCount() {
            return totalCount;
        }

        @Override
        public List<Card> getItem(int position) {


            return cardDAO.getCard(position,0);
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
}



