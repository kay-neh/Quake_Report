package com.example.quakereport.ui.overview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quakereport.data.database.Earthquake;
import com.example.quakereport.databinding.ListViewItemBinding;

import java.util.Objects;


public class OverviewAdapter extends ListAdapter<Earthquake, OverviewAdapter.OverviewAdapterViewHolder> {

    final private ListItemClickListener mOnclickListener;

    protected OverviewAdapter(ListItemClickListener mOnclickListener) {
        super(new DiffUtilImpl());
        this.mOnclickListener = mOnclickListener;
    }


    public interface ListItemClickListener {
        void onListItemClick(String eventId);
    }

     static class OverviewAdapterViewHolder extends RecyclerView.ViewHolder {
        ListViewItemBinding binding;

        OverviewAdapterViewHolder(@NonNull ListViewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Earthquake earthquake){
            binding.setEarthquake(earthquake);
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
        Earthquake earthquake = getItem(position);
        holder.bind(earthquake);
        holder.itemView.setOnClickListener(v -> mOnclickListener.onListItemClick(earthquake.getEventId()));

    }
}

class DiffUtilImpl extends DiffUtil.ItemCallback<Earthquake>{

    @Override
    public boolean areItemsTheSame(@NonNull Earthquake oldItem, @NonNull Earthquake newItem) {
        return Objects.equals(oldItem.getEventId(), newItem.getEventId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Earthquake oldItem, @NonNull Earthquake newItem) {
        return Objects.equals(oldItem, newItem);
    }
}
