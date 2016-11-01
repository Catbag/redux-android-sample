package br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
public final class RiffsyMedia {
  @SerializedName("tinygif")
  private final RiffsyGif mRiffsyGif;

  /**
   * No args constructor for use in serialization
   */
  public RiffsyMedia() {
    this(new Builder());
  }

  public RiffsyMedia(Builder builder) {
    this.mRiffsyGif = builder.mRiffsyGif;
  }

  public RiffsyGif gif() {
    return mRiffsyGif;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static class Builder {
    private RiffsyGif mRiffsyGif;

    public Builder() {
    }

    public Builder(RiffsyMedia riffsyMedia) {
      this.mRiffsyGif = riffsyMedia.mRiffsyGif;
    }

    public Builder gif(RiffsyGif riffsyGif) {
      this.mRiffsyGif = riffsyGif;
      return this;
    }

    public RiffsyMedia build() {
      return new RiffsyMedia(this);
    }
  }
}
