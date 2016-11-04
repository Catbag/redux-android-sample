package br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
public final class RiffsyResult {
  @SerializedName("media")
  private final List<RiffsyMedia> mRiffsyMedia;

  @SerializedName("title")
  private final String mTitle;

  /**
   * No args constructor for use in serialization
   */
  public RiffsyResult() {
    this(new Builder());
  }

  public RiffsyResult(Builder builder) {
    this.mRiffsyMedia = builder.mRiffsyMedia;
    this.mTitle = builder.mTitle;
  }

  public List<RiffsyMedia> media() {
    return mRiffsyMedia;
  }

  public String title() {
    return mTitle;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static class Builder {
    private List<RiffsyMedia> mRiffsyMedia;
    private String mTitle;

    public Builder() {
    }

    public Builder(RiffsyResult result) {
      this.mRiffsyMedia = result.mRiffsyMedia;
      this.mTitle = result.mTitle;
    }

    public Builder media(List<RiffsyMedia> riffsyMedia) {
      this.mRiffsyMedia = riffsyMedia;
      return this;
    }

    public Builder title(String title) {
      this.mTitle = title;
      return this;
    }

    public RiffsyResult build() {
      return new RiffsyResult(this);
    }
  }
}
