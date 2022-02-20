package rk.android.app.privacydashboard.fragment.permission;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.dlyt.yanndroid.oneui.sesl.recyclerview.LinearLayoutManager;
import rk.android.app.privacydashboard.databinding.FragmentPermissionTabBinding;
import rk.android.app.privacydashboard.fragment.permission.recyclerview.DividerDecoration;
import rk.android.app.privacydashboard.fragment.permission.recyclerview.PermissionListAdapter;
import rk.android.app.privacydashboard.fragment.permission.recyclerview.RoundedCornerDecoration;

public class PermissionListTabFragment extends Fragment {
    private Context context;
    private FragmentPermissionTabBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPermissionTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (binding != null) {
            binding.tabRecyclerView.setAdapter(new PermissionListAdapter(context));
            binding.tabRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            binding.tabRecyclerView.seslSetFillBottomEnabled(true);

            DividerDecoration dividerDecoration = new DividerDecoration(context);
            dividerDecoration.setDividerPadding(10, 10);
            binding.tabRecyclerView.addItemDecoration(dividerDecoration);
            binding.tabRecyclerView.addItemDecoration(new RoundedCornerDecoration(context));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
