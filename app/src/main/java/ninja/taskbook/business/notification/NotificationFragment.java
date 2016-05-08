package ninja.taskbook.business.notification;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.NotificationEntity;
import ninja.taskbook.model.entity.TaskEntity;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.network.thrift.manager.ThriftManager;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftNotification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//----------------------------------------------------------------------------------------------------
public class NotificationFragment  extends Fragment {

    //----------------------------------------------------------------------------------------------------
    private RecyclerView mRecyclerView;
    private List<NotificationEntity> mNotificationItems = new ArrayList<>();

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification, container, false);

        // Recycler Layout
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new NotificationItemDecoration(getContext()));
        mRecyclerView.setAdapter(new NotificationItemAdapter());

        // Load
        loadNotificationData();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadNotificationData() {
        DataManager.getInstance().requestNotificationItems(
                new DataManager.RequestCallback<List<NotificationEntity>>() {
                    @Override
                    public void onResult(List<NotificationEntity> result) {
                        mNotificationItems = result;
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
        );
    }

    //----------------------------------------------------------------------------------------------------
    void onNotificationItemClicked(int id) {
        new AlertDialog.Builder(getContext())
                .setMessage("你确定?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .show();
    }

    // NotificationItemHolder
    //----------------------------------------------------------------------------------------------------
    class NotificationItemHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public NotificationItemHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView)itemView.findViewById(R.id.title_text_view);
        }
    }

    // NotificationItemAdapter
    //----------------------------------------------------------------------------------------------------
    class NotificationItemAdapter extends RecyclerView.Adapter<NotificationItemHolder> {
        public NotificationItemAdapter() {

        }

        @Override
        public int getItemCount() {
            return mNotificationItems.size();
        }

        @Override
        public NotificationItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, null);
            return new NotificationItemHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationItemHolder holder, final int position) {
            View view = holder.itemView;
            holder.titleTextView.setText(mNotificationItems.get(position).notificationData);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNotificationItemClicked(mNotificationItems.get(position).notificationId);
                }
            });
        }
    }
}
