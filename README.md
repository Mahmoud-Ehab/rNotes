# rNotes

## What is rNotes?
rNotes is a java-based Android app.
Since the user hits the record button to begin, it records voice using the phone's internal microphone.
As soon as the user clicks the button again, it saves the last 5 – 15 seconds to the device (the user can select 5, 10, or 15 seconds).

### How is this useful?
Students might find rNotes helpful. They can use it to easily record notes in lectures rather than typing.
It could also be useful in debates.

## How it works?
There are just two activities in the app; MainActivity and NoteActivity.

**MainActivity** is the default activity that will be shown to the user when he opens the application. It contains bottom navigation with two buttons, each button will show different layout.
The first layout has been designed for recording. And the second one for displaying a list of the recorded notes, so the user can select one of them.<br>
[android.media.AudioRecord](https://developer.android.com/reference/android/media/AudioRecord) library is used in order to record voice.

**NoteActivity** will take place when the user clicks on any note from the note list in order to edit it, delete it or listen to it.
By default, each note will be saved with the date and time, when it was saved at, as “title”. But it is just a silly way to distinguish each note from the other, so the user can edit any note title when he moves to NoteActivity and change it to something meaningful.
Notes are being saved at the internal cache directory of the app, and it can be deleted from NoteActivity.<br>
[android.media.AudioTrack](https://developer.android.com/reference/android/media/AudioTrack) library is used in order to play the recorded note.
