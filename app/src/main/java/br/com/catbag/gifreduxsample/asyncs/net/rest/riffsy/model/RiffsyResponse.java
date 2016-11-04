package br.com.catbag.gifreduxsample.asyncs.net.rest.riffsy.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Riffsy Api Response.
 * eg. https://api.riffsy.com/v1/search?key=LIVDSRZULELA&tag=goodluck&limit=10
 *
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
public final class RiffsyResponse {
  @SerializedName("results")
  private final List<RiffsyResult> mResults;

  @SerializedName("next")
  private final Float mNext;

  /**
   * No args constructor for use in serialization
   */
  public RiffsyResponse() {
    this(new Builder());
  }

  public RiffsyResponse(Builder builder) {
    this.mResults = builder.mResults;
    this.mNext = builder.mNext;
  }

  public List<RiffsyResult> results() {
    return mResults;
  }

  public Float next() {
    return mNext;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static class Builder {
    private List<RiffsyResult> mResults;
    private Float mNext;

    public Builder() {
    }

    public Builder(RiffsyResponse response) {
      this.mResults = response.mResults;
    }

    public Builder results(List<RiffsyResult> results) {
      this.mResults = results;
      return this;
    }

    public Builder next(Float next) {
      this.mNext = next;
      return this;
    }

    public RiffsyResponse build() {
      return new RiffsyResponse(this);
    }
  }
}
