package com.example.swipevideo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

/**
 * Main (and only) screen of the app.
 * <p>
 * Shows a vertically swipeable list of videos using {@link ViewPager2}.
 * A single {@link ExoPlayer} instance is shared and re-attached to
 * whichever page is currently visible, which keeps memory use low and
 * guarantees that only one video plays at a time.
 */
public class MainActivity extends AppCompatActivity implements VideoAdapter.VideoListener {

    /** Pager that lets the user swipe up / down between videos. */
    private ViewPager2 viewPager;

    /** Adapter supplying the video pages. */
    private VideoAdapter adapter;

    /** Shared player instance; {@code null} while the activity is stopped. */
    private ExoPlayer player;

    /** Page the player is currently attached to. */
    private VideoAdapter.VideoViewHolder boundHolder;

    /** Adapter position of {@link #boundHolder}, or -1 if none. */
    private int boundPosition = -1;

    /** Position of the page currently visible to the user. */
    private int currentPosition = 0;

    /**
     * Sets up the layout, the adapter and the vertical pager.
     *
     * @param savedInstanceState previously saved state, if any
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new VideoAdapter(VideoRepository.getVideos(), this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            /**
             * Starts the newly visible video.
             *
             * @param position index of the selected page
             */
            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                RecyclerView rv = (RecyclerView) viewPager.getChildAt(0);
                RecyclerView.ViewHolder vh = rv.findViewHolderForAdapterPosition(position);
                if (vh instanceof VideoAdapter.VideoViewHolder) {
                    bindPlayer((VideoAdapter.VideoViewHolder) vh, position);
                }
            }
        });
    }

    /** Creates the player when the activity becomes visible. */
    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
        // Attach to the visible page once layout has finished.
        viewPager.post(() -> {
            RecyclerView rv = (RecyclerView) viewPager.getChildAt(0);
            RecyclerView.ViewHolder vh = rv.findViewHolderForAdapterPosition(currentPosition);
            if (vh instanceof VideoAdapter.VideoViewHolder) {
                bindPlayer((VideoAdapter.VideoViewHolder) vh, currentPosition);
            }
        });
    }

    /** Releases the player when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    /**
     * Creates the shared {@link ExoPlayer} and registers a state listener
     * that drives the buffering spinner and the pause icon.
     */
    private void initializePlayer() {
        if (player != null) {
            return;
        }
        player = new ExoPlayer.Builder(this).build();
        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        player.addListener(new Player.Listener() {
            /**
             * Updates the spinner according to the buffering state.
             *
             * @param state one of the {@code Player.STATE_*} constants
             */
            @Override
            public void onPlaybackStateChanged(int state) {
                if (boundHolder != null) {
                    boundHolder.setLoading(state == Player.STATE_BUFFERING);
                }
            }

            /**
             * Shows the play icon while the user has paused playback.
             *
             * @param playWhenReady   whether playback should run
             * @param reason          reason for the change
             */
            @Override
            public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
                if (boundHolder != null) {
                    boundHolder.setPaused(!playWhenReady);
                }
            }
            /**
             * Shows the playback error so problems are easy to diagnose.
             *
             * @param error the error reported by ExoPlayer
             */
            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                android.widget.Toast.makeText(MainActivity.this,
                        "Playback error: " + error.getErrorCodeName(),
                        android.widget.Toast.LENGTH_LONG).show();
                if (boundHolder != null) {
                    boundHolder.setLoading(false);
                }
            }
        });
    }

    /** Detaches the player from its page and frees its resources. */
    private void releasePlayer() {
        if (boundHolder != null) {
            boundHolder.playerView.setPlayer(null);
        }
        boundHolder = null;
        boundPosition = -1;
        if (player != null) {
            player.release();
            player = null;
        }
    }

    /**
     * Attaches the shared player to the given page and starts playback.
     * Does nothing if the player is already attached to that page.
     *
     * @param holder   the page to play on
     * @param position adapter position of the page
     */
    private void bindPlayer(VideoAdapter.VideoViewHolder holder, int position) {
        if (player == null || (boundHolder == holder && boundPosition == position)) {
            return;
        }
        if (boundHolder != null) {
            boundHolder.playerView.setPlayer(null);
            boundHolder.setPaused(false);
        }
        boundHolder = holder;
        boundPosition = position;
        holder.setPaused(false);
        holder.playerView.setPlayer(player);
        player.setMediaItem(MediaItem.fromUri(adapter.getItem(position).getVideoUrl()));
        player.prepare();
        player.setPlayWhenReady(true);
    }

    /**
     * Toggles play / pause when the visible page is tapped.
     *
     * @param holder the page that was tapped
     */
    @Override
    public void onVideoTapped(VideoAdapter.VideoViewHolder holder) {
        if (player != null && holder == boundHolder) {
            player.setPlayWhenReady(!player.getPlayWhenReady());
        }
    }

    /**
     * Starts playback if the newly attached page is the visible one
     * (covers the very first page shown at launch).
     *
     * @param holder the page that was attached
     */
    @Override
    public void onHolderAttached(@NonNull VideoAdapter.VideoViewHolder holder) {
        int pos = holder.getBindingAdapterPosition();
        if (pos == currentPosition && pos != RecyclerView.NO_POSITION) {
            bindPlayer(holder, pos);
        }
    }
}
