package com.example.swipevideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Supplies the list of videos displayed by the app.
 * <p>
 * Videos are hosted online; replace the URLs below with your own hosted
 * files (GitHub release assets, Firebase Storage, a web server, etc.)
 * if you prefer.
 */
public final class VideoRepository {

    /** Utility class: no instances. */
    private VideoRepository() {
    }

    /**
     * Builds the list of videos for the feed.
     *
     * @return a new list containing every {@link VideoItem}
     */
    public static List<VideoItem> getVideos() {
        List<VideoItem> list = new ArrayList<>();
        list.add(new VideoItem("VID-001", "Big Buck Bunny",
                "A giant rabbit takes revenge on three bullying rodents.",
                "https://www.w3schools.com/html/mov_bbb.mp4"));
        list.add(new VideoItem("VID-002", "Flower Bloom",
                "A time-lapse of a flower opening in the sunlight.",
                "https://interactive-examples.mdn.mozilla.net/media/cc0-videos/flower.mp4"));
        list.add(new VideoItem("VID-003", "Bear in the Wild",
                "A bear going about its day in a calm natural setting.",
                "https://www.w3schools.com/html/movie.mp4"));
        list.add(new VideoItem("VID-004", "Jellyfish",
                "Colourful jellyfish drifting through the deep blue sea.",
                "https://test-videos.co.uk/vids/jellyfish/mp4/h264/360/Jellyfish_360_10s_1MB.mp4"));
        list.add(new VideoItem("VID-005", "Sintel",
                "A young woman searches for her lost dragon companion.",
                "https://test-videos.co.uk/vids/sintel/mp4/h264/360/Sintel_360_10s_1MB.mp4"));
        return list;
    }
}
