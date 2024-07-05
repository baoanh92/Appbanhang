package com.manager.appbanhang.retrofit;

import android.os.Message;

import com.manager.appbanhang.model.DoanhThuNgay;
import com.manager.appbanhang.model.DonHangModel;
import com.manager.appbanhang.model.AccountModel;
import com.manager.appbanhang.model.LienHeModel;
import com.manager.appbanhang.model.LoaiSpModel;
import com.manager.appbanhang.model.MessageModel;
import com.manager.appbanhang.model.SanPhamMoiModel;
import com.manager.appbanhang.model.ThongKeModel;
import com.manager.appbanhang.model.ThongKeNgayModel;
import com.manager.appbanhang.model.UserModel;

import java.util.Date;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();
    @GET("getloaispsell.php")
    Observable<LoaiSpModel> getLoaiSpSell();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
    @GET("getsoluong.php")
    Observable<SanPhamMoiModel> getSpMoiById(@Query("id") int id);

    @GET("getallhotro.php")
    Observable<LienHeModel> gethotro(@Query("user") String user);
    @GET("gethotro.php")
    Observable<LienHeModel> gethotroAll();
    @GET("getkhachhang.php")
    Observable<AccountModel> getKhachHang();
    @GET("getadmin.php")
    Observable<AccountModel> getAdmin();
    @GET("thongke.php")
    Observable<ThongKeModel> getthongke();
    @POST("updateghichuandtrangthai.php")
    @FormUrlEncoded
    Observable<MessageModel> updateNoteandDate(
            @Field("id") int id,
            @Field("NgayGiao") String ngaygiao
    );
    @POST("updatenote.php")
    @FormUrlEncoded
    Observable<MessageModel> updateNote(
            @Field("id") int id,
            @Field("GhiChu") String ghichu
    );
    @GET("thongkethang.php")
    Observable<ThongKeModel> getthongkeThang();
    @GET("sanphamsell.php")
    Observable<ThongKeNgayModel> getSanPhamSellByDate(@Query("date") String date);
    @GET("doanhthungay.php")
    Observable<DoanhThuNgay> getThongKeNgay();
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("address") String address
    );
    @POST("dangkiadmin.php")
    @FormUrlEncoded
    Observable<UserModel> dangkiadmin(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("decentralization") String decentralization
    );
    @POST("dangnhapadmin.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhapadmin(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("hotennguoinhan") String hotennguoinhan,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );

    @POST("Order_detail.php")
    @FormUrlEncoded
    Observable<MessageModel> createOder_detail(
            @Field("product_id") int product_id,
            @Field("quantity") int quantity
    );

    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") int id
    );

    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );
    @POST("timkiemdt.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> searchDt(
            @Field("search") String search
    );
    @POST("timkiemkhachhang.php")
    @FormUrlEncoded
    Observable<AccountModel> searchKhachHang(
            @Field("search") String search
    );

    @POST("timkiemthanhvien.php")
    @FormUrlEncoded
    Observable<AccountModel> searchThanhVien(
            @Field("search") String search
    );

    @POST("timkiemlt.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> searchLt(
            @Field("search") String search
    );
    @POST("timkiemsp.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> searchSp(
            @Field("search") String search,
            @Field("loai") int loai
    );
    @POST("xoa.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSanPham(
            @Field("id") int id
    );
    @POST("xoakhachhang.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaKhachHang(
            @Field("id") int id
    );
    @POST("xoadanhmuc.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaDanhMuc(
            @Field("id") int id
    );
    @POST("xoaadmin.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaAdmin(
            @Field("id") int id
    );

    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai,
            @Field("Quantity") int Quantity
    );
    @POST("themsanpham.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSanPham(
            @Field("tensp") String tensp,
            @Field("hinhanh") String hinhanh
    );
    @POST("updatesp.php")
    @FormUrlEncoded
    Observable<MessageModel> updatesp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai,
            @Field("id") int id
    );

    @POST("updateorder.php")
    @FormUrlEncoded
    Observable<MessageModel> updateOrder(
            @Field("id") int id,
            @Field("trangthai") int trangthai
    );
    @POST("updateKhachHang.php")
    @FormUrlEncoded
    Observable<MessageModel> updateKhachHang(
            @Field("email") String email,
            @Field("phone") String mobile,
            @Field("address") String address,
            @Field("id") int id
    );
    @POST("updateadmin.php")
    @FormUrlEncoded
    Observable<MessageModel> updateAdmin(
            @Field("email") String email,
            @Field("phone") String mobile,
            @Field("address") String address,
            @Field("id") int id,
            @Field("dc") String dc
    );

    @POST("updatecategory.php")
    @FormUrlEncoded
    Observable<MessageModel> updateCategory(
            @Field("id") int id,
            @Field("name") String name,
            @Field("hinhanh") String hinhanh
    );
    @POST("addcategory.php")
    @FormUrlEncoded
    Observable<MessageModel> addCatogory(
            @Field("name") String name,
            @Field("hinhanh") String hinhanh
    );

    @POST("phanhoi.php")
    @FormUrlEncoded
    Observable<LienHeModel> themPhanHoi(
            @Field("user") String user,
            @Field("mess") String mess,
            @Field("date") String date
    );
    @POST("traloi.php")
    @FormUrlEncoded
    Observable<LienHeModel> themTraloi(
            @Field("id") int id,
            @Field("mess") String mess
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);


}

