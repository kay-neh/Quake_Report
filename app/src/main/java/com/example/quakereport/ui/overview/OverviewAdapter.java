package com.example.quakereport.ui.overview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quakereport.databinding.ListViewItemBinding;

import java.util.Objects;


public class OverviewAdapter extends ListAdapter<OverviewUIState, OverviewAdapter.OverviewAdapterViewHolder> {

    final private ListItemClickListener mOnclickListener;

    protected OverviewAdapter(ListItemClickListener mOnclickListener) {
        super(new DiffUtilImpl());
        this.mOnclickListener = mOnclickListener;
    }


    public interface ListItemClickListener {
        void onListItemClick(View view, String eventId);
    }

     static class OverviewAdapterViewHolder extends RecyclerView.ViewHolder {
        ListViewItemBinding binding;

        OverviewAdapterViewHolder(@NonNull ListViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(OverviewUIState overviewUIState){
            binding.setOverviewuistate(overviewUIState);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public OverviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OverviewAdapterViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OverviewAdapterViewHolder holder, int position) {
        OverviewUIState overviewUIState = getItem(position);
        holder.bind(overviewUIState);
        holder.itemView.setOnClickListener(view -> mOnclickListener.onListItemClick(view,overviewUIState.getEventId()));

    }
}

class DiffUtilImpl extends DiffUtil.ItemCallback<OverviewUIState>{

    @Override
    public boolean areItemsTheSame(@NonNull OverviewUIState oldItem, @NonNull OverviewUIState newItem) {
        return Objects.equals(oldItem.getEventId(), newItem.getEventId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull OverviewUIState oldItem, @NonNull OverviewUIState newItem) {
        return Objects.equals(oldItem, newItem);
    }
}
