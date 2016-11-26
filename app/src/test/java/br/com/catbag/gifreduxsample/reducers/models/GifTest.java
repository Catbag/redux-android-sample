package br.com.catbag.gifreduxsample.reducers.models;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.junit.Test;

import java.io.IOException;

import br.com.catbag.gifreduxsample.models.Gif;
import br.com.catbag.gifreduxsample.models.ImmutableGif;

import static junit.framework.Assert.assertEquals;

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

public class GifTest {

    @Test
    public void testGifToJson() throws JsonProcessingException {
        assertEquals(getDefaultGif().toJson(), getDefaultJSON());
    }

    @Test
    public void testJsonToAppState() throws IOException {
        assertEquals(Gif.fromJson(getDefaultJSON()), getDefaultGif());
    }

    private String getDefaultJSON() {
        return "{\"uuid\":\"1\",\"path\":\"\",\"url\":\"https://media.giphy.com/media/"
                + "l0HlE56oAxpngfnWM/giphy.gif\",\"title\":\"Gif Default\","
                + "\"watched\":false,\"status\":\"DOWNLOADED\"}";
    }

    private Gif getDefaultGif() {
        return ImmutableGif.builder()
                .uuid("1")
                .title("Gif Default")
                .url("https://media.giphy.com/media/l0HlE56oAxpngfnWM/giphy.gif")
                .status(Gif.Status.DOWNLOADED)
                .build();
    }

}
