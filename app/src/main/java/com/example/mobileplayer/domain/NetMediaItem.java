package com.example.mobileplayer.domain;

import java.io.Serializable;
import java.util.List;

public class NetMediaItem implements Serializable {
//    "id": 75289,
//            "movieName": "《速度与激情:特别行动》曝全新中文预告",
//            "coverImg": "http://img5.mtime.cn/mg/2019/06/29/002009.16684021_120X90X4.jpg",
//            "movieId": 254114,
//            "url": "http://vfx.mtime.cn/Video/2019/06/29/mp4/190629004821240734.mp4",
//            "hightUrl": "http://vfx.mtime.cn/Video/2019/06/29/mp4/190629004821240734.mp4",
//            "videoTitle": "速度与激情：特别行动 中文预告",
//            "videoLength": 146,
//            "rating": -1,
//            "type": [
//            "动作",
//            "冒险"
//            ],
//            "summary": ""
    private int id;
    private String movieName;
    private String coverImg;
    private int movieId;
    private String url;
    private String hightUrl;
    private String videoTitle;
    private int videoLength;
    private long rating;
    private List<String> type;
    private String summary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHightUrl() {
        return hightUrl;
    }

    public void setHightUrl(String hightUrl) {
        this.hightUrl = hightUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public void setVideoLength(int videoLength) {
        this.videoLength = videoLength;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "NetMediaItem{" +
                "id=" + id +
                ", movieName='" + movieName + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", movieId=" + movieId +
                ", url='" + url + '\'' +
                ", hightUrl='" + hightUrl + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoLength=" + videoLength +
                ", rating=" + rating +
                ", type=" + type +
                ", summary='" + summary + '\'' +
                '}';
    }
}
