package anjuman.e.husami;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.CustomViewHolder> {

    Activity mActivity;
    ArrayList<NotificationPOJO> mNotificationses;

    public NotificationsAdapter(NotificationActivity notificationActivity) {
        mActivity = notificationActivity;
        mNotificationses = NotificationActivity.mNotificationses;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item_view, null);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        if (!TextUtils.isEmpty(mNotificationses.get(position).mNotificationTitle)) {
            holder.mTextViewNotificationName.setText(mNotificationses.get(position).mNotificationTitle);
            holder.mCardView.setOnClickListener(onClickListener);
            holder.mCardView.setTag(holder);
            holder.mTextViewDate.setText(mNotificationses.get(position).mNotificationDateTime);
        } else
            holder.mCardView.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mNotificationses == null ? 0 : mNotificationses.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewNotificationName, mTextViewDate;
        CardView mCardView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
            mTextViewNotificationName = (TextView) itemView.findViewById(R.id.text_notification_name);
            mTextViewDate = (TextView) itemView.findViewById(R.id.text_notification_date);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CustomViewHolder customViewHolder = (CustomViewHolder) v.getTag();
            Intent intent = new Intent(mActivity, NotificationDetailsActivity.class);
            intent.putExtra("POSITION", customViewHolder.getAdapterPosition());
            mActivity.startActivity(intent);
        }
    };
}
