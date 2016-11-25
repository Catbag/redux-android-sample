package br.com.catbag.gifreduxsample.asyncs.data.net.rest.riffsy.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
public final class RiffsyGif {
  @SerializedName("url")
  private final String mUrl;

  @SerializedName("preview")
  private final String mPreview;

  /**
   * No args constructor for use in serialization
   */
  public RiffsyGif() {
    this(new Builder());
  }

  public RiffsyGif(Builder builder) {
    this.mUrl = builder.mUrl;
    this.mPreview = builder.mPreview;
  }

  public String url() {
    return mUrl;
  }

  public String preview() {
    return mPreview;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static class Builder {
    private String mUrl;
    private String mPreview;

    public Builder() {
    }

    public Builder(RiffsyGif riffsyGif) {
      this.mUrl = riffsyGif.mUrl;
      this.mPreview = riffsyGif.mPreview;
    }

    public Builder url(String url) {
      this.mUrl = url;
      return this;
    }

    public Builder preview(String preview) {
      this.mPreview = preview;
      return this;
    }

    public RiffsyGif build() {
      return new RiffsyGif(this);
    }
  }
}
