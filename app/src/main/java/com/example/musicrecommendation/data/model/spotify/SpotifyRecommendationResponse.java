package com.example.musicrecommendation.data.model.spotify;

import java.util.List;

public class SpotifyRecommendationResponse {
    private List<Track> tracks;
    private List<Seed> seeds;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
    }

    public static class Track {
        private Album album;
        private List<Artist> artists;
        private String id;
        private String name;
        private String preview_url;
        private String uri;
        private int popularity;
        private boolean is_playable;

        // getters and setters
        public Album getAlbum() {
            return album;
        }

        public void setAlbum(Album album) {
            this.album = album;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public void setArtists(List<Artist> artists) {
            this.artists = artists;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPreview_url() {
            return preview_url;
        }

        public void setPreview_url(String preview_url) {
            this.preview_url = preview_url;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public int getPopularity() {
            return popularity;
        }

        public void setPopularity(int popularity) {
            this.popularity = popularity;
        }

        public boolean isIs_playable() {
            return is_playable;
        }

        public void setIs_playable(boolean is_playable) {
            this.is_playable = is_playable;
        }
    }

    public static class Album {
        private String album_type;
        private List<Artist> artists;
        private String id;
        private List<Image> images;
        private String name;
        private String release_date;

        // getters and setters
        public String getAlbum_type() {
            return album_type;
        }

        public void setAlbum_type(String album_type) {
            this.album_type = album_type;
        }

        public List<Artist> getArtists() {
            return artists;
        }

        public void setArtists(List<Artist> artists) {
            this.artists = artists;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Image> getImages() {
            return images;
        }

        public void setImages(List<Image> images) {
            this.images = images;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }
    }

    public static class Artist {
        private String id;
        private String name;
        private String uri;

        // getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class Image {
        private int height;
        private int width;
        private String url;

        // getters and setters
        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Seed {
        private int initialPoolSize;
        private int afterFilteringSize;
        private int afterRelinkingSize;
        private String id;
        private String type;
        private String href;

        // getters and setters
        public int getInitialPoolSize() {
            return initialPoolSize;
        }

        public void setInitialPoolSize(int initialPoolSize) {
            this.initialPoolSize = initialPoolSize;
        }

        public int getAfterFilteringSize() {
            return afterFilteringSize;
        }

        public void setAfterFilteringSize(int afterFilteringSize) {
            this.afterFilteringSize = afterFilteringSize;
        }

        public int getAfterRelinkingSize() {
            return afterRelinkingSize;
        }

        public void setAfterRelinkingSize(int afterRelinkingSize) {
            this.afterRelinkingSize = afterRelinkingSize;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
