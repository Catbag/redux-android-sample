package br.com.catbag.gifreduxsample.models;

/**
 Copyright 26/10/2016
 Felipe Piñeiro (fpbitencourt@gmail.com),
 Nilton Vasques (nilton.vasques@gmail.com) and
 Raul Abreu (raulccabreu@gmail.com)

 Be free to ask for help, email us!

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied. See the License for the specific language governing permissions and limitations under
 the License.
 **/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.io.IOException;

@SuppressWarnings("PMD.BooleanGetMethodName")
@Value.Immutable
@JsonSerialize(as = ImmutableGif.class)
@JsonDeserialize(as = ImmutableGif.class)
public abstract class Gif {

    public enum Status {
        PAUSED, LOOPING, DOWNLOADING, DOWNLOADED, NOT_DOWNLOADED, DOWNLOAD_FAILED
    }

    //abstract method are immutable by default
    public abstract String getUuid();

    @Value.Default
    public String getPath() {
        return "";
    }

    public abstract String getUrl();
    
    public abstract String getTitle();

    @Value.Default
    public boolean getWatched() {
        return false;
    }

    @Value.Default
    public Status getStatus() {
        return Status.NOT_DOWNLOADED;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static Gif fromJson(String json) throws IOException {
        return new ObjectMapper().readValue(json, Gif.class);
    }

}