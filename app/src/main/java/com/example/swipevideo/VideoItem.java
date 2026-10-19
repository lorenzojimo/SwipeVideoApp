package com.example.swipevideo;

/**
 * Immutable model describing a single video shown in the swipe feed.
 * <p>
 * Each video has a unique ID, a title, a short description and the
 * URL of the file hosted online.
 */
public class VideoItem {

    /** Unique identifier of the video. */
    private final String id;

    /** Human readable title of the video. */
    private final String title;

    /** Brief description of the video. */
    private final String description;

    /** Public URL where the video file is hosted. */
    private final String videoUrl;

    /**
     * Creates a new video item.
     *
     * @param id          unique video ID
     * @param title       video title
     * @param description short description
     * @param videoUrl    URL of the hosted video file
     */
    public VideoItem(String id, String title, String description, String videoUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    /**
     * Gets the unique video ID.
     *
     * @return the video ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the video title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the video description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the URL of the hosted video.
     *
     * @return the video URL
     */
    public String getVideoUrl() {
        return videoUrl;
    }
}
