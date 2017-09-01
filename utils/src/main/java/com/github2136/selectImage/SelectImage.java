package com.github2136.selectImage;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by yubin on 2017/8/26.
 */

public class SelectImage {
    /**
     * The description of the image
     * <P>Type: TEXT</P>
     */
    private String description;

    /**
     * The picasa id of the image
     * <P>Type: TEXT</P>
     */
    private String picasa_id;

    /**
     * Whether the video should be published as private  or private
     * <P>Type: INTEGER</P>
     */
    private int is_private;

    /**
     * The latitude where the image was captured.
     * <P>Type: DOUBLE</P>
     */
    private double latitude;

    /**
     * The longitude where the image was captured.
     * <P>Type: DOUBLE</P>
     */
    private double longitude;

    /**
     * The date & time that the image was taken in units
     * of milliseconds since jan 1, 1970.
     * <P>Type: INTEGER</P>
     */
    private int date_taken;

    /**
     * The orientation for the image expressed as degrees.
     * Only degrees 0, 90, 180, 270 will work.
     * <P>Type: INTEGER</P>
     */
    private int orientation;

    /**
     * The mini thumb id.
     * <P>Type: INTEGER</P>
     */
    private int mini_thumb_magic;

    /**
     * The bucket id of the image. This is a read-only property that
     * is automatically computed from the DATA column.
     * <P>Type: TEXT</P>
     */
    private String bucket_id;

    /**
     * The bucket display name of the image. This is a read-only property that
     * is automatically computed from the DATA column.
     * <P>Type: TEXT</P>
     */
    private String bucket_display_name;

    /**
     * The unique ID for a row.
     * <P>Type: INTEGER (long)</P>
     */
    private long _id;


    /**
     * Path to the file on disk.
     * <p>
     * Note that apps may not have filesystem permissions to directly access
     * this path. Instead of trying to open this path directly, apps should
     * use {@link ContentResolver#openFileDescriptor(Uri, String)} to gain
     * access.
     * <p>
     * Type: TEXT
     */
    private String data;

    /**
     * The size of the file in bytes
     * <P>Type: INTEGER (long)</P>
     */
    private long size;

    /**
     * The display name of the file
     * <P>Type: TEXT</P>
     */
    private String display_name;

    /**
     * The title of the content
     * <P>Type: TEXT</P>
     */
    private String title;

    /**
     * The time the file was added to the media provider
     * Units are seconds since 1970.
     * <P>Type: INTEGER (long)</P>
     */
    private long date_added;

    /**
     * The time the file was last modified
     * Units are seconds since 1970.
     * NOTE: This is for internal use by the media scanner.  Do not modify this field.
     * <P>Type: INTEGER (long)</P>
     */
    private long date_modified;

    /**
     * The MIME type of the file
     * <P>Type: TEXT</P>
     */
    private String mime_type;


    /**
     * The width of the image/video in pixels.
     */
    private int width;

    /**
     * The height of the image/video in pixels.
     */
    private int height;

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicasa_id() {
        return picasa_id == null ? "" : picasa_id;
    }

    public void setPicasa_id(String picasa_id) {
        this.picasa_id = picasa_id;
    }

    public int getIs_private() {
        return is_private;
    }

    public void setIs_private(int is_private) {
        this.is_private = is_private;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDate_taken() {
        return date_taken;
    }

    public void setDate_taken(int date_taken) {
        this.date_taken = date_taken;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getMini_thumb_magic() {
        return mini_thumb_magic;
    }

    public void setMini_thumb_magic(int mini_thumb_magic) {
        this.mini_thumb_magic = mini_thumb_magic;
    }

    public String getBucket_id() {
        return bucket_id == null ? "" : bucket_id;
    }

    public void setBucket_id(String bucket_id) {
        this.bucket_id = bucket_id;
    }

    public String getBucket_display_name() {
        return bucket_display_name == null ? "" : bucket_display_name;
    }

    public void setBucket_display_name(String bucket_display_name) {
        this.bucket_display_name = bucket_display_name;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getData() {
        return data == null ? "" : data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplay_name() {
        return display_name == null ? "" : display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate_added() {
        return date_added;
    }

    public void setDate_added(long date_added) {
        this.date_added = date_added;
    }

    public long getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(long date_modified) {
        this.date_modified = date_modified;
    }

    public String getMime_type() {
        return mime_type == null ? "" : mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
