package ninja.taskbook.business.task;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.thrift.TException;

import ninja.taskbook.R;
import ninja.taskbook.model.data.DataManager;
import ninja.taskbook.model.entity.UserEntity;
import ninja.taskbook.model.network.thrift.manager.ThriftManager;
import ninja.taskbook.model.network.thrift.service.TaskBookService;
import ninja.taskbook.model.network.thrift.service.ThriftGroupInfo;
import ninja.taskbook.model.network.thrift.service.ThriftTaskInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

//----------------------------------------------------------------------------------------------------
public class TaskCreatorFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    public TaskCreatorFragment() {

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_creator, container, false);


        return rootView;
    }

    //----------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------------
    private void createTask() {
        UserEntity entity = DataManager.getInstance().getUserInfo();
        if (entity == null) {
            // Error
            return;
        }

        Observable.just(entity.userId)
                .map(new Func1<Integer, ThriftTaskInfo>() {
                    @Override
                    public ThriftTaskInfo call(Integer userId) {
                        try {
                            TaskBookService.Client client = (TaskBookService.Client) ThriftManager.createClient(ThriftManager.ClientTypeEnum.CLIENT.toString());
                            if (client != null) {
                                ThriftTaskInfo info = new ThriftTaskInfo(0, 0, "task name", "task name", "task name", "task name", 0.5);
                                return client.createTask(userId, info);
                            }
                        } catch (TException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThriftTaskInfo>() {
                    @Override
                    public void call(ThriftTaskInfo result) {
                        Toast toast = Toast.makeText(getActivity(), "创建成功", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
    }
}
