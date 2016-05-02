package ninja.taskbook.business.group;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ninja.taskbook.R;
import ninja.taskbook.business.task.TaskCreatorFragment;
import ninja.taskbook.model.entity.GroupEntity;

//----------------------------------------------------------------------------------------------------
public class GroupDetailFragment extends Fragment {

    //----------------------------------------------------------------------------------------------------
    private GroupEntity mGroupEntity;

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_detail, container, false);

        // Create
        Button createTaskButton = (Button)rootView.findViewById(R.id.create_task_button);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createTask();
            }
        });

        // Load
        loadChart();

        return rootView;
    }

    //----------------------------------------------------------------------------------------------------
    private void loadChart() {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.chart_frame_layout, new GroupTaskLineFragment())
                .commit();
    }

    //----------------------------------------------------------------------------------------------------
    private void createTask() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new TaskCreatorFragment())
                .addToBackStack(null)
                .commit();
    }
}