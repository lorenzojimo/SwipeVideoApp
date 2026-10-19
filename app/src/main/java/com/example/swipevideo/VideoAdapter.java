package com.example.swipevideo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView adapter that backs the vertical {@code ViewPager2}.
 * <p>
 * Each page shows one video's details (title, description, ID). The actual
 * playback is controlled by {@link MainActivity}, which attaches a single
 * shared player to the currently visible page.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    /**
     * Callbacks sent from the adapter to the host (the activity).
     */
    public interface VideoListener {

        /**
         * Called when the user taps a video page (used to pause / resume).
         *
         * @param holder the page that was tapped
         */
        void onVideoTapped(VideoViewHolder holder);

        /**
         * Called when a page becomes attached to the window.
         *
         * @param holder the attached page
         */
        void onHolderAttached(VideoViewHolder holder);
    }

    /** Videos displayed by this adapter. */
    private final List<VideoItem> videos;

    /** Listener notified about taps and attach events. */
    private final VideoListener listener;

    /**
     * Creates the adapter.
     *
     * @param videos   list of videos to display
     * @param listener listener for tap and attach events
     */
    public VideoAdapter(List<VideoItem> videos, VideoListener listener) {
        this.videos = videos;
        this.listener = listener;
    }

    /**
     * Gets the video at the given position.
     *
     * @param position adapter position
     * @return the {@link VideoItem} at that position
     */
    public VideoItem getItem(int position) {
        return videos.get(position);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem item = videos.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.videoId.setText(holder.itemView.getContext()
                .getString(R.string.video_id_format, item.getId()));
        holder.setPaused(false);
        holder.setLoading(true);
        holder.itemView.setOnClickListener(v -> listener.onVideoTapped(holder));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VideoViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        listener.onHolderAttached(holder);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    /**
     * Holds the views of a single video page.
     */
    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        /** Surface on which the video is rendered. */
        public final PlayerView playerView;

        /** Video title text. */
        final TextView title;

        /** Video description text. */
        final TextView description;

        /** Unique video ID text. */
        final TextView videoId;

        /** Large play icon shown while playback is paused. */
        final ImageView pauseIcon;

        /** Spinner shown while the video is buffering. */
        final ProgressBar loading;

        /**
         * Looks up all child views.
         *
         * @param itemView the inflated page layout
         */
        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.playerView);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            videoId = itemView.findViewById(R.id.tvVideoId);
            pauseIcon = itemView.findViewById(R.id.ivPause);
            loading = itemView.findViewById(R.id.progress);
        }

        /**
         * Shows or hides the paused (play) icon.
         *
         * @param paused {@code true} when playback is paused
         */
        public void setPaused(boolean paused) {
            pauseIcon.setVisibility(paused ? View.VISIBLE : View.GONE);
        }

        /**
         * Shows or hides the buffering spinner.
         *
         * @param isLoading {@code true} while buffering
         */
        public void setLoading(boolean isLoading) {
            loading.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
}
