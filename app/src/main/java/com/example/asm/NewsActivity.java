package com.example.asm;

import static com.example.asm.Interface.ServiceAPI.BASE_SERVICE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.asm.Adapter.TinTucAdapter;
import com.example.asm.Interface.ServiceAPI;
import com.example.asm.Model.Item;
import com.example.asm.Model.ListFull;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {


//    private ArrayList<String> arrTitle, arrLink;
//    private ArrayAdapter adapter;
//    ProgressBar progressBar;
    Toolbar toolbar;
    RecyclerView recyclerListNews;
    private ArrayList<Item> item = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerListNews = findViewById(R.id.recyclerListNews);

        toolbar = findViewById(R.id.toolBar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        item = new ArrayList<>();
        demoCallAPI();

        loadData();

        //ListView lstTieuDe = findViewById(R.id.listViewNews);
        //progressBar = findViewById(R.id.progressBar);

//        arrTitle = new ArrayList<>();
//        arrLink = new ArrayList<>();

        //progressBar.setVisibility(View.VISIBLE);
        //new ReadRSS().execute("https://vnexpress.net/rss/Giao-duc.rss");

//        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrTitle);
//        lstTieuDe.setAdapter(adapter);

//           lstTieuDe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(NewsActivity.this, WebviewActivity.class);
//                intent.putExtra("linkNews", arrLink.get(i));
//                startActivity(intent);
//            }
//        });

    }

    //function gọi API
    private void demoCallAPI() {

        ServiceAPI requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_SERVICE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceAPI.class);

        new CompositeDisposable().add(requestInterface.getData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void loadData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerListNews.setLayoutManager(linearLayoutManager);
        TinTucAdapter tinTucAdapter = new TinTucAdapter(this,item);
        recyclerListNews.setAdapter(tinTucAdapter);

    }

    private void handleResponse(ListFull listFull) {
        //API trả về dữ liệu thành công, thực hiện việc lấy data
        item = listFull.getChannel().getItem();
        loadData();
        //Toast.makeText(this, listFull.getChannel().getItem().get(1).getTitle() , Toast.LENGTH_SHORT).show();
    }


    private void handleError(Throwable error) {
        String a = "";
        //khi gọi API KHÔNG THÀNH CÔNG thì thực hiện xử lý ở đây
        Toast.makeText(this, "không thành công", Toast.LENGTH_SHORT).show();
    }




    //AsyncTask lấy dữ liệu từ nguồn Internet về
//    private class ReadRSS extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            StringBuilder content = new StringBuilder();
//            try {
//                URL url = new URL(strings[0]);
//                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    content.append(line);
//                }
//                bufferedReader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return content.toString();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            XMLDOMParser parser = new XMLDOMParser();
//            Document document = parser.getDocument(s);
//            //đọc tag name có tên là item và lưu trên NodeList
//            //mỗi NodeList là 1 item
//            NodeList nodeList = document.getElementsByTagName("item");
//            String title = "";
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                Element element = (Element) nodeList.item(i);
//                title = parser.getValue(element, "title");
//                arrTitle.add(title);
//                arrLink.add(parser.getValue(element, "link"));
//            }
//            adapter.notifyDataSetChanged();
//            //progressBar.setVisibility(View.GONE);
//        }
//    }

}