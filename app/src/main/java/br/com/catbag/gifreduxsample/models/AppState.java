package br.com.catbag.gifreduxsample.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import org.immutables.value.Value;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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

@SuppressWarnings("PMD.BooleanGetMethodName")
@Value.Immutable
@JsonSerialize(as = ImmutableAppState.class)
@JsonDeserialize(as = ImmutableAppState.class)
public abstract class AppState {

    @Value.Default
    public Map<String, Gif> getGifs() {
        return new LinkedHashMap<>();
    }

    @Value.Default
    public boolean getHasMoreGifs() {
        return true;
    }

    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static AppState fromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new GuavaModule());
        return mapper.readValue(json, AppState.class);
    }

}
