package com.lheidosms.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lheidosms.utils.LheidoUtils;
import com.lheidosms.utils.Message;
import com.lheidosms.app.R;

import java.util.ArrayList;

public class ConversationAdapter extends BaseAdapter {
    private final String mPhoneContact;
    /**
     * Récupérer un item de la liste en fonction de sa position
     * @param position - Position de l'item à récupérer
     * @return l'item récupéré
     */
    public LheidoUtils.UserPref userPref;
    //private LayoutInflater mInflater;
    private ArrayList<Message> mListSms;
    private Context mContext;

    public ConversationAdapter(Context context,String contactPhone, ArrayList<Message> conversation){
        mContext = context;
        mListSms = conversation;
        mPhoneContact = contactPhone;
        userPref = new LheidoUtils.UserPref();
        userPref.setUserPref(PreferenceManager.getDefaultSharedPreferences(mContext));
//mInflater = LayoutInflater.from(mContext);
    }

    public Message getItem(int position) {
        return mListSms.get(getCount() -1 - position);
    }

    /**
     * Récupérer l'identifiant d'un item de la liste en fonction de sa position (plutôt utilisé dans le cas d'une
     * base de données, mais on va l'utiliser aussi)
     * @param position - Position de l'item à récupérer
     * @return l'identifiant de l'item
     */
    public long getItemId(int position) {
        return position;
    }

    public View getView(int r, View convertView, ViewGroup parent) {
        Message message = (Message) this.getItem(r);
        ConversationViewHolder holder;
        if(convertView == null)
        {
            holder = new ConversationViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message, parent, false);
            holder.mBody = (TextView) convertView.findViewById(R.id.message);
            holder.mdate = (TextView) convertView.findViewById(R.id.date_message);
            holder.mLayout = (RelativeLayout) convertView.findViewById(R.id.message_relativeLayout);
            holder.mIsRead = (View) convertView.findViewById(R.id.is_read);
            convertView.setTag(holder);
        }
        else
            holder = (ConversationViewHolder) convertView.getTag();

        holder.mBody.setText(message.getBody());
        holder.mBody.setTextSize(userPref.text_size);
        holder.mdate.setText(message.getDate());

        //RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.mLayout.getLayoutParams();
        if(!PhoneNumberUtils.compare(mPhoneContact, message.getSender())) {

            holder.mLayout.setGravity(Gravity.RIGHT);
            holder.mLayout.setPadding(42, 0, 0, 0);
            holder.mBody.setBackgroundColor(mContext.getResources().getColor(R.color.grey_mid_high));
            if(message.isRead())
                holder.mIsRead.setBackgroundColor(mContext.getResources().getColor(R.color.read_green));
            else{
                holder.mIsRead.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
            }
        }
        else {
            holder.mLayout.setGravity(Gravity.LEFT);
            holder.mLayout.setPadding(0, 0, 42, 0);
            holder.mBody.setBackgroundColor(mContext.getResources().getColor(R.color.grey_low));
            holder.mIsRead.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }
        return convertView;
    }
    @Override
    public int getCount() {
        return mListSms.size();
    }
    class ConversationViewHolder {
        public RelativeLayout mLayout;
        public TextView mBody;
        public TextView mdate;
        public View mIsRead;
    }
}
