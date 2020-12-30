package com.cmcc.smsposterpro.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmcc.smsposterpro.R;
import com.cmcc.smsposterpro.adapter.ContactSelectedAdapter;
import com.cmcc.smsposterpro.adapter.decoration.ContactDecoration;
import com.cmcc.smsposterpro.bean.Contact;
import com.cmcc.smsposterpro.confing.Constant;
import com.cmcc.smsposterpro.utils.db.DataBaseManager;

import java.util.ArrayList;

public class SelectedContactActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mSelcetedContactList;
    private Button mAddContactButton, mAddOneContact;
    private ContactSelectedAdapter mContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_contact);
        initActionbar();

        mSelcetedContactList = (RecyclerView) findViewById(R.id.list_selected_contact);
        mAddContactButton = (Button) findViewById(R.id.button_add_contact);
        mAddOneContact = (Button) findViewById(R.id.button_add_one);

        mAddContactButton.setOnClickListener(this);
        mAddOneContact.setOnClickListener(this);

        initRecyclerView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(intent.getBooleanExtra(Constant.EXTRA_DATA_CHANGE,false)){
            mContactAdapter.notifyUpdata(getContactList());
        }
        super.onNewIntent(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionbar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 初始化RecyclerView
     * 从保存被选中联系人的数据库中拿出所有数据，并填充到RecyclerView
     */
    private void initRecyclerView() {
        mSelcetedContactList.addItemDecoration(new ContactDecoration());
        mSelcetedContactList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ArrayList<Contact> selectedList = getContactList();
        mContactAdapter = new ContactSelectedAdapter(this, selectedList);
        mSelcetedContactList.setAdapter(mContactAdapter);

    }

    private ArrayList<Contact> getContactList() {
        DataBaseManager dataBaseManager = new DataBaseManager(this);
        ArrayList<Contact> selectedList = dataBaseManager.getAllContact();
        dataBaseManager.closeHelper();
        return selectedList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_contact:
                startActivity(new Intent(this, ContactListActivity.class));
                break;
            case R.id.button_add_one:
                showAddOneDialog();
                break;
        }
    }


    private void showAddOneDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_email_account, null, false);
        final EditText textName = (EditText) view.findViewById(R.id.editText_account);
        final EditText textNum = (EditText) view.findViewById(R.id.editText_password);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);

        textName.setHint("名称");
        textNum.setHint("电话号");
        title.setText("请输入名称和手机号");
        textNum.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = textName.getText().toString();
                String num = textNum.getText().toString();
                if (name.length() != 0 && num.length() != 0) {
                    DataBaseManager dataBaseManager = new DataBaseManager(SelectedContactActivity.this);
                    Contact contact = new Contact(name,num);
                    dataBaseManager.addContact(contact);
                    dataBaseManager.closeHelper();
                    mContactAdapter.notifyUpdata(getContactList());
                }else{
                    Toast.makeText(SelectedContactActivity.this,"名称或手机号不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.show();

    }
}
