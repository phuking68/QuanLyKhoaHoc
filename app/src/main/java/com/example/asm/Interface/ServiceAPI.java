package com.example.asm.Interface;

import com.example.asm.Model.ListFull;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ServiceAPI {
    String BASE_SERVICE = "https://apis.dinhnt.com/";

    //http method:phương thức gọi được lên API
    @GET("edu.json")
    //giá trị mong muốn trả về
    Observable<ListFull> getData();


}
