# flux-android-sample

A Android sample that implements Flux architecture with React and NoSQL

# Architecture
![Gif Sample Flux Architecture](architecture_gif_sample.png)

#App State Example
``` javascript
{
    gifLocalPath: "path",
    gifDownloadFailureMsg: "error",
    gifUrl: "url",
    gifTitle: "string",
    gifState: PAUSED, //PAUSED, LOOPING, DOWNLOADING, DOWNLOADED, NOT_DOWNLOADED
    gifWatched: false
}
```

#Good Pratices

###Actions Creators

- Pay attention on action creation UI calls that do something async and 
dispatch action, the main action has to be dispatched before other async
created actions, and its not a good practice dispatch two actions on the 
same synchronous cycle.
