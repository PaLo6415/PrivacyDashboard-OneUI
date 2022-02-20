package rk.android.app.privacydashboard.fragment.permission.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import de.dlyt.yanndroid.oneui.view.RecyclerView;
import rk.android.app.privacydashboard.R;

public class PermissionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;

    public PermissionListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(context);

        /*switch (viewType) {
            case 0:
                return null;
            case 1:
                PermissionItemViewModel view = DataBindingUtil.inflate(inflater, R.layout.oui_view_permission_tab_item, parent, false);
                //return inflater.inflate(R.layout.oui_view_permission_tab_item, parent, false);
        }*/

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return 0;
    }





    /*class ItemViewHolder extends BaseViewHolder {
        private AbstractC1788s f7097v;

        public ItemViewHolder(AbstractC1788s sVar) {
            super(sVar.m4784u());
            f7097v = sVar;
        }

        private String m36N(C1872e eVar) {
            if (eVar.f7100c.size() == 1) {
                return eVar.f7104g;
            }
            if (eVar.f7100c.size() != 2) {
                return C1867t.this.f7089e.getResources().getQuantityString(2131689472, eVar.f7100c.size() - 1, eVar.f7104g, Integer.valueOf(eVar.f7100c.size() - 1));
            }
            return eVar.f7100c.get(1) + ", " + eVar.f7100c.get(0);
        }

        private void m34P(C1872e eVar, View view) {
            C1867t.this.f7092h.mo38a(eVar.f7102e);
        }

        public void m37M(C1872e eVar) {
            this.f7097v.m4784u().setOnClickListener(new View$OnClickListenerC1835i(this, eVar));
            this.f7097v.f6814B.setText(eVar.f7101d);
            this.f7097v.f6816y.setText(m36N(eVar));
            this.f7097v.f6813A.setImageResource(eVar.f7103f);
            this.f7097v.f6815C.setText(C1797a.m267c(C1867t.this.f7089e, eVar.f7105h));
        }
    }*/
}
