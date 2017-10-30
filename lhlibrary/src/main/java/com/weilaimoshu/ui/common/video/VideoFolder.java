package com.weilaimoshu.ui.common.video;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huanghaibin_dev
 * on 2016/7/11.
 */
public class VideoFolder implements Serializable {
    private String name;
    private String path;
    private String albumPath;
    private ArrayList<Video> images = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Video> getImages() {
        return images;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof VideoFolder) {
            if (((VideoFolder) o).getPath() == null && path != null)
                return false;
            String oPath = ((VideoFolder) o).getPath().toLowerCase();
            return oPath.equals(this.path.toLowerCase());
        }
        return false;
    }
}
