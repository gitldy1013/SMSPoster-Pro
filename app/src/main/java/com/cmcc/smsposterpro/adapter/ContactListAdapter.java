package com.cmcc.smsposterpro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.cmcc.smsposterpro.R;
import com.cmcc.smsposterpro.adapter.holder.ContactHolder;
import com.cmcc.smsposterpro.bean.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WHF on 2017/3/28.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactHolder> {

    private LayoutInflater mInflater;
    private List<Contact> mContactList;
    private ArrayList<Contact> mSelectedList = null;

    public ContactListAdapter(Context context, List<Contact> contactList) {
        this.mInflater = LayoutInflater.from(context);
        this.mContactList = contactList;
        this.mSelectedList = new ArrayList<>();
    }

    private View getItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.item_contact,parent,false);
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getItemView(mInflater,parent);
        ContactHolder holder = new ContactHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        final Contact contact = mContactList.get(position);
        holder.mContactName.setText(contact.getContactName());
        holder.mContactNum.setText(contact.getContactNum());
        if(contact.getSelected()==1){
            holder.mRightIcon.setImageResource(R.mipmap.ic_selected);
        }else {
            holder.mRightIcon.setImageResource(R.mipmap.ic_unselected);
        }
        holder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contact.getSelected()==1){
                    contact.setSelected(0);
                    mSelectedList.remove(contact);
                }else{
                    contact.setSelected(1);
                    mSelectedList.add(contact);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public ArrayList<Contact> getSelectedList(){
        return mSelectedList;
    }

}
