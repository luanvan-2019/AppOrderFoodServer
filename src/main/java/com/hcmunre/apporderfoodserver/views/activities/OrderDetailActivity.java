package com.hcmunre.apporderfoodserver.views.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hcmunre.apporderfoodserver.R;
import com.hcmunre.apporderfoodserver.commons.Common;
import com.hcmunre.apporderfoodserver.models.Database.OrderData;
import com.hcmunre.apporderfoodserver.models.Database.ShipperData;
import com.hcmunre.apporderfoodserver.models.Entity.Order;
import com.hcmunre.apporderfoodserver.models.Entity.OrderDetail;
import com.hcmunre.apporderfoodserver.models.Entity.Shipper;
import com.hcmunre.apporderfoodserver.models.Entity.ShipperOrder;
import com.hcmunre.apporderfoodserver.models.Entity.Status;
import com.hcmunre.apporderfoodserver.notifications.MySingleton;
import com.hcmunre.apporderfoodserver.views.adapters.OrderDetailAdapter;
import com.hcmunre.apporderfoodserver.views.adapters.PdfPrintDocumentAdapter;
import com.hcmunre.apporderfoodserver.views.adapters.SpinnerAdapter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailActivity extends AppCompatActivity {
    @BindView(R.id.txt_phone)
    TextView txt_phone;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.txt_confirm)
    TextView txt_confirm;
    @BindView(R.id.image_print)
    ImageView image_print;
    @BindView(R.id.recyc_order_detail)
    RecyclerView recyc_order_detail;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image_phone_call)
    ImageView image_phone_call;
    @BindView(R.id.spinner_shipper)
    Spinner spinner_shipper;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    OrderData orderData = new OrderData();
    ArrayList<OrderDetail> orderDetailList=new ArrayList<>();
    private String filename="order_.pdf";
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy hh:mm");
    private ArrayList<Shipper> shippers;
    private SpinnerAdapter spinnerAdapter;
    ShipperData shipperData=new ShipperData();
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAABxgzzk4:APA91bFOUq0T_vGnwemLQfJcU6akuV1gLQVJdL5mxyxV1m1bDeDbapGb8mWH0gKqSL2tSyuS_A7kTD3iWTfeFK0NhHNhcu8TY7Z7ClSu8LA2xJSJoDaYhbOge7MUF1J8V6FSRiUeDW8i";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
        eventClick();
        new getShipperForOrder().execute();
        getOrderDetail();
    }

    public class getShipperForOrder extends AsyncTask<String,String,ArrayList<Shipper>> {
        @Override
        protected void onPostExecute(ArrayList<Shipper> shippers) {
            spinnerAdapter=new SpinnerAdapter(OrderDetailActivity.this,shippers,spinner_shipper);
            spinner_shipper.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<Shipper> doInBackground(String... strings) {
            ArrayList<Shipper> shippers;
            shippers=shipperData.getAllShipper(PreferenceUtilsServer.getUserId(OrderDetailActivity.this));
            return shippers;
        }
    }
    private void init() {
        ButterKnife.bind(this);
        toolbar.setTitle(Common.currentOrder.getOrderName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setSubtitle("Id#"+Common.currentOrder.getOrderId());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyc_order_detail.setLayoutManager(linearLayoutManager);
        recyc_order_detail.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));

    }
    private void eventClick() {
        image_print.setOnClickListener(v -> printOrder());
        image_phone_call.setOnClickListener(view -> {
            AlertDialog.Builder builder=new AlertDialog.Builder(OrderDetailActivity.this);
            View custom_view=getLayoutInflater().inflate(R.layout.custom_dialog,null);
            builder.setView(custom_view);
            AlertDialog dialog=builder.create();
            dialog.show();
            TextView txt_calling=dialog.findViewById(R.id.txt_calling);
            TextView txt_cancel=dialog.findViewById(R.id.txt_cancel);
            TextView txt_name_customer=dialog.findViewById(R.id.txt_name_customer);
            txt_name_customer.setText(Common.currentOrder.getOrderName());
            txt_calling.setOnClickListener(view1 -> {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+Common.currentOrder.getOrderPhone()));
                if(ActivityCompat.checkSelfPermission(OrderDetailActivity.this,Manifest.permission.CALL_PHONE)
                        !=PackageManager.PERMISSION_GRANTED){
                    return;
                }else {
                    startActivity(intent);
                }
            });
            txt_confirm.setOnClickListener(v -> {
                new shipper().execute();
            });
            txt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        });
    }


    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void getOrderDetail() {
        Observable<ArrayList<OrderDetail>> listOrder = Observable.just(orderData.getOrderDetail(Common.currentOrder.getOrderId()));
        compositeDisposable.add(
                listOrder
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(orderDetails -> {
                            orderDetailList=orderDetails;
                            OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(this, orderDetailList);
                            recyc_order_detail.setAdapter(orderDetailAdapter);
                            orderDetailAdapter.notifyDataSetChanged();
                        }, throwable -> {
                            Toast.makeText(this, "Lỗi " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
        );
    }
    private void printOrder(){
        creatPDFFile(Common.getAppPath(OrderDetailActivity.this)+filename);
    }

    private void creatPDFFile(String path) {
        if(new File(path).exists()){
            new File(path).delete();
        }
        try {
            Document document=new Document();
            PdfWriter.getInstance(document,new FileOutputStream(path));
            document.open();
            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Sài Gòn Food");
            document.addCreator("Admin");
            //font setting
            BaseColor baseColor=new BaseColor(0,153,204,255);
            float headingFontSize=25.0f;
            float fontSize=20.0f;
            BaseFont baseFont=BaseFont.createFont("assets/fonts/Lato-Light.ttf",BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
            Font orderTitleFont=new Font(baseFont,36,Font.NORMAL,BaseColor.BLACK);
            Font orderItemFont=new Font(baseFont,20,Font.NORMAL,BaseColor.BLACK);
            Font orderItemFontBold=new Font(baseFont,25,Font.BOLD,BaseColor.BLACK);
            Font orderIdFont=new Font(baseFont,20,Font.NORMAL,baseColor);
            addNewItemLeftWithRightText(document,String.valueOf(Common.currentOrder.getOrderId()),simpleDateFormat.format(Common.currentOrder.getOrderDate())
            ,orderItemFont,orderIdFont);
            addNewItem(document,PreferenceUtilsServer.getRestaurantName(this), Element.ALIGN_CENTER,orderTitleFont);
            addLineSeparator(document);
            addNewItem(document,Common.currentOrder.getOrderName(),Element.ALIGN_CENTER,orderItemFontBold);
            addNewItem(document,Common.currentOrder.getOrderAddress(),Element.ALIGN_CENTER,orderItemFontBold);
            addNewItem(document,Common.currentOrder.getOrderPhone(),Element.ALIGN_CENTER,orderItemFontBold);
            addLineSeparator(document);
            //cartItem
            for(OrderDetail item:orderDetailList){
                addNewItemLeftWithRightText(document,new StringBuilder("")
                .append(item.getQuantity())
                .append("x")
                .append(item.getName()).toString(),new StringBuilder("")
                .append(item.getPrice())
                .append("đ").toString(),orderItemFont,orderIdFont);
                addLineSeparator(document);
            }
            document.close();
            printPDF();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        PrintManager printManager=(PrintManager)getSystemService(Context.PRINT_SERVICE);
        try {
            PrintDocumentAdapter printDocumentAdapter=new PdfPrintDocumentAdapter(
                    OrderDetailActivity.this,Common.getAppPath(OrderDetailActivity.this)+filename
            );
            printManager.print("Document",printDocumentAdapter
            ,new PrintAttributes.Builder().build());
        }catch (Exception ex){
            Log.e(Common.TAG,""+ex.getMessage());
        }

    }

    private void addLineSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator=new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk=new Chunk(text,font);
        Paragraph p=new Paragraph(chunk);
        p.setAlignment(align);
        document.add(p);

    }

    private void addNewItemLeftWithRightText(Document document, String leftText, String rightText, Font leftFont, Font rightFont) throws DocumentException {
        Chunk chunkLeft=new Chunk(leftText,leftFont);
        Chunk chunkRight=new Chunk(rightText,rightFont);
        Paragraph p=new Paragraph(chunkLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkRight);
        document.add(p);
    }

    public class deliveryForShipper extends AsyncTask<String,String,Boolean>{
        private int orderStatus;
        public deliveryForShipper(int orderStatus) {
            this.orderStatus = orderStatus;
            this.execute();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean==true){
                new shipper().execute();
            }
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            Order order=new Order();
            order.setOrderId(Common.currentOrder.getOrderId());
            order.setOrderStatus(orderStatus);
            boolean success=orderData.updateOrder(order);
            return success;
        }
    }
    public class shipper extends AsyncTask<String,String,Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(OrderDetailActivity.this);
            progressDialog.setMessage("Vui lòng chờ...");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean==true){
                Toast.makeText(OrderDetailActivity.this, "Đã giao cho shipper", Toast.LENGTH_SHORT).show();
                NOTIFICATION_TITLE = "Giao hàng";
                NOTIFICATION_MESSAGE ="Bạn có đơn hàng mới "+Common.currentOrder.getOrderId()+" ?";

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", Common.createTopicSender(Common.getTopicChannelShippper(Common.currentShiper.getId())));
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }
                sendNotification(notification);
                progressDialog.dismiss();
            }else {
                Toast.makeText(OrderDetailActivity.this, "chưa giao", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            ShipperOrder shipperOrder=new ShipperOrder();
            shipperOrder.setOrderID(Common.currentOrder.getOrderId());
            shipperOrder.setShipperId(Common.currentShiper.getId());
            shipperOrder.setRestaurantId(PreferenceUtilsServer.getUserId(OrderDetailActivity.this));
            shipperOrder.setShippingStatus(1);
            boolean success=orderData.shippingOrder(shipperOrder);
            return success;
        }
    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderDetailActivity.this, "Yêu cầu lỗi", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Lỗi");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
