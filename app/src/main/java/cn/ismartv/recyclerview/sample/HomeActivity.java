package cn.ismartv.recyclerview.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ismartv.recyclerview.sample.core.IsmartvClient;
import cn.ismartv.recyclerview.widget.GridLayoutManager;
import cn.ismartv.recyclerview.widget.RecyclerView;

public class HomeActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "HomeActivity";


    private RecyclerView recyclerView;
    private List<HttpDataEntity.Object> datas;
    private HomeAdapter adapter;

    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);


        recyclerView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d(TAG, "keycode is : " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_DOWN:
                            recyclerView.smoothScrollToPosition(45);
                            break;

                    }

                    return true;
                }
                return false;
            }
        });
        recyclerView.setHasFixedSize(true);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this, StaggeredGridLayoutManager.HORIZONTAL, false);
//        gridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(gridLayoutManager);

//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayoutManager.HORIZONTAL));
//        recyclerView.setLayoutManager(new GridLayoutManager());


        new IsmartvClient(AppConstant.TEST_API, new IsmartvClient.CallBack() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "result is: " + result);
                initData(new Gson().fromJson(result, HttpDataEntity.class).getObjects());
                recyclerView.setAdapter(adapter = new HomeAdapter());
            }

            @Override
            public void onFailed(Exception exception) {
                Log.e(TAG, exception.getMessage());
            }
        }).start();


//        recyclerView.scrollToPosition();


    }

    protected void initData(List<HttpDataEntity.Object> objects) {
        datas = objects;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                recyclerView.smoothScrollBy(recyclerView.getWidth(), 0);
//                recyclerView.smoothScrollToPosition(40);
//                recyclerView.getChildAt(40).requestFocusFromTouch();
                break;

        }
    }


    class HomeAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(HomeActivity.this).inflate(R.layout.item_recycler, viewGroup, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, int postion) {
            myViewHolder.textView.setText(datas.get(postion).getTitle() + "   " + postion);
            Picasso.with(HomeActivity.this).load(datas.get(postion).getImage()).into(myViewHolder.imageView);
            if (postion == 0){
                myViewHolder.itemView.requestFocus();
                myViewHolder.itemView.requestFocusFromTouch();
            }


        }


        @Override
        public int getItemCount() {
            return datas.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView textView;
        private ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            textView = (TextView) itemView.findViewById(R.id.id_number);
            imageView = (ImageView) itemView.findViewById(R.id.image);

        }
    }


}
