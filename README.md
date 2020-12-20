# rNotes

## What is rNotes?
rNotes is an android app written in java. It records voice using the phone internal mic since the user presses the record button to start. And it saves the last 5 – 15 seconds to the device (the user can select 5, 10, or 15), as soon as the user presses the button again. Meanly, it records and saves voice notes, as simple as that.

### How is this useful?
rNotes could be useful for students. They can record notes easily in lectures using it, rather than typing.  Also, it might be useful in debates.

## How it works?
There are just two activities in the app; MainActivity and NoteActivity.

**MainActivity** is the default activity that will be shown to the user when he opens the application. It contains bottom navigation with two buttons, each button will show different layout.
The first layout has been designed for recording. And the second one for displaying a list of the recorded notes, so the user can select one of them.<br>
[android.media.AudioRecord](https://developer.android.com/reference/android/media/AudioRecord) library is used in order to record voice.

**NoteActivity** will take place when the user clicks on any note from the note list in order to edit it, delete it or listen to it.
By default, each note will be saved with the date and time, when it was saved at, as “title”. But it is just a silly way to distinguish each note from the other, so the user can edit any note title when he moves to NoteActivity and change it to something meaningful.
Notes are being saved at the internal cache directory of the app, and it can be deleted from NoteActivity.<br>
[android.media.AudioTrack](https://developer.android.com/reference/android/media/AudioTrack) library is used in order to play the recorded note.
