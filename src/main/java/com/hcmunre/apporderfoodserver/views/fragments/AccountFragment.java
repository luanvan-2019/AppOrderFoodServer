package com.hcmunre.apporderfoodserver.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.hcmunre.apporderfoodserver.R;

public class AccountFragment extends Fragment {
    Button btnUpdateUser;
    EditText editName,editEmail,editPhone,editAddress;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_account,container,false);
      btnUpdateUser=view.findViewById(R.id.btnupdateuser);
      editName=view.findViewById(R.id.editNameFB);
      editEmail=view.findViewById(R.id.editEmailFB);
      editPhone=view.findViewById(R.id.editPhone);
      editAddress=view.findViewById(R.id.editAddress);
      btnUpdateUser.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
                editName.setEnabled(true);
          }
      });
      return view;
    }
}
