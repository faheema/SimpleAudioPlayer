/*
 * Copyright 2018 Google Inc. All rights reserved to FAHMAPPS.Com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sap.com.fahm.simpleaudioplayer.player;

/**
 * Created by Faheem on 20/12/17.
 */

public interface IAudioPlayer {
    void loadMedia(int resourceId);
    void release();
    boolean isPlaying();
    void play();
    void reset();
    void stop();
    void pause();
    void initializeProgressCallback();
    void seekTo(int position);
}
